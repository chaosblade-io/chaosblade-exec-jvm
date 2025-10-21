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

import java.io.IOException;
import java.io.StringReader;

public abstract class StringUtils {

  public static final String EMPTY = "";

  private static final String FOLDER_SEPARATOR = "/";

  private static final char EXTENSION_SEPARATOR = '.';

  private static final int INDEX_NOT_FOUND = -1;

  // ---------------------------------------------------------------------
  // General convenience methods for working with Strings
  // ---------------------------------------------------------------------

  /**
   * Check that the given String is neither <code>null</code> nor of length 0. Note: Will return
   * <code>true</code> for a String that purely consists of whitespace.
   *
   * <p>
   *
   * <pre>
   * StringUtils.hasLength(null) = false
   * StringUtils.hasLength("") = false
   * StringUtils.hasLength(" ") = true
   * StringUtils.hasLength("Hello") = true
   * </pre>
   *
   * @param str the String to check (may be <code>null</code>)
   * @return <code>true</code> if the String is not null and has length
   * @see #hasText(String)
   */
  public static boolean hasLength(String str) {
    return (str != null && str.length() > 0);
  }

  /**
   * Check whether the given String has actual text. More specifically, returns <code>true</code> if
   * the string not <code>null</code>, its length is greater than 0, and it contains at least one
   * non-whitespace character.
   *
   * <p>
   *
   * <pre>
   * StringUtils.hasText(null) = false
   * StringUtils.hasText("") = false
   * StringUtils.hasText(" ") = false
   * StringUtils.hasText("12345") = true
   * StringUtils.hasText(" 12345 ") = true
   * </pre>
   *
   * @param str the String to check (may be <code>null</code>)
   * @return <code>true</code> if the String is not <code>null</code>, its length is greater than 0,
   *     and it does not contain whitespace only
   * @see Character#isWhitespace
   */
  public static boolean hasText(String str) {
    if (!hasLength(str)) {
      return false;
    }
    int strLen = str.length();
    for (int i = 0; i < strLen; i++) {
      if (!Character.isWhitespace(str.charAt(i))) {
        return true;
      }
    }
    return false;
  }

  /**
   * Check whether the given String contains any whitespace characters.
   *
   * @param str the String to check (may be <code>null</code>)
   * @return <code>true</code> if the String is not empty and contains at least 1 whitespace
   *     character
   * @see Character#isWhitespace
   */
  public static boolean containsWhitespace(String str) {
    if (!hasLength(str)) {
      return false;
    }
    int strLen = str.length();
    for (int i = 0; i < strLen; i++) {
      if (Character.isWhitespace(str.charAt(i))) {
        return true;
      }
    }
    return false;
  }

  /**
   * Trim leading and trailing whitespace from the given String.
   *
   * @param str the String to check
   * @return the trimmed String
   * @see Character#isWhitespace
   */
  public static String trimWhitespace(String str) {
    if (!hasLength(str)) {
      return str;
    }
    StringBuffer buf = new StringBuffer(str);
    while (buf.length() > 0 && Character.isWhitespace(buf.charAt(0))) {
      buf.deleteCharAt(0);
    }
    while (buf.length() > 0 && Character.isWhitespace(buf.charAt(buf.length() - 1))) {
      buf.deleteCharAt(buf.length() - 1);
    }
    return buf.toString();
  }

  /**
   * Trim leading whitespace from the given String.
   *
   * @param str the String to check
   * @return the trimmed String
   * @see Character#isWhitespace
   */
  public static String trimLeadingWhitespace(String str) {
    if (!hasLength(str)) {
      return str;
    }
    StringBuffer buf = new StringBuffer(str);
    while (buf.length() > 0 && Character.isWhitespace(buf.charAt(0))) {
      buf.deleteCharAt(0);
    }
    return buf.toString();
  }

  /**
   * Trim all occurences of the supplied leading character from the given String.
   *
   * @param str the String to check
   * @param leadingCharacter the leading character to be trimmed
   * @return the trimmed String
   */
  public static String trimLeadingCharacter(String str, char leadingCharacter) {
    if (!hasLength(str)) {
      return str;
    }
    StringBuffer buf = new StringBuffer(str);
    while (buf.length() > 0 && buf.charAt(0) == leadingCharacter) {
      buf.deleteCharAt(0);
    }
    return buf.toString();
  }

