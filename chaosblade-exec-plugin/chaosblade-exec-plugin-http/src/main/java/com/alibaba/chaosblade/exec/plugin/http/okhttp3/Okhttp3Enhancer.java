package com.alibaba.chaosblade.exec.plugin.http.okhttp3;

import com.alibaba.chaosblade.exec.common.aop.BeforeEnhancer;
import com.alibaba.chaosblade.exec.common.aop.EnhancerModel;
import com.alibaba.chaosblade.exec.common.model.matcher.MatcherModel;
import com.alibaba.chaosblade.exec.common.util.ReflectUtil;
import com.alibaba.chaosblade.exec.plugin.http.HttpConstant;
import com.alibaba.chaosblade.exec.plugin.http.UrlUtils;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

import static com.alibaba.chaosblade.exec.plugin.http.HttpConstant.OKHTTP3;

/**
 * 具体实现
 *
 * @author pengpj
 * @date 2020-7-13
 */
public class Okhttp3Enhancer extends BeforeEnhancer {

    private static final Logger LOGGER = LoggerFactory.getLogger(Okhttp3Enhancer.class);

    private static final String GET_REQUEST = "request";
    private static final String GET_URL = "url";

    /**
     * okhttp3.RealCall 的 url ，从成员变量 originalRequest 中获取
     */
    @Override
    public EnhancerModel doBeforeAdvice(ClassLoader classLoader,
                                        String className,
                                        Object object,
                                        Method method,
                                        Object[] methodArguments) throws Exception {
        MatcherModel matcherModel = new MatcherModel();
        matcherModel.add(HttpConstant.URI_KEY, getUrl(object));
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("http matchers: {}", JSON.toJSONString(matcherModel));
        }
        EnhancerModel enhancerModel = new EnhancerModel(classLoader, matcherModel);
        enhancerModel.addMatcher(OKHTTP3, "true");
        return new EnhancerModel(classLoader, matcherModel);
    }

    /**
     * get url
     */
    private String getUrl(Object realCall) throws Exception {
        Object request = ReflectUtil.invokeMethod(realCall, GET_REQUEST, new Object[0], false);
        if (request == null) {
            LOGGER.warn("okhttp3 Request is null, can not get necessary values.");
            return null;
        }

        Object requestUrl = ReflectUtil.invokeMethod(request, GET_URL, new Object[0], false);
        if (requestUrl == null) {
            LOGGER.warn("okhttp3 Url is null, can not get necessary values.");
            return null;
        }
        String path = UrlUtils.getUrlExcludeQueryParameters(requestUrl.toString());
        LOGGER.info("okhttp3 path : {}", path);
        return path;
    }

}
