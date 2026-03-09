/*
 * Copyright 2025 The ChaosBlade Authors
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

package com.alibaba.chaosblade.exec.common.util;

import org.junit.Assert;
import org.junit.Test;

public class BusinessParamUtilTest {

    @Test
    public void testHasMultiLevelKey_withDot_defaultNotFlat() {
        BusinessParamUtil.BusinessParam param = new BusinessParamUtil.BusinessParam();
        param.setKey("user.id");
        Assert.assertTrue(param.hasMultiLevelKey());
        Assert.assertEquals("user", param.getFirstLevelKey());
        Assert.assertEquals("id", param.getJsonPath());
    }

    @Test
    public void testHasMultiLevelKey_withDot_flatTrue() {
        BusinessParamUtil.BusinessParam param = new BusinessParamUtil.BusinessParam();
        param.setKey("user.id");
        param.setFlat(true);
        Assert.assertFalse(param.hasMultiLevelKey());
        Assert.assertEquals("user.id", param.getFirstLevelKey());
    }

    @Test
    public void testHasMultiLevelKey_noDot() {
        BusinessParamUtil.BusinessParam param = new BusinessParamUtil.BusinessParam();
        param.setKey("userId");
        Assert.assertFalse(param.hasMultiLevelKey());
        Assert.assertEquals("userId", param.getFirstLevelKey());
    }

    @Test
    public void testParseFromJsonStr_withFlat() {
        String json = "{\"pattern\":\"or\",\"params\":[{\"key\":\"user.id\",\"value\":\"123\",\"mode\":\"rpc\",\"flat\":true}]}";
        BusinessParamUtil.BusinessParamWrapper wrapper = BusinessParamUtil.parseFromJsonStr(json);
        Assert.assertNotNull(wrapper);
        Assert.assertEquals("or", wrapper.getPattern());
        Assert.assertEquals(1, wrapper.getParams().size());

        BusinessParamUtil.BusinessParam param = wrapper.getParams().get(0);
        Assert.assertEquals("user.id", param.getKey());
        Assert.assertEquals("123", param.getValue());
        Assert.assertEquals("rpc", param.getMode());
        Assert.assertTrue(param.isFlat());
        Assert.assertFalse(param.hasMultiLevelKey());
    }

    @Test
    public void testParseFromJsonStr_withoutFlat() {
        String json = "{\"pattern\":\"or\",\"params\":[{\"key\":\"user.id\",\"value\":\"123\",\"mode\":\"rpc\"}]}";
        BusinessParamUtil.BusinessParamWrapper wrapper = BusinessParamUtil.parseFromJsonStr(json);
        Assert.assertNotNull(wrapper);

        BusinessParamUtil.BusinessParam param = wrapper.getParams().get(0);
        Assert.assertEquals("user.id", param.getKey());
        Assert.assertFalse(param.isFlat());
        Assert.assertTrue(param.hasMultiLevelKey());
    }

    @Test
    public void testParseFromJsonStr_emptyString() {
        BusinessParamUtil.BusinessParamWrapper wrapper = BusinessParamUtil.parseFromJsonStr("");
        Assert.assertNotNull(wrapper);
        Assert.assertNull(wrapper.getParams());
    }

    @Test
    public void testParseFromJsonStr_invalidJson() {
        BusinessParamUtil.BusinessParamWrapper wrapper = BusinessParamUtil.parseFromJsonStr("not-json");
        Assert.assertNotNull(wrapper);
        Assert.assertNull(wrapper.getParams());
    }
}
