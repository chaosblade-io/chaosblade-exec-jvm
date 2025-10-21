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

package com.alibaba.chaosblade.exec.common.model.action.returnv.compiler;

import static com.alibaba.chaosblade.exec.common.model.action.returnv.compiler.Constant.*;
import static com.alibaba.chaosblade.exec.common.model.action.returnv.compiler.ConstantType.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

class Lexicon {

  /**
   * @date 2019/05/20
   *     <p>Supported operators, sort operational character by priority level. <char>'</char> is max
   *     level, Note: <char>=</char> is not mutator method.
   */
  static final String LEFT_PARENTHESIS = "(";

  static final String RIGHT_PARENTHESIS = ")";
  static final String GREATER_THAN = ">";
  static final String LESS_THAN = "<";
  static final String EQUAL = "=";
  static final String PLUS = "+";
  static final String MINUS = "-";
  static final String COMMA = ",";
  static final String TIMES = "*";
  static final String DIVISION = "/";
  static final String POW = "^";
  static final String MODULO = "%";
  private static final String QUOTATION = "'";

  static final String GREATER_EQUAL = ">=";
  static final String LESS_EQUAL = "<=";

  static final String OR = " or ";
  static final String AND = " and ";
  static final String END = "$";

  private static final String[] OPERATORS = {
    LEFT_PARENTHESIS,
    RIGHT_PARENTHESIS,
    QUOTATION,
    GREATER_THAN,
    GREATER_EQUAL,
    LESS_THAN,
    LESS_EQUAL,
    EQUAL,
    PLUS,
    MINUS,
    COMMA,
    TIMES,
    DIVISION,
    OR,
    AND,
    END,
    POW,
    MODULO
  };

  private static Constant transform(final String cache) throws CompilerException {
    if (cache == null || cache.trim().length() == 0) {
      return null;
    } else if (Pattern.matches("^-?(0|([1-9]\\d*))(\\.\\d*)?$", cache)) {
      return build(NUMERIC, Double.valueOf(cache));
    } else if (Pattern.matches("^(true|false)$", cache)) {
      return build(BOOLEAN, Boolean.valueOf(cache));
    } else if (cache.startsWith(QUOTATION) && cache.endsWith(QUOTATION)) {
      return build(STRING, cache.substring(1, cache.length() - 1));
    }
    return build(VARIATE, cache);
  }

  private static Constant transform(final ConstantType type, final String cache)
      throws CompilerException {
    if (cache == null || cache.trim().length() == 0) {
      return null;
    }
    return build(type, cache);
  }

  /**
   * check {@code op} is OPERATOR?
   *
   * @return ture if {@code op} is in {@code OPERATORS}, else false
   * @see #OPERATORS
   */
  static boolean isOperator(String op) {
    return Arrays.asList(OPERATORS).contains(op);
  }