  /**
   * Trim trailing whitespace from the given String.
   *
   * @param str the String to check
   * @return the trimmed String
   * @see Character#isWhitespace
   */
  public static String trimTrailingWhitespace(String str) {
    if (!hasLength(str)) {
      return str;
    }
    StringBuffer buf = new StringBuffer(str);
    while (buf.length() > 0 && Character.isWhitespace(buf.charAt(buf.length() - 1))) {
      buf.deleteCharAt(buf.length() - 1);
    }
    return buf.toString();
  }

  /**
   * Trim <i>all</i> whitespace from the given String: leading, trailing, and inbetween characters.
   *
   * @param str the String to check
   * @return the trimmed String
   * @see Character#isWhitespace
   */
  public static String trimAllWhitespace(String str) {
    if (!hasLength(str)) {
      return str;
    }
    StringBuffer buf = new StringBuffer(str);
    int index = 0;
    while (buf.length() > index) {
      if (Character.isWhitespace(buf.charAt(index))) {
        buf.deleteCharAt(index);
      } else {
        index++;
      }
    }
    return buf.toString();
  }

  /**
   * Test if the given String starts with the specified prefix, ignoring upper/lower case.
   *
   * @param str the String to check
   * @param prefix the prefix to look for
   * @see String#startsWith
   */
  public static boolean startsWithIgnoreCase(String str, String prefix) {
    if (str == null || prefix == null) {
      return false;
    }
    if (str.startsWith(prefix)) {
      return true;
    }
    if (str.length() < prefix.length()) {
      return false;
    }
    String lcStr = str.substring(0, prefix.length()).toLowerCase();
    String lcPrefix = prefix.toLowerCase();
    return lcStr.equals(lcPrefix);
  }

  /**
   * Test if the given String ends with the specified suffix, ignoring upper/lower case.
   *
   * @param str the String to check
   * @param suffix the suffix to look for
   * @see String#endsWith
   */
  public static boolean endsWithIgnoreCase(String str, String suffix) {
    if (str == null || suffix == null) {
      return false;
    }
    if (str.endsWith(suffix)) {
      return true;
    }
    if (str.length() < suffix.length()) {
      return false;
    }

    String lcStr = str.substring(str.length() - suffix.length()).toLowerCase();
    String lcSuffix = suffix.toLowerCase();
    return lcStr.equals(lcSuffix);
  }

  /**
   * Count the occurrences of the substring in string s.
   *
   * @param str string to search in. Return 0 if this is null.
   * @param sub string to search for. Return 0 if this is null.
   */
  public static int countOccurrencesOf(String str, String sub) {
    if (str == null || sub == null || str.length() == 0 || sub.length() == 0) {
      return 0;
    }
    int count = 0, pos = 0, idx = 0;
    while ((idx = str.indexOf(sub, pos)) != -1) {
      ++count;
      pos = idx + sub.length();
    }
    return count;
  }

  /**
   * Replace all occurences of a substring within a string with another string.
   *
   * @param inString String to examine
   * @param oldPattern String to replace
   * @param newPattern String to insert
   * @return a String with the replacements
   */
  public static String replace(String inString, String oldPattern, String newPattern) {
    if (inString == null) {
      return null;
    }
    if (oldPattern == null || newPattern == null) {
      return inString;
    }

    StringBuffer sbuf = new StringBuffer();
    // output StringBuffer we'll build up
    int pos = 0; // our position in the old string
    int index = inString.indexOf(oldPattern);
    // the index of an occurrence we've found, or -1
    int patLen = oldPattern.length();
    while (index >= 0) {
      sbuf.append(inString.substring(pos, index));
      sbuf.append(newPattern);
      pos = index + patLen;
      index = inString.indexOf(oldPattern, pos);
    }
    sbuf.append(inString.substring(pos));

    // remember to append any characters to the right of a match
    return sbuf.toString();
  }

  /**
   * Delete all occurrences of the given substring.
   *
   * @param pattern the pattern to delete all occurrences of
   */
  public static String delete(String inString, String pattern) {
    return replace(inString, pattern, "");
  }

