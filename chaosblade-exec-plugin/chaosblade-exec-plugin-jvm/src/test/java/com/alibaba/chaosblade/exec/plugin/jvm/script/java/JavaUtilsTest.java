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

package com.alibaba.chaosblade.exec.plugin.jvm.script.java;

import org.junit.Assert;
import org.junit.Test;

public class JavaUtilsTest {

    @Test
    public void testGetClassName() {
        Assert.assertEquals("", JavaUtils.getClassName(""));
        Assert.assertEquals("", JavaUtils.getClassName("String"));
        Assert.assertEquals("", JavaUtils.getClassName("classString"));
        Assert.assertEquals("", JavaUtils.getClassName("class.String"));
        Assert.assertEquals("String", JavaUtils.getClassName("classString%"));
    }

    @Test
    public void testIsAsciiAlpha() {
        Assert.assertTrue(JavaUtils.isAsciiAlpha('B'));
        Assert.assertTrue(JavaUtils.isAsciiAlpha('b'));

        Assert.assertFalse(JavaUtils.isAsciiAlpha('%'));
    }
}
