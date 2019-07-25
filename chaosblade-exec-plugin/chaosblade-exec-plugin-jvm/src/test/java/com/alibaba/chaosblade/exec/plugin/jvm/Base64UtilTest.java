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

package com.alibaba.chaosblade.exec.plugin.jvm;

import org.junit.Assert;
import org.junit.Test;

public class Base64UtilTest {

    @Test
    public void testEncode() {
        Assert.assertEquals("", Base64Util.encode(new byte[0], true));
        Assert.assertEquals("", Base64Util.encode(new byte[0], false));
        Assert.assertEquals("AQID",
                Base64Util.encode(new byte[]{1, 2, 3}, true));
        Assert.assertEquals("AQID",
                Base64Util.encode(new byte[]{1, 2, 3}, false));
    }

    @Test
    public void testDecode() {
        Assert.assertEquals("", Base64Util.decode(new byte[0]));
        Assert.assertEquals("", Base64Util.decode(new byte[]{0, 10}));
        Assert.assertEquals("", Base64Util.decode(new byte[]{1, 2, 13, 10}));
    }
}
