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

import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SQLParserUtil {

  private static final Pattern SELECT_FOR_UPDATE_PATTERN =
      Pattern.compile("^select\\s+.*\\s+for\\s+update.*$", Pattern.CASE_INSENSITIVE);
  private static Pattern ptable = Pattern.compile("\\s+`?([a-z0-9_@\\.\"$]+)`?\\s+");
  private static Pattern pinsert_into =
      Pattern.compile("\\s+into\\s+`?([a-z0-9_@\\.\"$]+)`?[\\s(]+");
  private static Pattern pdelete_from = Pattern.compile("\\s+from\\s+`?([a-z0-9_@\\.\"$]+)`?\\s+");
  private static Pattern pselect_from =
      Pattern.compile("\\s+from\\s+`?([a-z0-9_@\\.\"$]+)`?[\\s)]+");
  private static Pattern preplace_from =
      Pattern.compile("\\s+into\\s+`?([a-z0-9_@\\.\"$]+)`?[\\s(]+");
  private static Pattern pfrom_where = Pattern.compile("\\s+from\\s+(.*)\\s+where\\s+"); // .*默认最大匹配
  private static String hintregx = "/\\*.*?\\*/"; // hint正则式，懒惰匹配(最短匹配)

  /**
   * 获得SQL语句种类
   *
   * @param sql SQL语句
   * @throws SQLException 当SQL语句不是SELECT、INSERT、UPDATE、DELETE语句时，抛出异常。
   */
  public static SqlType getSqlType(String sql) throws SQLException {
    // #bug 2011-11-24,modify by junyu,先不走缓存，否则sql变化巨大，缓存换入换出太多，gc太明显
    // SqlType sqlType = globalCache.getSqlType(sql);
    // if (sqlType == null) {
    SqlType sqlType = null;
    // #bug 2011-12-8,modify by junyu ,this code use huge cpu resource,and
    // most
    // sql have no comment,so first simple look for there whether have the
    // comment
    String noCommentsSql = sql;
    if (sql.contains("/*")) {
      noCommentsSql = StringUtils.stripComments(sql, "'\"", "'\"", true, false, true, true).trim();
    }

    if (StringUtils.startsWithIgnoreCaseAndWs(noCommentsSql, "select")) {
      // #bug 2011-12-9,this select-for-update regex has low
      // performance,so
      // first judge this sql whether have ' for ' string.
      if (noCommentsSql.toLowerCase().contains(" for ")
          && SELECT_FOR_UPDATE_PATTERN.matcher(noCommentsSql).matches()) {
        sqlType = SqlType.SELECT_FOR_UPDATE;
      } else {
        sqlType = SqlType.SELECT;
      }
    } else if (StringUtils.startsWithIgnoreCaseAndWs(noCommentsSql, "show")) {
      sqlType = SqlType.SHOW;
    } else if (StringUtils.startsWithIgnoreCaseAndWs(noCommentsSql, "insert")) {
      sqlType = SqlType.INSERT;
    } else if (StringUtils.startsWithIgnoreCaseAndWs(noCommentsSql, "update")) {
      sqlType = SqlType.UPDATE;
    } else if (StringUtils.startsWithIgnoreCaseAndWs(noCommentsSql, "delete")) {
      sqlType = SqlType.DELETE;
    } else if (StringUtils.startsWithIgnoreCaseAndWs(noCommentsSql, "replace")) {
      sqlType = SqlType.REPLACE;
    } else if (StringUtils.startsWithIgnoreCaseAndWs(noCommentsSql, "truncate")) {
      sqlType = SqlType.TRUNCATE;
    } else if (StringUtils.startsWithIgnoreCaseAndWs(noCommentsSql, "create")) {
      sqlType = SqlType.CREATE;
    } else if (StringUtils.startsWithIgnoreCaseAndWs(noCommentsSql, "drop")) {
      sqlType = SqlType.DROP;
    } else if (StringUtils.startsWithIgnoreCaseAndWs(noCommentsSql, "load")) {
      sqlType = SqlType.LOAD;
    } else if (StringUtils.startsWithIgnoreCaseAndWs(noCommentsSql, "merge")) {
      sqlType = SqlType.MERGE;
    } else {
      sqlType = null;
    }
    // sqlType = globalCache.setSqlTypeIfAbsent(sql, sqlType);
    // }
    return sqlType;
  }

  /**
   * @return 返回sql中第一个表明的小写 使用约束 1. 不考虑sql中引号字符串包含关键字的情况。假定sql中无字串。都用prepareStatement加问号方式 2.
   *     对于from后有两个表的，包括括号方式的临时视图，伪表之类，都以第一个表名为准 3. ic_cache@lnk_icdb0 icuser.tb0
   */
  public static String findTableName(String sql0) {
    if (sql0 == null) {
      return null;
    }
    sql0 = sql0.trim(); // trim可以去掉\\s,包括换行符、制表符等
    if (sql0.length() < 7) {
      return null;
    }

    if (sql0.indexOf("/*") != -1) {
      // 去除hint
      // System.out.println("hint:"+sql0);
      sql0 = sql0.replaceAll(hintregx, "").trim(); // 懒惰匹配(最短匹配)
      // System.out.println(sql0);
    }
    sql0 = sql0.toLowerCase();
    sql0 = sql0 + " "; // 便于处理

    if (sql0.startsWith("update")) {
      Matcher m = ptable.matcher(sql0);
      if (m.find(6)) {
        return m.group(1);
      }
      return null;
    }

    if (sql0.startsWith("delete")) {
      Matcher m = pdelete_from.matcher(sql0);
      if (m.find(6)) {
        return m.group(1);
      }

      m = ptable.matcher(sql0); // delete 可以没有from
      if (m.find(6)) {
        return m.group(1);
      }
      return null;
    }

    if (sql0.startsWith("insert")) {
      Matcher m = pinsert_into.matcher(sql0);
      if (m.find(6)) {
        return m.group(1);
      }
      return null;
    }

    if (sql0.startsWith("replace")) {
      Matcher m = preplace_from.matcher(sql0);
      if (m.find(6)) {
        return m.group(1);
      }
      return null;
    }

    if (!sql0.startsWith("select")) {
      return null; // 不以update delete select开头的sql
    }

    Matcher m = pselect_from.matcher(sql0);
    if (m.find(6)) {
      return m.group(1);
    }

    m = pfrom_where.matcher(sql0);
    if (m.find(6)) {
      String from2where = m.group(1);
      // System.out.println(from2where);
      String[] tables = from2where.split(",");
      for (int i = 1; i < tables.length; i++) {
        // 因为第一个项已经搜索过了，所以从第二项开始
        if (tables[i].indexOf('(') == -1) {
          return tables[i].trim().split("\\s")[0];
        } else {
          String subTable = findTableName(tables[i]);
          if (subTable != null) {
            return subTable;
          }
        }
      }
    }

    // 考虑是否一开始就对所有的右括号前后加空格
    if (sql0.indexOf(")from") != -1) {
      System.out.println(sql0);
      sql0 = sql0.replaceAll("\\)from", ") from");
      return findTableName(sql0);
    }

    return null;
  }

  public enum SqlType {
    SELECT(0),
    INSERT(1),
    UPDATE(2),
    DELETE(3),
    SELECT_FOR_UPDATE(4),
    REPLACE(5),
    TRUNCATE(6),
    CREATE(7),
    DROP(8),
    LOAD(9),
    MERGE(10),
    SHOW(11),
    DEFAULT_SQL_TYPE(-100);
    private int i;

    private SqlType(int i) {
      this.i = i;
    }

    public int value() {
      return this.i;
    }

    public static SqlType valueOf(int i) {
      for (SqlType t : values()) {
        if (t.value() == i) {
          return t;
        }
      }
      throw new IllegalArgumentException("Invalid SqlType:" + i);
    }
  }
}