  /**
   * Delete any character in a given string.
   *
   * @param charsToDelete a set of characters to delete. E.g. "az\n" will delete 'a's, 'z's and new
   *     lines.
   */
  public static String deleteAny(String inString, String charsToDelete) {
    if (!hasLength(inString) || !hasLength(charsToDelete)) {
      return inString;
    }
    StringBuffer out = new StringBuffer();
    for (int i = 0; i < inString.length(); i++) {
      char c = inString.charAt(i);
      if (charsToDelete.indexOf(c) == -1) {
        out.append(c);
      }
    }
    return out.toString();
  }

  // ---------------------------------------------------------------------
  // Convenience methods for working with formatted Strings
  // ---------------------------------------------------------------------

  /**
   * Quote the given String with single quotes.
   * @param str the input String (e.g. "myString")
   * @return the quoted String (e.g. "'myString'"),
   * or <code>null<code> if the input was <code>null</code>
   */
  public static String quote(String str) {
    return (str != null ? "'" + str + "'" : null);
  }

  /**
   * Turn the given Object into a String with single quotes if it is a String; keeping the Object
   * as-is else.
   *
   * @param obj the input Object (e.g. "myString")
   * @return the quoted String (e.g. "'myString'"), or the input object as-is if not a String
   */
  public static Object quoteIfString(Object obj) {
    return (obj instanceof String ? quote((String) obj) : obj);
  }

  /**
   * Unqualify a string qualified by a '.' dot character. For example, "this.name.is.qualified",
   * returns "qualified".
   *
   * @param qualifiedName the qualified name
   */
  public static String unqualify(String qualifiedName) {
    return unqualify(qualifiedName, '.');
  }

  /**
   * Unqualify a string qualified by a separator character. For example, "this:name:is:qualified"
   * returns "qualified" if using a ':' separator.
   *
   * @param qualifiedName the qualified name
   * @param separator the separator
   */
  public static String unqualify(String qualifiedName, char separator) {
    return qualifiedName.substring(qualifiedName.lastIndexOf(separator) + 1);
  }

  /**
   * Capitalize a <code>String</code>, changing the first letter to upper case as per {@link
   * Character#toUpperCase(char)}. No other letters are changed.
   *
   * @param str the String to capitalize, may be <code>null</code>
   * @return the capitalized String, <code>null</code> if null
   */
  public static String capitalize(String str) {
    return changeFirstCharacterCase(str, true);
  }

  /**
   * Uncapitalize a <code>String</code>, changing the first letter to lower case as per {@link
   * Character#toLowerCase(char)}. No other letters are changed.
   *
   * @param str the String to uncapitalize, may be <code>null</code>
   * @return the uncapitalized String, <code>null</code> if null
   */
  public static String uncapitalize(String str) {
    return changeFirstCharacterCase(str, false);
  }

  private static String changeFirstCharacterCase(String str, boolean capitalize) {
    if (str == null || str.length() == 0) {
      return str;
    }
    StringBuffer buf = new StringBuffer(str.length());
    if (capitalize) {
      buf.append(Character.toUpperCase(str.charAt(0)));
    } else {
      buf.append(Character.toLowerCase(str.charAt(0)));
    }
    buf.append(str.substring(1));
    return buf.toString();
  }

  /**
   * Extract the filename from the given path, e.g. "mypath/myfile.txt" -> "myfile.txt".
   *
   * @param path the file path (may be <code>null</code>)
   * @return the extracted filename, or <code>null</code> if none
   */
  public static String getFilename(String path) {
    if (path == null) {
      return null;
    }
    int separatorIndex = path.lastIndexOf(FOLDER_SEPARATOR);
    return (separatorIndex != -1 ? path.substring(separatorIndex + 1) : path);
  }

  /**
   * Extract the filename extension from the given path, e.g. "mypath/myfile.txt" -> "txt".
   *
   * @param path the file path (may be <code>null</code>)
   * @return the extracted filename extension, or <code>null</code> if none
   */
  public static String getFilenameExtension(String path) {
    if (path == null) {
      return null;
    }
    int sepIndex = path.lastIndexOf(EXTENSION_SEPARATOR);
    return (sepIndex != -1 ? path.substring(sepIndex + 1) : null);
  }

  /**
   * Strip the filename extension from the given path, e.g. "mypath/myfile.txt" -> "mypath/myfile".
   *
   * @param path the file path (may be <code>null</code>)
   * @return the path with stripped filename extension, or <code>null</code> if none
   */
  public static String stripFilenameExtension(String path) {
    if (path == null) {
      return null;
    }
    int sepIndex = path.lastIndexOf(EXTENSION_SEPARATOR);
    return (sepIndex != -1 ? path.substring(0, sepIndex) : path);
  }

