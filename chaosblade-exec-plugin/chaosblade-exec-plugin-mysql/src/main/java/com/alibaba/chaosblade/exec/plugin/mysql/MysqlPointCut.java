/*
 * Copyright 1999-2019 Alibaba Group Holding Ltd.
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

package com.alibaba.chaosblade.exec.plugin.mysql;

import com.alibaba.chaosblade.exec.common.aop.PointCut;
import com.alibaba.chaosblade.exec.common.aop.matcher.clazz.ClassMatcher;
import com.alibaba.chaosblade.exec.common.aop.matcher.clazz.NameClassMatcher;
import com.alibaba.chaosblade.exec.common.aop.matcher.clazz.OrClassMatcher;
import com.alibaba.chaosblade.exec.common.aop.matcher.method.MethodMatcher;
import com.alibaba.chaosblade.exec.common.aop.matcher.method.NameMethodMatcher;
import com.alibaba.chaosblade.exec.common.aop.matcher.method.OrMethodMatcher;

import static com.alibaba.chaosblade.exec.plugin.mysql.MysqlConstant.*;

/**
 * @author Changjun Xiao
 */
public class MysqlPointCut implements PointCut {

    @Override
    public ClassMatcher getClassMatcher() {
        OrClassMatcher orClassMatcher = new OrClassMatcher();

        return orClassMatcher
                .or(new NameClassMatcher(MYSQL_IO_CLASS))
                .or(new NameClassMatcher(MYSQL8_NATIVE_SESSION_CLASS));
    }

    @Override
    public MethodMatcher getMethodMatcher() {
        OrMethodMatcher orMethodMatcher = new OrMethodMatcher();
        return orMethodMatcher
                .or(new NameMethodMatcher(INTERCEPTOR_PRE_METHOD))
                .or(new NameMethodMatcher(MYSQL8_NATIVE_SESSION_METHOD));
    }
}
