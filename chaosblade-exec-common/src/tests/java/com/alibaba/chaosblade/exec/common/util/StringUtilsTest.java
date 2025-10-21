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

public class StringUtilsTest {

    @Test
    public void testHasLength() {
        Assert.assertTrue(StringUtils.hasLength("foo"));

        Assert.assertFalse(StringUtils.hasLength(""));
    }

    @Test
    public void testHasText() {
        Assert.assertFalse(StringUtils.hasText(" "));
        Assert.assertFalse(StringUtils.hasText(null));

        Assert.assertTrue(StringUtils.hasText("foo"));
    }

    @Test
    public void testContainsWhitespace() {
        Assert.assertFalse(StringUtils.containsWhitespace(null));
        Assert.assertFalse(StringUtils.containsWhitespace("foo"));

        Assert.assertTrue(StringUtils.containsWhitespace(" "));
    }

    @Test
    public void testTrimWhitespace() {
        Assert.assertNull(StringUtils.trimWhitespace(null));

        Assert.assertEquals("foo", StringUtils.trimWhitespace(" foo "));
    }

    @Test
    public void testTrimLeadingWhitespace() {
        Assert.assertNull(StringUtils.trimLeadingWhitespace(null));

        Assert.assertEquals("foo ",
                StringUtils.trimLeadingWhitespace(" foo "));
    }

    @Test
    public void testTrimLeadingCharacter() {
        Assert.assertNull(StringUtils.trimLeadingCharacter(null, 'a'));

        Assert.assertEquals("oo",
                StringUtils.trimLeadingCharacter("foo", 'f'));
    }

    @Test
    public void testTrimTrailingWhitespace() {
        Assert.assertNull(StringUtils.trimTrailingWhitespace(null));

        Assert.assertEquals(" foo",
                StringUtils.trimTrailingWhitespace(" foo "));
    }

    @Test
    public void testTrimAllWhitespace() {
        Assert.assertNull(StringUtils.trimAllWhitespace(null));

        Assert.assertEquals("foo",
                StringUtils.trimAllWhitespace(" f oo "));
    }

    @Test
    public void testStartsWithIgnoreCase() {
        Assert.assertFalse(StringUtils.startsWithIgnoreCase(null, null));
        Assert.assertFalse(StringUtils.startsWithIgnoreCase("foo", "foobar"));
        Assert.assertFalse(StringUtils.startsWithIgnoreCase("foo", "bar"));

        Assert.assertTrue(StringUtils.startsWithIgnoreCase("foo", "f"));
    }

    @Test
    public void testEndsWithIgnoreCase() {
        Assert.assertFalse(StringUtils.endsWithIgnoreCase(null, null));
        Assert.assertFalse(StringUtils.endsWithIgnoreCase("foo", "foobar"));
        Assert.assertFalse(StringUtils.endsWithIgnoreCase("foo", "bar"));

        Assert.assertTrue(StringUtils.endsWithIgnoreCase("foo", "o"));
    }

    @Test
    public void testCountOccurrencesOf() {
        Assert.assertEquals(0, StringUtils.countOccurrencesOf(null, null));
        Assert.assertEquals(2, StringUtils.countOccurrencesOf("foo", "o"));
    }

    @Test
    public void testReplace() {
        Assert.assertNull(StringUtils.replace(null, null, null));

        Assert.assertEquals("foo",
                StringUtils.replace("foo", null, null));
    }

    @Test
    public void testDelete() {
        Assert.assertEquals("f", StringUtils.delete("foo", "o"));
    }

    @Test
    public void testDeleteAny() {
        Assert.assertNull(StringUtils.deleteAny(null, null));

        Assert.assertEquals("f", StringUtils.deleteAny("foo", "o"));
    }

    @Test
    public void testQuoteIfString() {
        Assert.assertNull(StringUtils.quoteIfString(null));

        Assert.assertEquals("'foo'", StringUtils.quoteIfString("foo"));
    }

    @Test
    public void testUnqualify() {
        Assert.assertEquals("o", StringUtils.unqualify("fo.o"));
    }

    @Test
    public void testCapitalize() {
        Assert.assertNull(StringUtils.capitalize(null));

        Assert.assertEquals("Foo", StringUtils.capitalize("foo"));
    }

    @Test
    public void testUncapitalize() {
        Assert.assertNull(StringUtils.uncapitalize(null));

        Assert.assertEquals("foo", StringUtils.uncapitalize("Foo"));
    }

    @Test
    public void testGetFilename() {
        Assert.assertNull(StringUtils.getFilename(null));

        Assert.assertEquals("foo.txt",
                StringUtils.getFilename("bar/foo.txt"));
    }

    @Test
    public void textGetFilenameExtension() {
        Assert.assertNull(StringUtils.getFilenameExtension(null));

        Assert.assertEquals("txt",
                StringUtils.getFilenameExtension("bar/foo.txt"));
    }

    @Test
    public void testStripFilenameExtension() {
        Assert.assertNull(StringUtils.stripFilenameExtension(null));

        Assert.assertEquals("bar/foo",
                StringUtils.stripFilenameExtension("bar/foo.txt"));
    }

    @Test
    public void testApplyRelativePath() {
        Assert.assertEquals("bar",
                StringUtils.applyRelativePath("foo.txt", "bar"));
        Assert.assertEquals("bar/baz",
                StringUtils.applyRelativePath("bar/foo.txt", "baz"));
    }

    @Test
    public void testIsNotBlank() {
        Assert.assertFalse(StringUtils.isNotBlank(" "));
        Assert.assertFalse(StringUtils.isNotBlank(null));

        Assert.assertTrue(StringUtils.isNotBlank("foo"));
    }

    @Test
    public void testIsNotEmpty() {
        Assert.assertFalse(StringUtils.isNotEmpty(null));

        Assert.assertTrue(StringUtils.isNotEmpty("foo"));
    }

    @Test
    public void testTrimToEmpty() {
        Assert.assertEquals(StringUtils.EMPTY, StringUtils.trimToEmpty(null));
        Assert.assertEquals("foo", StringUtils.trimToEmpty("  foo  "));
    }

    @Test
    public void testTrimToNull() {
        Assert.assertNull(StringUtils.trimToNull(null));

        Assert.assertEquals("foo", StringUtils.trimToNull("  foo  "));
    }

    @Test
    public void testStripComments() {
        Assert.assertNull(StringUtils.stripComments(null, "foo", "bar",
                true, true, true, true));

        Assert.assertEquals("\u0000",
                StringUtils.stripComments("\u0000f/o*\no\r\nbar", "foo", "bar",
                        true, true, true, true));
        Assert.assertEquals("",
                StringUtils.stripComments("/f/oo bar", "foo", "bar",
                        true, true, true, true));
        Assert.assertEquals("-102o a",
                StringUtils.stripComments("--foo bar", "foo", "bar",
                        true, true, true, true));
    }

    @Test
    public void testStartsWithIgnoreCaseAndWs() {
        Assert.assertFalse(StringUtils.startsWithIgnoreCaseAndWs(null, "a"));
        Assert.assertFalse(StringUtils.startsWithIgnoreCaseAndWs("bar", "a"));
    }

    @Test
    public void testIsNumeric() {
        Assert.assertFalse(StringUtils.isNumeric(null));
        Assert.assertFalse(StringUtils.isNumeric("foo"));

        Assert.assertTrue(StringUtils.isNumeric("123"));
    }
}