  /**
   * Apply the given relative path to the given path, assuming standard Java folder separation (i.e.
   * "/" separators);
   *
   * @param path the path to start from (usually a full file path)
   * @param relativePath the relative path to apply (relative to the full file path above)
   * @return the full file path that results from applying the relative path
   */
  public static String applyRelativePath(String path, String relativePath) {
    int separatorIndex = path.lastIndexOf(FOLDER_SEPARATOR);
    if (separatorIndex != -1) {
      String newPath = path.substring(0, separatorIndex);
      if (!relativePath.startsWith(FOLDER_SEPARATOR)) {
        newPath += FOLDER_SEPARATOR;
      }
      return newPath + relativePath;
    } else {
      return relativePath;
    }
  }

  /**
   * Checks if a String is whitespace, empty ("") or null.
   *
   * <pre>
   * StringUtils.isBlank(null)      = true
   * StringUtils.isBlank("")        = true
   * StringUtils.isBlank(" ")       = true
   * StringUtils.isBlank("bob")     = false
   * StringUtils.isBlank("  bob  ") = false
   * </pre>
   *
   * @param str the String to check, may be null
   * @return <code>true</code> if the String is null, empty or whitespace
   */
  public static boolean isBlank(String str) {
    int strLen;
    if (str == null || (strLen = str.length()) == 0) {
      return true;
    }
    for (int i = 0; i < strLen; i++) {
      if ((Character.isWhitespace(str.charAt(i)) == false)) {
        return false;
      }
    }
    return true;
  }

  /**
   * Checks if a String is not empty (""), not null and not whitespace only.
   *
   * <pre>
   * StringUtils.isNotBlank(null)      = false
   * StringUtils.isNotBlank("")        = false
   * StringUtils.isNotBlank(" ")       = false
   * StringUtils.isNotBlank("bob")     = true
   * StringUtils.isNotBlank("  bob  ") = true
   * </pre>
   *
   * @param str the String to check, may be null
   * @return <code>true</code> if the String is not empty and not null and not whitespace
   */
  public static boolean isNotBlank(String str) {
    return !isBlank(str);
  }

  // Empty checks
  // -----------------------------------------------------------------------
  /**
   * Checks if a String is empty ("") or null.
   *
   * <pre>
   * StringUtils.isEmpty(null)      = true
   * StringUtils.isEmpty("")        = true
   * StringUtils.isEmpty(" ")       = false
   * StringUtils.isEmpty("bob")     = false
   * StringUtils.isEmpty("  bob  ") = false
   * </pre>
   *
   * <p>NOTE: This method changed in Lang version 2.0. It no longer trims the String. That
   * functionality is available in isBlank().
   *
   * @param str the String to check, may be null
   * @return <code>true</code> if the String is empty or null
   */
  public static boolean isEmpty(String str) {
    return str == null || str.length() == 0;
  }

  /**
   * Checks if a String is not empty ("") and not null.
   *
   * <pre>
   * StringUtils.isNotEmpty(null)      = false
   * StringUtils.isNotEmpty("")        = false
   * StringUtils.isNotEmpty(" ")       = true
   * StringUtils.isNotEmpty("bob")     = true
   * StringUtils.isNotEmpty("  bob  ") = true
   * </pre>
   *
   * @param str the String to check, may be null
   * @return <code>true</code> if the String is not empty and not null
   */
  public static boolean isNotEmpty(String str) {
    return !isEmpty(str);
  }

  /**
   * Removes control characters (char &lt;= 32) from both ends of this String returning an empty
   * String ("") if the String is empty ("") after the trim or if it is <code>null</code>.
   *
   * <p>The String is trimmed using {@link String#trim()}. Trim removes start and end characters
   * &lt;= 32.
   *
   * <pre>
   * StringUtils.trimToEmpty(null)          = ""
   * StringUtils.trimToEmpty("")            = ""
   * StringUtils.trimToEmpty("     ")       = ""
   * StringUtils.trimToEmpty("abc")         = "abc"
   * StringUtils.trimToEmpty("    abc    ") = "abc"
   * </pre>
   *
   * @param str the String to be trimmed, may be null
   * @return the trimmed String, or an empty String if <code>null</code> input
   */
  public static String trimToEmpty(String str) {
    return str == null ? EMPTY : str.trim();
  }

