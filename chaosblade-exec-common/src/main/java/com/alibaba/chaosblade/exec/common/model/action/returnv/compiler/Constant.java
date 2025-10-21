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
import static com.alibaba.chaosblade.exec.common.model.action.returnv.compiler.Lexicon.*;

import java.math.BigDecimal;
import java.util.Collections;
import lombok.Getter;

/** @author Muxin Sun 2019/09/23 */
@Getter
public class Constant {

  public static final Constant LEFT_PARENTHESIS_CONSTANT = new Constant(SYMBOL, LEFT_PARENTHESIS);
  public static final Constant RIGHT_PARENTHESIS_CONSTANT = new Constant(SYMBOL, RIGHT_PARENTHESIS);
  public static final Constant GREATER_THAN_CONSTANT = new Constant(SYMBOL, GREATER_THAN);
  public static final Constant LESS_THAN_CONSTANT = new Constant(SYMBOL, LESS_THAN);
  public static final Constant EQUAL_CONSTANT = new Constant(SYMBOL, EQUAL);
  public static final Constant PLUS_CONSTANT = new Constant(SYMBOL, PLUS);
  public static final Constant MINUS_CONSTANT = new Constant(SYMBOL, MINUS);
  public static final Constant COMMA_CONSTANT = new Constant(SYMBOL, COMMA);
  public static final Constant TIMES_CONSTANT = new Constant(SYMBOL, TIMES);
  public static final Constant DIVISION_CONSTANT = new Constant(SYMBOL, DIVISION);
  public static final Constant GREATER_EQUAL_CONSTANT = new Constant(SYMBOL, GREATER_EQUAL);
  public static final Constant LESS_EQUAL_CONSTANT = new Constant(SYMBOL, LESS_EQUAL);
  public static final Constant OR_CONSTANT = new Constant(SYMBOL, OR);
  public static final Constant AND_CONSTANT = new Constant(SYMBOL, AND);
  public static final Constant END_CONSTANT = new Constant(SYMBOL, END);
  public static final Constant POW_CONSTANT = new Constant(SYMBOL, POW);
  public static final Constant MODULO_CONSTANT = new Constant(SYMBOL, MODULO);

  /** public static final Constant NULL_CONSTANT = new Constant(SYMBOL, new JsonPrimitive(NULL)); */

  /**
   * Lexical compiler triple: <code>type</code> is variate type, <code>name</code> is variate name,
   * <code>value</code> is variate value.
   *
   * @date 2019/09/21
   */
  private ConstantType type;

  private Object value;

  public Number getAsNumber() {
    if (value instanceof Number) {
      return (Number) value;
    } else if (value instanceof BigDecimal) {
      return ((BigDecimal) value).doubleValue();
    }
    return Double.NaN;
  }

  public String getAsString() {
    if (value == null) {
      return "null";
    }
    if (value instanceof String) {
      return (String) value;
    }
    if (value instanceof Double) {
      Double value = (Double) this.value;
      return value % 1 == 0 ? String.valueOf(value.longValue()) : value.toString();
    }
    if (value instanceof BigDecimal) {
      Double value = ((BigDecimal) this.value).doubleValue();
      return value % 1 == 0 ? String.valueOf(value.longValue()) : value.toString();
    }
    return value.toString();
  }

  public Boolean getAsBoolean() {
    if (value instanceof Boolean) {
      return Boolean.parseBoolean(value.toString());
    }
    return false;
  }

  public Object[] getAsArray() {
    if (value.getClass().isArray()) {
      return (Object[]) value;
    }
    return Collections.singletonList(value).toArray();
  }

  /**
   * Variate Constructor.
   *
   * @param type variate type.
   * @param value variate value.
   */
  private Constant(final ConstantType type, final Object value) {
    this.type = type;
    this.value = value;
  }

