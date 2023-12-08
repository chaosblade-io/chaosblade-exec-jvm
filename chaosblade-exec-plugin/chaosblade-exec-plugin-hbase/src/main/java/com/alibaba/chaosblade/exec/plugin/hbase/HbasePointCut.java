package com.alibaba.chaosblade.exec.plugin.hbase;

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
public class HbasePointCut implements PointCut {

    private static final String HBASE_TABLE_CLASS = "org.apache.hadoop.hbase.TableName";
    private static final String HBASE_VALUEOF_METHOD = "valueOf";
    private static final String HBASE_COLUMN_CLASS = "org.apache.hadoop.hbase.client.Get";
    private static final String HBASE_ADDCOLUMN_METHOD = "addFamily";

    @Override
    public ClassMatcher getClassMatcher() {
        OrClassMatcher orClassMatcher = new OrClassMatcher();
        orClassMatcher.or(new NameClassMatcher(HBASE_TABLE_CLASS)).or(new NameClassMatcher(HBASE_COLUMN_CLASS));
        return orClassMatcher;
    }

    @Override
    public MethodMatcher getMethodMatcher() {
        OrMethodMatcher orMethodMatcher = new OrMethodMatcher();
        orMethodMatcher.or(new NameMethodMatcher(HBASE_VALUEOF_METHOD)).or(new NameMethodMatcher(HBASE_ADDCOLUMN_METHOD));
        return orMethodMatcher;
    }
}