  /**
   * Removes control characters (char &lt;= 32) from both ends of this String returning <code>null
   * </code> if the String is empty ("") after the trim or if it is <code>null</code>.
   *
   * <p>The String is trimmed using {@link String#trim()}. Trim removes start and end characters
   * &lt;= 32.
   *
   * <pre>
   * StringUtils.trimToNull(null)          = null
   * StringUtils.trimToNull("")            = null
   * StringUtils.trimToNull("     ")       = null
   * StringUtils.trimToNull("abc")         = "abc"
   * StringUtils.trimToNull("    abc    ") = "abc"
   * </pre>
   *
   * @param str the String to be trimmed, may be null
   * @return the trimmed String, <code>null</code> if only chars &lt;= 32, empty or null String
   *     input
   */
  public static String trimToNull(String str) {
    String ts = trim(str);
    return isEmpty(ts) ? null : ts;
  }

  /**
   * Removes control characters (char &lt;= 32) from both ends of this String, handling <code>null
   * </code> by returning <code>null</code>.
   *
   * <p>The String is trimmed using {@link String#trim()}. Trim removes start and end characters
   * &lt;= 32.
   *
   * <pre>
   * StringUtils.trim(null)          = null
   * StringUtils.trim("")            = ""
   * StringUtils.trim("     ")       = ""
   * StringUtils.trim("abc")         = "abc"
   * StringUtils.trim("    abc    ") = "abc"
   * </pre>
   *
   * @param str the String to be trimmed, may be null
   * @return the trimmed string, <code>null</code> if null String input
   */
  public static String trim(String str) {
    return str == null ? null : str.trim();
  }

  /**
   * Returns the given string, with comments removed
   *
   * @param src the source string
   * @param stringOpens characters which delimit the "open" of a string
   * @param stringCloses characters which delimit the "close" of a string, in counterpart order to
   *     <code>stringOpens</code>
   * @param slashStarComments strip slash-star type "C" style comments
   * @param slashSlashComments strip slash-slash C++ style comments to end-of-line
   * @param hashComments strip #-style comments to end-of-line
   * @param dashDashComments strip "--" style comments to end-of-line
   * @return the input string with all comment-delimited data removed
   */
  public static String stripComments(
      String src,
      String stringOpens,
      String stringCloses,
      boolean slashStarComments,
      boolean slashSlashComments,
      boolean hashComments,
      boolean dashDashComments) {
    if (src == null) {
      return null;
    }

    StringBuffer buf = new StringBuffer(src.length());

    // It's just more natural to deal with this as a stream
    // when parsing..This code is currently only called when
    // parsing the kind of metadata that developers are strongly
    // recommended to cache anyways, so we're not worried
    // about the _1_ extra object allocation if it cleans
    // up the code

    StringReader sourceReader = new StringReader(src);

    int contextMarker = Character.MIN_VALUE;
    boolean escaped = false;
    int markerTypeFound = -1;

    int ind = 0;

    int currentChar = 0;

    try {
      while ((currentChar = sourceReader.read()) != -1) {

        if (false && currentChar == '\\') {
          escaped = !escaped;
        } else if (markerTypeFound != -1
            && currentChar == stringCloses.charAt(markerTypeFound)
            && !escaped) {
          contextMarker = Character.MIN_VALUE;
          markerTypeFound = -1;
        } else if ((ind = stringOpens.indexOf(currentChar)) != -1
            && !escaped
            && contextMarker == Character.MIN_VALUE) {
          markerTypeFound = ind;
          contextMarker = currentChar;
        }

        if (contextMarker == Character.MIN_VALUE
            && currentChar == '/'
            && (slashSlashComments || slashStarComments)) {
          currentChar = sourceReader.read();
          if (currentChar == '*' && slashStarComments) {
            int prevChar = 0;
            while ((currentChar = sourceReader.read()) != '/' || prevChar != '*') {
              if (currentChar == '\r') {

                currentChar = sourceReader.read();
                if (currentChar == '\n') {
                  currentChar = sourceReader.read();
                }
              } else {
                if (currentChar == '\n') {

                  currentChar = sourceReader.read();
                }
              }
              if (currentChar < 0) {
                break;
              }
              prevChar = currentChar;
            }
            continue;
          } else if (currentChar == '/' && slashSlashComments) {
            while ((currentChar = sourceReader.read()) != '\n'
                && currentChar != '\r'
                && currentChar >= 0) {;
            }
          }
        } else if (contextMarker == Character.MIN_VALUE && currentChar == '#' && hashComments) {
          // Slurp up everything until the newline
          while ((currentChar = sourceReader.read()) != '\n'
              && currentChar != '\r'
              && currentChar >= 0) {;
          }
        } else if (contextMarker == Character.MIN_VALUE && currentChar == '-' && dashDashComments) {
          currentChar = sourceReader.read();

          if (currentChar == -1 || currentChar != '-') {
            buf.append('-');

            if (currentChar != -1) {
              buf.append(currentChar);
            }

            continue;
          }

          // Slurp up everything until the newline

          while ((currentChar = sourceReader.read()) != '\n'
              && currentChar != '\r'
              && currentChar >= 0) ;
        }

        if (currentChar != -1) {
          buf.append((char) currentChar);
        }
      }
    } catch (IOException ioEx) {
      // we'll never see this from a StringReader
    }

    return buf.toString();
  }

