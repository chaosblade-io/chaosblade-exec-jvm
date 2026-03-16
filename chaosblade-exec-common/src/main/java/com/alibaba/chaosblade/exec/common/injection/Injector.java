/*
 * Copyright 2025 The ChaosBlade Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.alibaba.chaosblade.exec.common.injection;

import com.alibaba.chaosblade.exec.common.aop.CustomMatcher;
import com.alibaba.chaosblade.exec.common.aop.EnhancerModel;
import com.alibaba.chaosblade.exec.common.center.ManagerFactory;
import com.alibaba.chaosblade.exec.common.center.StatusMetric;
import com.alibaba.chaosblade.exec.common.constant.ModelConstant;
import com.alibaba.chaosblade.exec.common.exception.InterruptProcessException;
import com.alibaba.chaosblade.exec.common.model.Model;
import com.alibaba.chaosblade.exec.common.model.ModelSpec;
import com.alibaba.chaosblade.exec.common.model.action.ActionSpec;
import com.alibaba.chaosblade.exec.common.model.action.returnv.UnsupportedReturnTypeException;
import com.alibaba.chaosblade.exec.common.model.matcher.MatcherModel;
import com.alibaba.chaosblade.exec.common.util.ModelUtil;
import com.alibaba.chaosblade.exec.common.util.StringUtil;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import org.mvel2.MVEL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** @author Changjun Xiao */
public class Injector {
  private static final Logger LOGGER = LoggerFactory.getLogger(Injector.class);

  private static Cache<String, Serializable> EXP_CACHE =
      CacheBuilder.newBuilder().expireAfterAccess(60, TimeUnit.MINUTES).build();

  /**
   * Inject
   *
   * @param enhancerModel
   * @throws InterruptProcessException
   */
  public static void inject(EnhancerModel enhancerModel) throws InterruptProcessException {
    String target = enhancerModel.getTarget();
    List<StatusMetric> statusMetrics = ManagerFactory.getStatusManager().getExpByTarget(target);
    for (StatusMetric statusMetric : statusMetrics) {
      Model model = statusMetric.getModel();
      if (!compare(model, enhancerModel)) {
        continue;
      }
      try {
        boolean pass =
            passParamExp(
                target, model.getMatcher().get(ModelConstant.PARAM_EXP_FLAG), enhancerModel);
        if (!pass) {
          LOGGER.info("passParamExp by: {}", model);
          break;
        }

        pass = limitAndIncrease(statusMetric);
        if (!pass) {
          LOGGER.info("Limited by: {}", model);
          break;
        }
        LOGGER.info("Match rule: {}", model);
        enhancerModel.merge(model);
        ModelSpec modelSpec = ManagerFactory.getModelSpecManager().getModelSpec(target);
        ActionSpec actionSpec = modelSpec.getActionSpec(model.getActionName());
        actionSpec.getActionExecutor().run(enhancerModel);
      } catch (InterruptProcessException e) {
        throw e;
      } catch (UnsupportedReturnTypeException e) {
        LOGGER.warn("unsupported return type for return experiment", e);
        // decrease the count if throw unexpected exception
        statusMetric.decrease();
      } catch (Throwable e) {
        LOGGER.warn("inject exception", e);
        // decrease the count if throw unexpected exception
        statusMetric.decrease();
      }
      // break it if compared success
      break;
    }
  }

  /**
   * @param statusMetric
   * @return
   */
  private static boolean limitAndIncrease(StatusMetric statusMetric) {
    Model model = statusMetric.getModel();
    String limitCount = model.getMatcher().get(ModelConstant.EFFECT_COUNT_MATCHER_NAME);
    if (!StringUtil.isBlank(limitCount)) {
      Long count = Long.valueOf(limitCount);
      if (statusMetric.getCount() >= count) {
        return false;
      }
      return statusMetric.increaseWithLock(count);
    }
    String limitPercent = model.getMatcher().get(ModelConstant.EFFECT_PERCENT_MATCHER_NAME);
    if (!StringUtil.isBlank(limitPercent)) {
      Integer percent = Integer.valueOf(limitPercent);
      Random random = new Random();
      int randomValue = random.nextInt(100) + 1;
      if (randomValue > percent) {
        return false;
      }
    }
    statusMetric.increase();
    return true;
  }

