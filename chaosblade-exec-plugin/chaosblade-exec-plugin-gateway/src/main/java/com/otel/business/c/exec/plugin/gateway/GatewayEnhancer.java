package com.otel.business.c.exec.plugin.gateway;

import com.otel.business.c.exec.common.aop.BeforeEnhancer;
import com.otel.business.c.exec.common.aop.EnhancerModel;
import com.otel.business.c.exec.common.model.matcher.MatcherModel;
import com.otel.business.c.exec.common.util.JsonUtil;
import com.otel.business.c.exec.common.util.ReflectUtil;
import java.lang.reflect.Method;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author wb-shd671576
 *
 * @package: com.otel.business.c.exec.plugin.gateway @Date 2021-07-29
 */
public class GatewayEnhancer extends BeforeEnhancer {

  private static final Logger LOGGER = LoggerFactory.getLogger(GatewayEnhancer.class);

  @Override
  public EnhancerModel doBeforeAdvice(
      ClassLoader classLoader,
      String className,
      Object object,
      Method method,
      Object[] methodArguments)
      throws Exception {
    if (methodArguments == null || object == null) {
      LOGGER.warn("The necessary parameters is null");
      return null;
    }
    Object serverWebExchange = methodArguments[0];
    Object request =
        ReflectUtil.invokeMethod(serverWebExchange, "getRequest", new Object[] {}, false);
    MatcherModel matcherModel = new MatcherModel();
    if (request != null) {
      Object url = ReflectUtil.invokeMethod(request, "getURI", new Object[] {}, false);
      if (url != null) {
        String path =
            ReflectUtil.invokeMethod(request, "getPath", new Object[] {}, false).toString();
        matcherModel.add(GatewayConstant.GET_REQUST_PATH, path);
      }
    }
    LOGGER.debug("Gateway matchers: {}", JsonUtil.writer().writeValueAsString(matcherModel));
    return new EnhancerModel(classLoader, matcherModel);
  }
}
