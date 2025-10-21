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

import static com.alibaba.chaosblade.exec.common.model.action.returnv.compiler.ConstantType.*;

import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Calculator for metric cal
 *
 * @author Muxin Sun 2019/09/23
 */
public abstract class Calculator {

  /** function name */
  private static final String MIN_FUNCTION = "min";

  private static final String MAX_FUNCTION = "max";
  private static final String SUM_FUNCTION = "sum";
  private static final String AVG_FUNCTION = "avg";
  private static final String COUNT_FUNCTION = "count";
  private static final String RANDOM_FUNCTION = "random";
  private static final String MATCH_FUNCTION = "match";
  private static final String NUMERIC_FUNCTION = "numeric";
  private static final String BOOLEAN_FUNCTION = "boolean";
  private static final String STRING_FUNCTION = "string";
  private static final String NONNULL_FUNCTION = "nonNull";
  private static final String ISNULL_FUNCTION = "isNull";
  private static final String IF_FUNCTION = "if";
  private static final String SIN_FUNCTION = "sin";
  private static final String COS_FUNCTION = "cos";
  private static final String ASIN_FUNCTION = "asin";
  private static final String ACOS_FUNCTION = "acos";

  private static final Random rng = new Random();

  private double getRandomDouble() {
    return rng.nextDouble();
  }

  /**
   * @param valueA valueA is numeric
   * @param valueB valueB is numeric
   * @return 1 if valueA grant than valueB, else 0
   * @throws CompilerException Non-numeric type cannot be multiplied.
   */
  Constant gt(Constant valueA, Constant valueB) throws CompilerException {
    if (valueA.isNUMERIC() && valueB.isNUMERIC()) {
      return Constant.build(
          BOOLEAN, valueA.getAsNumber().doubleValue() > valueB.getAsNumber().doubleValue());
    } else if (valueA.isSTRING() && valueB.isSTRING()) {
      return Constant.build(BOOLEAN, valueA.getAsString().compareTo(valueB.getAsString()) > 0);
    }
    throw new CompilerException("Operator type cannot be compared.");
  }

  /**
   * @param valueA valueA is numeric
   * @param valueB valueB is numeric
   * @return 1 if valueA less than valueB, else 0
   * @throws CompilerException Non-numeric type cannot be multiplied.
   */
  Constant lt(Constant valueA, Constant valueB) throws CompilerException {
    if (valueA.isNUMERIC() && valueB.isNUMERIC()) {
      return Constant.build(
          BOOLEAN, valueA.getAsNumber().doubleValue() < valueB.getAsNumber().doubleValue());
    } else if (valueA.isSTRING() && valueB.isSTRING()) {
      return Constant.build(BOOLEAN, valueA.getAsString().compareTo(valueB.getAsString()) < 0);
    }
    throw new CompilerException("Operator type cannot be compared.");
  }

  /**
   * '=' sign
   *
   * @param valueA valueA is numeric
   * @param valueB valueB is numeric
   * @return 1 if valueA less than valueB, else 0
   * @throws CompilerException Non-numeric type cannot be multiplied.
   */
  Constant eq(Constant valueA, Constant valueB) throws CompilerException {
    if (valueA.isNUMERIC() && valueB.isNUMERIC()) {
      return Constant.build(BOOLEAN, valueA.getAsNumber().equals(valueB.getAsNumber()));
    } else if (valueA.isSTRING() && valueB.isSTRING()) {
      return Constant.build(BOOLEAN, valueA.getAsString().equals(valueB.getAsString()));
    }
    throw new CompilerException("Operator type cannot be compared.");
  }

  /**
   * @param valueA valueA is numeric
   * @param valueB valueB is numeric
   * @return 1 if valueA less than valueB, else 0
   * @throws CompilerException Non-numeric type cannot be multiplied.
   */
  Constant ge(Constant valueA, Constant valueB) throws CompilerException {
    if (valueA.isNUMERIC() && valueB.isNUMERIC()) {
      return Constant.build(
          BOOLEAN, valueA.getAsNumber().doubleValue() >= valueB.getAsNumber().doubleValue());
    } else if (valueA.isSTRING() && valueB.isSTRING()) {
      return Constant.build(BOOLEAN, valueA.getAsString().compareTo(valueB.getAsString()) >= 0);
    }
    throw new CompilerException("Operator type cannot be compared.");
  }

  /**
   * @param valueA valueA is numeric
   * @param valueB valueB is numeric
   * @return 1 if valueA less than or equal valueB, else 0
   * @throws CompilerException Non-numeric type cannot be multiplied.
   */
  Constant le(Constant valueA, Constant valueB) throws CompilerException {
    if (valueA.isNUMERIC() && valueB.isNUMERIC()) {
      return Constant.build(
          BOOLEAN, valueA.getAsNumber().doubleValue() <= valueB.getAsNumber().doubleValue());
    } else if (valueA.isSTRING() && valueB.isSTRING()) {
      return Constant.build(BOOLEAN, valueA.getAsString().compareTo(valueB.getAsString()) <= 0);
    }
    throw new CompilerException("Operator type cannot be compared.");
  }