  /**
   * build <code>type</code> variate
   *
   * @param obj variate value.
   * @return <code>Variate</code> Variate type is <code>type</code>, name is null, value is empty
   */
  static Constant build(Object obj) {
    return new Constant(ORIGINAL, null);
  }

  /**
   * build <code>type</code> variate
   *
   * @param type datum type.
   * @param obj variate value.
   * @throws CompilerException type can not match value
   * @return <code>Variate</code> Variate type is <code>type</code>, name is null, value is empty
   */
  public static Constant build(ConstantType type, Object obj) throws CompilerException {
    if (obj == null) {
      type = NULL;
    }
    switch (type) {
      case NULL:
      case ORIGINAL:
        return new Constant(type, null);
      case NUMERIC:
        if (obj instanceof Number) {
          return new Constant(type, obj);
        }
        break;
      case BOOLEAN:
        if (obj instanceof Boolean) {
          return new Constant(type, obj);
        }
        break;
      case STRING:
      case SYMBOL:
      case FUNCTION:
      case VARIATE:
        return new Constant(type, obj.toString());
      case ARRAY:
        return new Constant(type, obj);
      default:
        throw new CompilerException("type '" + type.name() + "' is not exist");
    }
    throw new CompilerException("type is '" + type.name() + "', but value is " + obj);
  }

  /**
   * check {@code value} is empty?
   *
   * @return ture if {@code value} is null or the size of {@code value} is 0, else false
   * @see #value
   */
  private boolean isEmpty() {
    return this.value == null || this.value.toString().trim().length() == 0;
  }

  /**
   * check variate {@code type}
   *
   * @return true if {@code type} is {@code STRING}, else false
   */
  public boolean isSTRING() {
    return this.type.equals(STRING);
  }

  /**
   * check variate {@code type}
   *
   * @return true if {@code type} is {@code NUMERIC}, else false
   */
  public boolean isNUMERIC() {
    return this.type.equals(NUMERIC);
  }

  /**
   * check variate {@code type}
   *
   * @return true if {@code type} is {@code NULL}, else false
   */
  public boolean isNULL() {
    return this.type.equals(NULL);
  }

  /**
   * check variate {@code type}
   *
   * @return true if {@code type} is {@code FUNCTION}, else false
   */
  public boolean isFUNCTION() {
    return this.type.equals(FUNCTION);
  }

  /**
   * check variate {@code type}
   *
   * @return true if {@code type} is {@code SYMBOL}, else false
   */
  public boolean isSYMBOL() {
    return this.type.equals(SYMBOL);
  }

  /**
   * check variate {@code type}
   *
   * @return true if {@code type} is {@code BOOLEAN}, else false
   */
  public boolean isBOOLEAN() {
    return this.type.equals(BOOLEAN);
  }

  /**
   * check variate {@code type}
   *
   * @return true if {@code type} is {@code ORIGINAL}, else false
   */
  public boolean isORIGINAL() {
    return this.type.equals(ORIGINAL);
  }

  /**
   * check variate {@code type}
   *
   * @return true if {@code type} is {@code VARIATE}, else false
   */
  public boolean isVARIATE() {
    return this.type.equals(VARIATE);
  }

  /**
   * check variate {@code type}
   *
   * @return true if {@code type} is {@code ARRAY}, else false
   */
  public boolean isARRAY() {
    return this.type.equals(ARRAY);
  }

  /**
   * check variate {@code value}
   *
   * @param obj constant
   * @return true if {@code value} is value, else false
   */
  @Override
  public boolean equals(Object obj) {
    if (obj instanceof Constant) {
      Constant v = (Constant) obj;
      return v.type.equals(this.type) && v.value.toString().equals(this.value.toString());
    }
    return false;
  }

  /**
   * toString function
   *
   * @return string
   */
  @Override
  public String toString() {
    return String.format("type is %s -> value is %s", this.type, this.value);
  }

  public ConstantType getType() {
    return type;
  }

  public Object getValue() {
    return value;
  }
}
