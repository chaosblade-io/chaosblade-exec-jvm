package com.alibaba.chaosblade.exec.plugin.elasticsearch;

import com.alibaba.chaosblade.exec.common.aop.PointCut;
import com.alibaba.chaosblade.exec.common.aop.matcher.clazz.ClassMatcher;
import com.alibaba.chaosblade.exec.common.aop.matcher.clazz.NameClassMatcher;
import com.alibaba.chaosblade.exec.common.aop.matcher.clazz.OrClassMatcher;
import com.alibaba.chaosblade.exec.common.aop.matcher.method.MethodMatcher;
import com.alibaba.chaosblade.exec.common.aop.matcher.method.NameMethodMatcher;
import com.alibaba.chaosblade.exec.common.aop.matcher.method.OrMethodMatcher;


/**
 * @Author Yuhan Tang
 * @package: com.alibaba.chaosblade.exec.plugin.hbase
 * @Date 2020-10-30 14:42
 */
public class ElasticSearchPointCut implements PointCut {

    private static final String ES_REST_CLASS = "org.elasticsearch.client.RestHighLevelClient";
    private static final String ES_REST_METHOD_SYNC = "internalPerformRequest";
    private static final String ES_REST_METHOD_ASYNC = "internalPerformRequestAsync";

    private static final String ES_CLIENT_CLASS = "org.elasticsearch.client.support.AbstractClient";
    private static final String ES_CLIENT_METHOD_EXECUTE = "execute";
    private static final String ES_CLIENT_METHOD_INDEX = "prepareIndex";
    private static final String ES_CLIENT_METHOD_DELETE = "prepareDelete";
    private static final String ES_CLIENT_METHOD_GET = "prepareGet";

    @Override
    public ClassMatcher getClassMatcher() {
        return new OrClassMatcher().or(new NameClassMatcher(ES_REST_CLASS)).or(new NameClassMatcher(ES_CLIENT_CLASS));
    }

    @Override
    public MethodMatcher getMethodMatcher() {
        OrMethodMatcher orMethodMatcher = new OrMethodMatcher();
        orMethodMatcher.or(new NameMethodMatcher(ES_REST_METHOD_SYNC))
                .or(new NameMethodMatcher(ES_REST_METHOD_ASYNC))
                .or(new NameMethodMatcher(ES_CLIENT_METHOD_EXECUTE))
                .or(new NameMethodMatcher(ES_CLIENT_METHOD_INDEX))
                .or(new NameMethodMatcher(ES_CLIENT_METHOD_DELETE))
                .or(new NameMethodMatcher(ES_CLIENT_METHOD_GET));
        return orMethodMatcher;
    }
}