  /**
   * '+' sign
   *
   * @param valueA valueA is numeric
   * @param valueB valueB is numeric
   * @return <code>valueA+valueB</code>
   * @throws CompilerException Non-numeric types cannot be added.
   */
  Constant plus(Constant valueA, Constant valueB) throws CompilerException {
    if (valueA.isNUMERIC() && valueB.isNUMERIC()) {
      return Constant.build(
          NUMERIC,
          new BigDecimal(valueA.getAsNumber().toString())
              .add(new BigDecimal(valueB.getAsNumber().toString())));
    } else if (valueA.isSTRING() && valueB.isSTRING()) {
      return Constant.build(STRING, valueA.getAsString() + valueB.getAsString());
    }
    return Constant.build(NUMERIC, Double.NaN);
  }

  /**
   * '-' sign
   *
   * @param valueA valueA is numeric
   * @param valueB valueB is numeric
   * @return <code>valueA-valueB</code>
   * @throws CompilerException Non-numeric type cannot be subtracted.
   */
  Constant minus(Constant valueA, Constant valueB) throws CompilerException {
    if (valueA.isNUMERIC() && valueB.isNUMERIC()) {
      return Constant.build(
          NUMERIC,
          new BigDecimal(valueA.getAsNumber().toString())
              .subtract(new BigDecimal(valueB.getAsNumber().toString())));
    }
    return Constant.build(NUMERIC, Double.NaN);
  }

  /**
   * '*' sign
   *
   * @param valueA valueA is numeric
   * @param valueB valueB is numeric
   * @return <code>valueA*valueB</code>
   * @throws CompilerException Non-numeric type cannot be multiplied.
   */
  Constant times(Constant valueA, Constant valueB) throws CompilerException {
    if (valueA.isNUMERIC() && valueB.isNUMERIC()) {
      return Constant.build(
          NUMERIC,
          new BigDecimal(valueA.getAsNumber().toString())
              .multiply(new BigDecimal(valueB.getAsNumber().toString())));
    }
    return Constant.build(NUMERIC, Double.NaN);
  }

  /**
   * '/' sign
   *
   * @param valueA valueA is numeric
   * @param valueB valueB is numeric
   * @return <code>valueA/valueB</code> valueB is no-null
   * @throws CompilerException Non-numeric type cannot be divided.
   */
  Constant division(Constant valueA, Constant valueB) throws CompilerException {
    if (valueA.isNUMERIC() && valueB.isNUMERIC()) {
      return Constant.build(
          NUMERIC,
          new BigDecimal(valueA.getAsNumber().toString())
              .divide(new BigDecimal(valueB.getAsNumber().toString())));
    }
    return Constant.build(NUMERIC, Double.NaN);
  }

  /**
   * '%' sign
   *
   * @param valueA valueA is numeric
   * @param valueB valueB is numeric
   * @return <code>valueA/valueB</code> valueB is no-null
   * @throws CompilerException Non-numeric type cannot be divided.
   */
  Constant modulo(Constant valueA, Constant valueB) throws CompilerException {
    if (valueA.isNUMERIC() && valueB.isNUMERIC()) {
      return Constant.build(
          NUMERIC, valueA.getAsNumber().longValue() % valueB.getAsNumber().longValue());
    }
    return Constant.build(NUMERIC, Double.NaN);
  }

  /**
   * '^' sign
   *
   * @param valueA valueA is numeric
   * @param valueB valueB is numeric
   * @return <code>valueA^valueB</code> valueB is no-null
   * @throws CompilerException Non-numeric type cannot be divided.
   */
  Constant pow(Constant valueA, Constant valueB) throws CompilerException {
    if (valueA.isNUMERIC() && valueB.isNUMERIC()) {

      return Constant.build(
          NUMERIC,
          new BigDecimal(valueA.getAsNumber().toString()).pow(valueB.getAsNumber().intValue()));
    }
    return Constant.build(NUMERIC, Double.NaN);
  }

  /**
   * 'and' sign
   *
   * @param valueA valueA is numeric
   * @param valueB valueB is numeric
   * @return <code>valueA^valueB</code> valueB is no-null
   * @throws CompilerException Non-numeric type cannot be divided.
   */
  Constant and(Constant valueA, Constant valueB) throws CompilerException {
    if (valueA.isBOOLEAN() && valueB.isBOOLEAN()) {
      return Constant.build(BOOLEAN, valueA.getAsBoolean() && valueB.getAsBoolean());
    }
    return Constant.build(BOOLEAN, false);
  }

