package com.alibaba.chaosblade.exec.plugin.redisson;

import com.alibaba.chaosblade.exec.common.aop.BeforeEnhancer;
import com.alibaba.chaosblade.exec.common.aop.EnhancerModel;
import com.alibaba.chaosblade.exec.common.model.matcher.MatcherModel;
import com.alibaba.chaosblade.exec.common.util.JsonUtil;
import com.alibaba.chaosblade.exec.common.util.ReflectUtil;
import org.graalvm.util.CollectionsUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * @author xueshaoyi
 * @Date 2020/11/23 上午11:40
 **/
public class RedissonEnhancer extends BeforeEnhancer {

	public static final String CHARSET = "UTF-8";
	private static final Logger LOGGER = LoggerFactory.getLogger(RedissonEnhancer.class);

	@Override
	public EnhancerModel doBeforeAdvice(ClassLoader classLoader, String className, Object object, Method method,
	                                    Object[] methodArguments) throws Exception {
		if (object == null) {
			LOGGER.info("he necessary parameters is null");
			return null;
		}


		Object command = ReflectUtil.getFieldValue(object, "command", false);
		boolean redisCommand =
				ReflectUtil.isAssignableFrom(
						classLoader, command.getClass(), "org.redisson.client.protocol.RedisCommand");
		String cmd = null;
		if (redisCommand) {
			cmd = ReflectUtil.invokeMethod(command, "getName", new Object[0], false);
		}

		String key = null;
		Object[] params = ReflectUtil.getFieldValue(object, "params", false);
		if (params != null && params.length > 1) {
			key = new String((byte[])command, CHARSET);
		}


		MatcherModel matcherModel = new MatcherModel();
		if (cmd != null) {
			matcherModel.add(RedissonConstant.COMMAND_TYPE_MATCHER_NAME, cmd.toLowerCase());
		}
		if (key != null) {
			matcherModel.add(RedissonConstant.KEY_MATCHER_NAME, key);
		}
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("redisson matchers: {}", JsonUtil.writer().writeValueAsString(matcherModel));
		}
		return new EnhancerModel(classLoader, matcherModel);
	}
}