  /**
   * Compare the experiment rule with the date collected by method enhancer
   *
   * @param model
   * @param enhancerModel
   * @return
   */
  private static boolean compare(Model model, EnhancerModel enhancerModel) {
    MatcherModel matcher = model.getMatcher();
    if (matcher == null) {
      return true;
    }
    MatcherModel enhancerMatcherModel = enhancerModel.getMatcherModel();
    if (enhancerMatcherModel == null) {
      LOGGER.debug("enhancerMatcherModel is null, match fail");
      return false;
    }
    Map<String, Object> matchers = matcher.getMatchers();
    for (Entry<String, Object> entry : matchers.entrySet()) {
      String keyName = entry.getKey();
      LOGGER.debug("match key:{} beginning...", keyName);
      // filter effect count and effect percent
      if (keyName.equalsIgnoreCase(ModelConstant.EFFECT_COUNT_MATCHER_NAME)
          || keyName.equalsIgnoreCase(ModelConstant.EFFECT_PERCENT_MATCHER_NAME)
          || keyName.equalsIgnoreCase(ModelConstant.PARAM_EXP_FLAG)) {
        continue;
      }

      Object value = enhancerMatcherModel.get(keyName);
      if (value == null) {
        LOGGER.debug("match key:{}, value is null, match fail", keyName);
        return false;
      }

      CustomMatcher customMatcher = enhancerModel.getMatcher(keyName);
      if (customMatcher == null) {
        // default match
        if (String.valueOf(value).equalsIgnoreCase(String.valueOf(entry.getValue()))) {
          LOGGER.debug("custom match key:{}, value equals, continue", keyName);
          continue;
        }

        // regex match
        if (keyName.endsWith(ModelConstant.REGEX_PATTERN_FLAG)) {
          LOGGER.debug("regex pattern: {}", keyName);
          boolean isMatch =
              Pattern.matches(String.valueOf(entry.getValue()), String.valueOf(value));
          if (isMatch) {
            LOGGER.debug("value: {} match regex pattern: {}", value, entry.getValue());
            continue;
          }
        }
        return false;
      }
      // business param match
      if (keyName.equals(ModelConstant.BUSINESS_PARAMS)) {
        Map<String, Map<String, String>> expMap = (Map<String, Map<String, String>>) value;
        value = expMap.get(ModelUtil.getIdentifier(model));
      }
      // custom match
      if (keyName.endsWith(ModelConstant.REGEX_PATTERN_FLAG)
          ? customMatcher.regexMatch(String.valueOf(entry.getValue()), value)
          : customMatcher.match(String.valueOf(entry.getValue()), value)) {
        continue;
      }
      LOGGER.debug("match key:{} fail", keyName);
      return false;
    }
    return !matchers.isEmpty();
  }

  /**
   * Compare the experiment rule with the date collected by method enhancer
   *
   * @param paramExp
   * @param enhancerModel
   * @return
   */
  private static boolean passParamExp(String target, String paramExp, EnhancerModel enhancerModel) {
    LOGGER.info("passParamExp target: {} paramExp: {}", target, paramExp);
    Object[] methodArguments = enhancerModel.getMethodArguments();
    if (LOGGER.isDebugEnabled()) {
      LOGGER.debug(
          "passParamExp target: {} paramExp: {} methodArguments: {}",
          target,
          paramExp,
          methodArguments);
    }
    if (null == methodArguments || methodArguments.length == 0 || StringUtil.isBlank(paramExp)) {
      return true;
    }

    Map<String, Object> paramMap = new HashMap<String, Object>(methodArguments.length);

    for (int i = 0; i < methodArguments.length; i++) {
      paramMap.put("arg" + i, methodArguments[i]);
    }

    Serializable exp = EXP_CACHE.getIfPresent(paramExp);

    try {
      if (exp == null) {
        synchronized (Injector.class) {
          Serializable compileExpression = MVEL.compileExpression(paramExp);
          EXP_CACHE.put(paramExp, compileExpression);
          exp = compileExpression;
        }
      }
      if (LOGGER.isDebugEnabled()) {
        LOGGER.debug("passParamExp exp: {} paramMap: {}", exp, paramMap);
      }
      Object result = MVEL.executeExpression(exp, paramMap);
      if (LOGGER.isDebugEnabled()) {
        LOGGER.debug("passParamExp result: {}", result);
      }
      if (Boolean.TRUE.equals(result)) {
        return true;
      }
    } catch (Throwable e) {
      LOGGER.error("matchParamExp err", e);
    }
    return false;
  }
}
