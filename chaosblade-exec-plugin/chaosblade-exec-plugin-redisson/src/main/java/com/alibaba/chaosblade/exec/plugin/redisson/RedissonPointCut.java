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


package com.alibaba.chaosblade.exec.plugin.redisson;

import com.alibaba.chaosblade.exec.common.aop.PointCut;
import com.alibaba.chaosblade.exec.common.aop.matcher.clazz.ClassMatcher;
import com.alibaba.chaosblade.exec.common.aop.matcher.clazz.NameClassMatcher;
import com.alibaba.chaosblade.exec.common.aop.matcher.method.MethodMatcher;
import com.alibaba.chaosblade.exec.common.aop.matcher.method.NameMethodMatcher;

/**
 * @author xueshaoyi
 * @Date 2020/11/23 上午11:36
 **/
public class RedissonPointCut implements PointCut {
	private static final String REDISSON_CONNECTION = "org.redisson.command.RedisExecutor";

	private static final String INTERCEPTOR_PRE_METHOD = "execute";

	@Override
	public ClassMatcher getClassMatcher() {
		return new NameClassMatcher(REDISSON_CONNECTION);
	}

	@Override
	public MethodMatcher getMethodMatcher() {
		return new NameMethodMatcher(INTERCEPTOR_PRE_METHOD);
	}
}