  /**
   * Determines whether or not the sting 'searchIn' contains the string 'searchFor', disregarding
   * case and leading whitespace
   *
   * @param searchIn the string to search in
   * @param searchFor the string to search for
   * @return true if the string starts with 'searchFor' ignoring whitespace
   */
  public static boolean startsWithIgnoreCaseAndWs(String searchIn, String searchFor) {
    return startsWithIgnoreCaseAndWs(searchIn, searchFor, 0);
  }

  /**
   * Determines whether or not the sting 'searchIn' contains the string 'searchFor', disregarding
   * case and leading whitespace
   *
   * @param searchIn the string to search in
   * @param searchFor the string to search for
   * @param beginPos where to start searching
   * @return true if the string starts with 'searchFor' ignoring whitespace
   */
  public static boolean startsWithIgnoreCaseAndWs(String searchIn, String searchFor, int beginPos) {
    if (searchIn == null) {
      return searchFor == null;
    }

    int inLength = searchIn.length();

    for (; beginPos < inLength; beginPos++) {
      if (!Character.isWhitespace(searchIn.charAt(beginPos))) {
        break;
      }
    }

    return startsWithIgnoreCase(searchIn, beginPos, searchFor);
  }

  /**
   * Determines whether or not the string 'searchIn' contains the string 'searchFor', dis-regarding
   * case starting at 'startAt' Shorthand for a String.regionMatch(...)
   *
   * @param searchIn the string to search in
   * @param startAt the position to start at
   * @param searchFor the string to search for
   * @return whether searchIn starts with searchFor, ignoring case
   */
  public static boolean startsWithIgnoreCase(String searchIn, int startAt, String searchFor) {
    return searchIn.regionMatches(true, startAt, searchFor, 0, searchFor.length());
  }

  public static boolean isNumeric(String str) {
    if (str == null) {
      return false;
    }
    int sz = str.length();
    for (int i = 0; i < sz; i++) {
      if (Character.isDigit(str.charAt(i)) == false) {
        return false;
      }
    }
    return true;
  }

  public static int indexOf(String str, char searchChar) {
    if (isEmpty(str)) {
      return INDEX_NOT_FOUND;
    }
    return str.indexOf(searchChar);
  }

  public static String substring(String str, int start, int end) {
    if (str == null) {
      return null;
    }

    // handle negatives
    if (end < 0) {
      // remember end is negative
      end = str.length() + end;
    }
    // remember start is negative
    if (start < 0) {
      start = str.length() + start;
    }

    // check length next
    if (end > str.length()) {
      end = str.length();
    }

    // if start is greater than end, return ""
    if (start > end) {
      return EMPTY;
    }

    if (start < 0) {
      start = 0;
    }
    if (end < 0) {
      end = 0;
    }

    return str.substring(start, end);
  }

  public static String substring(String str, int start) {
    if (str == null) {
      return null;
    }

    // handle negatives, which means last n characters
    if (start < 0) {
      // remember start is negative
      start = str.length() + start;
    }

    if (start < 0) {
      start = 0;
    }
    if (start > str.length()) {
      return EMPTY;
    }

    return str.substring(start);
  }
}
