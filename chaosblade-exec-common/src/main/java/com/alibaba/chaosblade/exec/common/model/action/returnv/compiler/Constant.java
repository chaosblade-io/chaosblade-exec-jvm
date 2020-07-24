package com.alibaba.chaosblade.exec.common.model.action.returnv.compiler;

import lombok.Getter;

import java.math.BigDecimal;
import java.util.Collections;

import static com.alibaba.chaosblade.exec.common.model.action.returnv.compiler.ConstantType.*;
import static com.alibaba.chaosblade.exec.common.model.action.returnv.compiler.Lexicon.*;

/**
 *
 * @author Muxin Sun
 * 2019/09/23
 */

@Getter
public class Constant {

    public static final Constant LEFT_PARENTHESIS_CONSTANT
            = new Constant(SYMBOL, LEFT_PARENTHESIS);
    public static final Constant RIGHT_PARENTHESIS_CONSTANT
            = new Constant(SYMBOL, RIGHT_PARENTHESIS);
    public static final Constant GREATER_THAN_CONSTANT
            = new Constant(SYMBOL, GREATER_THAN);
    public static final Constant LESS_THAN_CONSTANT
            = new Constant(SYMBOL, LESS_THAN);
    public static final Constant EQUAL_CONSTANT
            = new Constant(SYMBOL, EQUAL);
    public static final Constant PLUS_CONSTANT
            = new Constant(SYMBOL, PLUS);
    public static final Constant MINUS_CONSTANT
            = new Constant(SYMBOL, MINUS);
    public static final Constant COMMA_CONSTANT
            = new Constant(SYMBOL, COMMA);
    public static final Constant TIMES_CONSTANT
            = new Constant(SYMBOL, TIMES);
    public static final Constant DIVISION_CONSTANT
            = new Constant(SYMBOL, DIVISION);
    public static final Constant GREATER_EQUAL_CONSTANT
            = new Constant(SYMBOL, GREATER_EQUAL);
    public static final Constant LESS_EQUAL_CONSTANT
            = new Constant(SYMBOL, LESS_EQUAL);
    public static final Constant OR_CONSTANT
            = new Constant(SYMBOL, OR);
    public static final Constant AND_CONSTANT
            = new Constant(SYMBOL, AND);
    public static final Constant END_CONSTANT
            = new Constant(SYMBOL, END);
    public static final Constant POW_CONSTANT
            = new Constant(SYMBOL, POW);
    public static final Constant MODULO_CONSTANT
            = new Constant(SYMBOL, MODULO);

    /**
     *     public static final Constant NULL_CONSTANT
     *             = new Constant(SYMBOL, new JsonPrimitive(NULL));
     */

    /**
     * <p>Lexical compiler triple: <code>type</code> is variate type,
     *    <code>name</code> is variate name, <code>value</code> is variate value. </p>
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
     * <p> Variate Constructor. </p>
     *
     * @param type variate type.
     * @param value variate value.
     */
    private Constant(final ConstantType type, final Object value) {
        this.type = type;
        this.value = value;
    }

    /**
     * <p>build <code>type</code> variate</p>
     *
     * @param obj variate value.
     * @return <code>Variate</code> Variate type is <code>type</code>, name is null, value is empty
     */
    static Constant build(Object obj) {
        return new Constant(ORIGINAL, null);
    }

    /**
     * <p>build <code>type</code> variate</p>
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
     * <p>check {@code value} is empty?</p>
     *
     * @return ture if {@code value} is null or the size of {@code value} is 0, else false
     * @see #value
     */
    private boolean isEmpty() {
        return this.value==null || this.value.toString().trim().length()==0;
    }

    /**
     * <p>check variate {@code type}</p>
     *
     * @return true if {@code type} is {@code STRING}, else false
     */
    public boolean isSTRING() {
        return this.type.equals(STRING);
    }

    /**
     * <p>check variate {@code type}</p>
     *
     * @return true if {@code type} is {@code NUMERIC}, else false
     */
    public boolean isNUMERIC() {
        return this.type.equals(NUMERIC);
    }

    /**
     * <p>check variate {@code type}</p>
     *
     * @return true if {@code type} is {@code NULL}, else false
     */
    public boolean isNULL() {
        return this.type.equals(NULL);
    }

    /**
     * <p>check variate {@code type}</p>
     *
     * @return true if {@code type} is {@code FUNCTION}, else false
     */
    public boolean isFUNCTION() {
        return this.type.equals(FUNCTION);
    }

    /**
     * <p>check variate {@code type}</p>
     *
     * @return true if {@code type} is {@code SYMBOL}, else false
     */
    public boolean isSYMBOL() {
        return this.type.equals(SYMBOL);
    }

    /**
     * <p>check variate {@code type}</p>
     *
     * @return true if {@code type} is {@code BOOLEAN}, else false
     */
    public boolean isBOOLEAN() {
        return this.type.equals(BOOLEAN);
    }

    /**
     * <p>check variate {@code type}</p>
     *
     * @return true if {@code type} is {@code ORIGINAL}, else false
     */
    public boolean isORIGINAL() {
        return this.type.equals(ORIGINAL);
    }

    /**
     * <p>check variate {@code type}</p>
     *
     * @return true if {@code type} is {@code VARIATE}, else false
     */
    public boolean isVARIATE() {
        return this.type.equals(VARIATE);
    }

    /**
     * <p>check variate {@code type}</p>
     *
     * @return true if {@code type} is {@code ARRAY}, else false
     */
    public boolean isARRAY() { return this.type.equals(ARRAY); }

    /**
     * <p>check variate {@code value}</p>
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
     * <p>toString function</p>
     *
     * @return string
     */
    @Override
    public String toString() {
        return String.format("type is %s -> value is %s", this.type, this.value);
    }
}