  public static List<Constant> parse(final String formula) throws CompilerException {
    final List<Constant> words = new ArrayList<Constant>();
    StringBuilder cache = new StringBuilder();
    ConstantType prevType = null;
    for (int i = 0; i < formula.length(); ) {
      if (formula.substring(i).startsWith(OR)) {
        add(words, transform(cache.toString()));
        add(words, OR_CONSTANT);
        cache = new StringBuilder();
        i += OR.length();
        prevType = SYMBOL;
      } else if (formula.substring(i).startsWith(AND)) {
        add(words, transform(cache.toString().trim()));
        add(words, AND_CONSTANT);
        cache = new StringBuilder();
        i += AND.length();
        prevType = SYMBOL;
      } else if (formula.substring(i).startsWith(LESS_EQUAL)) {
        add(words, transform(cache.toString().trim()));
        add(words, LESS_EQUAL_CONSTANT);
        cache = new StringBuilder();
        i += LESS_EQUAL.length();
        prevType = SYMBOL;
      } else if (formula.substring(i).startsWith(POW)) {
        add(words, transform(cache.toString().trim()));
        add(words, POW_CONSTANT);
        cache = new StringBuilder();
        i += POW.length();
        prevType = SYMBOL;
      } else if (formula.substring(i).startsWith(GREATER_EQUAL)) {
        add(words, transform(cache.toString().trim()));
        add(words, GREATER_EQUAL_CONSTANT);
        cache = new StringBuilder();
        i += GREATER_EQUAL.length();
        prevType = SYMBOL;
      } else if (formula.substring(i).startsWith(DIVISION)) {
        add(words, transform(cache.toString().trim()));
        add(words, DIVISION_CONSTANT);
        cache = new StringBuilder();
        i += DIVISION.length();
        prevType = SYMBOL;
      } else if (formula.substring(i).startsWith(TIMES)) {
        add(words, transform(cache.toString().trim()));
        add(words, TIMES_CONSTANT);
        cache = new StringBuilder();
        i += TIMES.length();
        prevType = SYMBOL;
      } else if (formula.substring(i).startsWith(COMMA)) {
        add(words, transform(cache.toString().trim()));
        add(words, COMMA_CONSTANT);
        cache = new StringBuilder();
        i += COMMA.length();
        prevType = SYMBOL;
      } else if (formula.substring(i).startsWith(MINUS) && prevType != SYMBOL) {
        add(words, transform(cache.toString().trim()));
        add(words, MINUS_CONSTANT);
        cache = new StringBuilder();
        i += MINUS.length();
        prevType = SYMBOL;
      } else if (formula.substring(i).startsWith(PLUS)) {
        add(words, transform(cache.toString().trim()));
        add(words, PLUS_CONSTANT);
        cache = new StringBuilder();
        i += PLUS.length();
        prevType = SYMBOL;
      } else if (formula.substring(i).startsWith(EQUAL)) {
        add(words, transform(cache.toString().trim()));
        add(words, EQUAL_CONSTANT);
        cache = new StringBuilder();
        i += EQUAL.length();
        prevType = SYMBOL;
      } else if (formula.substring(i).startsWith(LESS_THAN)) {
        add(words, transform(cache.toString().trim()));
        add(words, LESS_THAN_CONSTANT);
        cache = new StringBuilder();
        i += LESS_THAN.length();
        prevType = SYMBOL;
      } else if (formula.substring(i).startsWith(GREATER_THAN)) {
        add(words, transform(cache.toString().trim()));
        add(words, GREATER_THAN_CONSTANT);
        cache = new StringBuilder();
        i += GREATER_THAN.length();
        prevType = SYMBOL;
      } else if (formula.substring(i).startsWith(RIGHT_PARENTHESIS)) {
        add(words, transform(cache.toString().trim()));
        add(words, RIGHT_PARENTHESIS_CONSTANT);
        cache = new StringBuilder();
        i += RIGHT_PARENTHESIS.length();
        prevType = VARIATE;
      } else if (formula.substring(i).startsWith(LEFT_PARENTHESIS)) {
        add(words, transform(FUNCTION, cache.toString().trim()));
        add(words, LEFT_PARENTHESIS_CONSTANT);
        cache = new StringBuilder();
        i += LEFT_PARENTHESIS.length();
        prevType = SYMBOL;
      } else if (formula.substring(i).startsWith(MODULO)) {
        add(words, transform(cache.toString().trim()));
        add(words, MODULO_CONSTANT);
        cache = new StringBuilder();
        i += MODULO.length();
        prevType = SYMBOL;
      } else if (formula.substring(i).startsWith(QUOTATION)) {
        add(words, transform(cache.toString().trim()));
        cache = new StringBuilder();
        cache.append(formula.charAt(i++));
        while (!formula.substring(i).startsWith(QUOTATION)) {
          cache.append(formula.charAt(i++));
        }
        cache.append(formula.charAt(i++));
        add(words, transform(cache.toString().trim()));
        cache = new StringBuilder();
        prevType = STRING;
      } else {
        cache.append(formula.charAt(i));
        i += 1;
        if (cache.toString().trim().length() > 0) {
          prevType = VARIATE;
        }
      }
    }
    if (cache.length() > 0) {
      add(words, transform(cache.toString().trim()));
    }

    return words;
  }

  private static void add(List<Constant> list, Constant ele) {
    if (ele != null && !ele.isNULL()) {
      list.add(ele);
    }
  }
}