  /**
   * 'or' sign
   *
   * @param valueA valueA is numeric
   * @param valueB valueB is numeric
   * @return <code>valueA^valueB</code> valueB is no-null
   * @throws CompilerException Non-numeric type cannot be divided.
   */
  Constant or(Constant valueA, Constant valueB) throws CompilerException {
    if (valueA.isBOOLEAN() && valueB.isBOOLEAN()) {
      return Constant.build(BOOLEAN, valueA.getAsBoolean() || valueB.getAsBoolean());
    }
    return Constant.build(BOOLEAN, false);
  }

  private boolean allMatchNumeric(final List<Constant> constants) {
    for (Constant constant : constants) {
      if (!constant.isNUMERIC()) {
        return false;
      }
    }
    return true;
  }

  private boolean allMatchString(final List<Constant> constants) {
    for (Constant constant : constants) {
      if (!constant.isSTRING()) {
        return false;
      }
    }
    return true;
  }

  private boolean allMatchNonNull(final List<Constant> constants) {
    for (Constant constant : constants) {
      if (constant.isNULL()) {
        return false;
      }
    }
    return true;
  }

  /**
   * execute function and
   *
   * @param function function name
   * @param parameters parameters is variate_stream
   * @return <code>execute result</code>
   * @throws CompilerException if function is not def.
   */
  Constant execute(String function, List<Constant> parameters) throws CompilerException {
    if (MIN_FUNCTION.equals(function)) {
      if (allMatchNumeric(parameters)) {
        double result = Double.NaN;
        for (Constant constant : parameters) {
          if (Double.isNaN(result)) {
            result = constant.getAsNumber().doubleValue();
          } else {
            result = Math.min(constant.getAsNumber().doubleValue(), result);
          }
        }
        return Constant.build(NUMERIC, result);
      } else if (allMatchString(parameters)) {
        String result = null;
        for (Constant constant : parameters) {
          if (result == null) {
            result = constant.getAsString();
          } else if (result.compareTo(constant.getAsString()) < 0) {
            result = constant.getAsString();
          }
        }
        return Constant.build(STRING, result);
      }
      throw new CompilerException(
          "min function do not accept non-numeric and no-string parameters.");
    } else if (MAX_FUNCTION.equals(function)) {
      if (allMatchNumeric(parameters)) {
        double result = Double.NaN;
        for (Constant constant : parameters) {
          if (Double.isNaN(result)) {
            result = constant.getAsNumber().doubleValue();
          } else {
            result = Math.max(constant.getAsNumber().doubleValue(), result);
          }
        }
        return Constant.build(NUMERIC, result);
      } else if (allMatchString(parameters)) {
        String result = null;
        for (Constant constant : parameters) {
          if (result == null) {
            result = constant.getAsString();
          } else if (result.compareTo(constant.getAsString()) > 0) {
            result = constant.getAsString();
          }
        }
        return Constant.build(STRING, result);
      }
      throw new CompilerException(
          "max function do not accept non-numeric and no-string parameters.");
    } else if (SUM_FUNCTION.equals(function)) {
      if (allMatchNumeric(parameters)) {
        double result = 0;
        for (Constant constant : parameters) {
          result += constant.getAsNumber().doubleValue();
        }
        return Constant.build(NUMERIC, result);
      }
      throw new CompilerException("sum function do not accept non-numeric parameters.");
    } else if (AVG_FUNCTION.equals(function)) {
      if (allMatchNumeric(parameters)) {
        double result = 0, count = 0;
        for (Constant constant : parameters) {
          result += constant.getAsNumber().doubleValue();
          count += 1;
        }
        return Constant.build(NUMERIC, result / count);
      }
      throw new CompilerException("avg function do not accept non-numeric parameters.");
    } else if (COUNT_FUNCTION.equals(function)) {
      return Constant.build(NUMERIC, (long) parameters.size());
    } else if (RANDOM_FUNCTION.equals(function)) {
      return Constant.build(NUMERIC, getRandomDouble());
    } else if (MATCH_FUNCTION.equals(function)) {
      if (parameters.size() == 3 && allMatchNonNull(parameters)) {
        final List<String> array = new ArrayList<String>();
        Matcher match =
            Pattern.compile(parameters.get(0).getAsString())
                .matcher(parameters.get(1).getAsString());
        final int count = parameters.get(2).getAsNumber().intValue();
        for (int i = 0; match.find(); i++) {
          if (i == count) {
            return Constant.build(STRING, match.group());
          } else {
            array.add(match.group());
          }
        }
        if (-1 == count) {
          return Constant.build(ARRAY, array.toArray());
        }
      } else if (parameters.size() == 2 && allMatchNonNull(parameters)) {
        Matcher match =
            Pattern.compile(parameters.get(0).getAsString())
                .matcher(parameters.get(1).getAsString());
        int count = 0;
        while (match.find()) {
          count++;
        }
        return Constant.build(NUMERIC, count);
      }
      return Constant.build(NULL, null);
    } else if (NUMERIC_FUNCTION.equals(function)) {
      if (parameters.size() == 1) {
        if (parameters.get(0).getValue() == null) {
          return Constant.build(NUMERIC, Double.NaN);
        }
        return Constant.build(NUMERIC, Double.parseDouble(parameters.get(0).getAsString()));
      }
      throw new CompilerException("type translation nonsupport multi-parameter.");
    } else if (BOOLEAN_FUNCTION.equals(function)) {
      if (parameters.size() == 1) {
        if (parameters.get(0).getValue() == null) {
          return Constant.build(BOOLEAN, false);
        }
        return Constant.build(BOOLEAN, parameters.get(0).getAsBoolean());
      }
      throw new CompilerException("type translation nonsupport multi-parameter.");
    } else if (STRING_FUNCTION.equals(function)) {
      if (parameters.size() == 1) {
        if (parameters.get(0).getValue() == null) {
          return Constant.build(STRING, "");
        }
        return Constant.build(STRING, parameters.get(0).getAsString());
      }
      throw new CompilerException("type translation nonsupport multi-parameter.");
    } else if (NONNULL_FUNCTION.equals(function)) {
      if (parameters.size() == 1) {
        if (parameters.get(0).isNUMERIC()) {
          return Constant.build(
              BOOLEAN, !Double.isNaN(parameters.get(0).getAsNumber().doubleValue()));
        }
        return Constant.build(BOOLEAN, !parameters.get(0).isNULL());
      }
      throw new CompilerException("nonNull function nonsupport multi-parameter.");
    } else if (ISNULL_FUNCTION.equals(function)) {
      if (parameters.size() == 1) {
        if (parameters.get(0).isNUMERIC()) {
          return Constant.build(
              BOOLEAN, Double.isNaN(parameters.get(0).getAsNumber().doubleValue()));
        }
        return Constant.build(BOOLEAN, parameters.get(0).isNULL());
      }
      throw new CompilerException("isNull function nonsupport multi-parameter.");
    } else if (IF_FUNCTION.equals(function)) {
      if (parameters.size() == 3) {
        if (parameters.get(0).getAsBoolean()) {
          return parameters.get(1);
        } else {
          return parameters.get(2);
        }
      }
      throw new CompilerException("if function nonsupport no 3-parameter.");
    } else if (SIN_FUNCTION.equals(function)) {
      if (parameters.size() == 1 && parameters.get(0).isNUMERIC()) {
        return Constant.build(NUMERIC, Math.sin(parameters.get(0).getAsNumber().doubleValue()));
      }
      return Constant.build(NUMERIC, Double.NaN);
    } else if (COS_FUNCTION.equals(function)) {
      if (parameters.size() == 1 && parameters.get(0).isNUMERIC()) {
        return Constant.build(NUMERIC, Math.cos(parameters.get(0).getAsNumber().doubleValue()));
      }
      return Constant.build(NUMERIC, Double.NaN);
    } else if (ASIN_FUNCTION.equals(function)) {
      if (parameters.size() == 1 && parameters.get(0).isNUMERIC()) {
        return Constant.build(NUMERIC, Math.asin(parameters.get(0).getAsNumber().doubleValue()));
      }
      return Constant.build(NUMERIC, Double.NaN);
    } else if (ACOS_FUNCTION.equals(function)) {
      if (parameters.size() == 1 && parameters.get(0).isNUMERIC()) {
        return Constant.build(NUMERIC, Math.acos(parameters.get(0).getAsNumber().doubleValue()));
      }
      return Constant.build(NUMERIC, Double.NaN);
    }
    throw new CompilerException("function is not def.");
  }

  /**
   * execute function, and args is array
   *
   * @param function function name
   * @param parameters function parameters
   * @throws CompilerException Calculator error
   * @return <code>execute result</code>
   */
  Constant execute(String function, Constant... parameters) throws CompilerException {
    return execute(function, Arrays.asList(parameters));
  }

  /**
   * check operator is a function?
   *
   * <pre>
   * isFunction(null)   = false
   * isFunction("")     = true
   * isFunction("  ")   = false
   * isFunction("abc")  = true
   * isFunction("ab2c") = false
   * isFunction("ab-c") = false
   * </pre>
   *
   * @param operator operator name
   * @return <code>true</code> if only contains letters, and is non-null
   */
  boolean isFunction(String operator) {
    return operator.trim().length() > 0;
  }

  /**
   * get variate value, by name
   *
   * @param name variate name
   * @throws CompilerException can not get value
   * @return <code>variate</code>
   */
  protected abstract Constant getValue(String name) throws CompilerException;

  public abstract boolean isVariate(String name);
}
