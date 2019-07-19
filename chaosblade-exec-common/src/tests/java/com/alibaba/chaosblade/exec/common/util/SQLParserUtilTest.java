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

package com.alibaba.chaosblade.exec.common.util;

import org.junit.Assert;
import org.junit.Test;

import java.sql.SQLException;

public class SQLParserUtilTest {

    @Test
    public void testGetSqlType() throws SQLException {
        Assert.assertNull(SQLParserUtil.getSqlType(""));
        Assert.assertNull(SQLParserUtil.getSqlType("/*foo"));

        Assert.assertEquals(0, SQLParserUtil.getSqlType("select").value());
        Assert.assertEquals(1, SQLParserUtil.getSqlType("insert").value());
        Assert.assertEquals(2, SQLParserUtil.getSqlType("update").value());
        Assert.assertEquals(3, SQLParserUtil.getSqlType("delete").value());
        Assert.assertEquals(5, SQLParserUtil.getSqlType("replace").value());
        Assert.assertEquals(6, SQLParserUtil.getSqlType("truncate").value());
        Assert.assertEquals(7, SQLParserUtil.getSqlType("create").value());
        Assert.assertEquals(8, SQLParserUtil.getSqlType("drop").value());
        Assert.assertEquals(9, SQLParserUtil.getSqlType("load").value());
        Assert.assertEquals(10, SQLParserUtil.getSqlType("merge").value());
        Assert.assertEquals(11, SQLParserUtil.getSqlType("show").value());
    }

    @Test
    public void testFindTableName() {
        Assert.assertNull(SQLParserUtil.findTableName(null));
        Assert.assertNull(SQLParserUtil.findTableName("foo"));
        Assert.assertNull(SQLParserUtil.findTableName("update %"));
        Assert.assertNull(SQLParserUtil.findTableName("delete %"));
        Assert.assertNull(SQLParserUtil.findTableName("insert %"));
        Assert.assertNull(SQLParserUtil.findTableName("replace %"));
        Assert.assertNull(SQLParserUtil.findTableName("foobarbaz"));
        Assert.assertNull(SQLParserUtil.findTableName("select )from"));

        Assert.assertEquals("foo", SQLParserUtil.findTableName("update foo"));
        Assert.assertEquals("foo", SQLParserUtil.findTableName("delete foo"));
        Assert.assertEquals("foo",
                SQLParserUtil.findTableName("delete from foo"));
        Assert.assertEquals("foo",
                SQLParserUtil.findTableName("insert into foo"));
        Assert.assertEquals("foo",
                SQLParserUtil.findTableName("replace into foo"));
        Assert.assertEquals("foo",
                SQLParserUtil.findTableName("select from foo"));
        Assert.assertEquals("foo",
                SQLParserUtil.findTableName("select from foo"));
        Assert.assertEquals("bar",
                SQLParserUtil.findTableName("select from (foo,bar,baz) where "));
        Assert.assertEquals("bar",
                SQLParserUtil.findTableName("select foo)from bar"));
    }
}
