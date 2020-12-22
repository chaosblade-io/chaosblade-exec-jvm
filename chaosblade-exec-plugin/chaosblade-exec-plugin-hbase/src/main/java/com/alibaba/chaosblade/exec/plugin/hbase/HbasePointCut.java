package com.alibaba.chaosblade.exec.plugin.hbase;

import com.alibaba.chaosblade.exec.common.aop.PointCut;
import com.alibaba.chaosblade.exec.common.aop.matcher.clazz.ClassMatcher;
import com.alibaba.chaosblade.exec.common.aop.matcher.clazz.NameClassMatcher;
import com.alibaba.chaosblade.exec.common.aop.matcher.method.MethodMatcher;
import com.alibaba.chaosblade.exec.common.aop.matcher.method.NameMethodMatcher;


/**
 * @Author Yuhan Tang
 * @package: com.alibaba.chaosblade.exec.plugin.hbase
 * @Date 2020-10-30 14:42
 */
public class HbasePointCut  implements PointCut {

    private static final String HBASE_TABLE_CLASS = "org.apache.hadoop.hbase.TableName";
    private static final String HBASE_VALUEOF_METHOD = "valueOf";

    @Override
    public ClassMatcher getClassMatcher() {
        return new NameClassMatcher(HBASE_TABLE_CLASS);
    }

    @Override
    public MethodMatcher getMethodMatcher() {
        return new NameMethodMatcher(HBASE_VALUEOF_METHOD);
    }
}
