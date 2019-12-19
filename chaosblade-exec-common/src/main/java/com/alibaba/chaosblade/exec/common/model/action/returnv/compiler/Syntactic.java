package com.alibaba.chaosblade.exec.common.model.action.returnv.compiler;

import java.util.*;

import static com.alibaba.chaosblade.exec.common.model.action.returnv.compiler.Constant.*;

/**
 * @author Muxin Sun
 * 2019/09/20
 */
public class Syntactic {

    private static Map<Constant, Integer> SYMBOL_CONSTANTS = new HashMap<Constant, Integer>();

    static {
        SYMBOL_CONSTANTS.put(POW_CONSTANT, 0);
        SYMBOL_CONSTANTS.put(MODULO_CONSTANT, 1);
        SYMBOL_CONSTANTS.put(TIMES_CONSTANT, 1);
        SYMBOL_CONSTANTS.put(DIVISION_CONSTANT, 1);
        SYMBOL_CONSTANTS.put(PLUS_CONSTANT, 2);
        SYMBOL_CONSTANTS.put(MINUS_CONSTANT, 2);
        SYMBOL_CONSTANTS.put(GREATER_THAN_CONSTANT, 3);
        SYMBOL_CONSTANTS.put(LESS_THAN_CONSTANT, 3);
        SYMBOL_CONSTANTS.put(EQUAL_CONSTANT, 3);
        SYMBOL_CONSTANTS.put(GREATER_EQUAL_CONSTANT, 3);
        SYMBOL_CONSTANTS.put(LESS_EQUAL_CONSTANT, 3);
        SYMBOL_CONSTANTS.put(AND_CONSTANT, 4);
        SYMBOL_CONSTANTS.put(OR_CONSTANT, 5);
        SYMBOL_CONSTANTS.put(COMMA_CONSTANT, 6);
        SYMBOL_CONSTANTS.put(LEFT_PARENTHESIS_CONSTANT, 7);
        SYMBOL_CONSTANTS.put(RIGHT_PARENTHESIS_CONSTANT, 8);
        SYMBOL_CONSTANTS.put(END_CONSTANT, 8);
    }

    /**
     * <p> compare symbol prior </p>
     * @param a symbol a
     * @param b symbol b
     * @return true if a > b, then false
     */
    private static boolean isCalculated(Constant a, Constant b) {
        if (a.equals(LEFT_PARENTHESIS_CONSTANT)) {
            return b.equals(RIGHT_PARENTHESIS_CONSTANT);
        }
        if (!SYMBOL_CONSTANTS.get(a).equals(SYMBOL_CONSTANTS.get(b))) {
            return SYMBOL_CONSTANTS.get(a) > SYMBOL_CONSTANTS.get(b);
        }
        return !a.equals(COMMA_CONSTANT);
    }

    private final Calculator calculator;


    public Syntactic(final Calculator calculator) {
        this.calculator = calculator;
    }

    /**
     * <p>calculate expr value</p>
     *
     * @param formula formula
     * @throws CompilerException formula error
     * @return Constant
     */
    public Constant getFormulaValue(final String formula) throws CompilerException {

        if (formula == null) {
            return Constant.build(ConstantType.NULL, null);
        }

        final Stack<Constant> operators = new Stack<Constant>();
        final Stack<Constant> numbers = new Stack<Constant>();

        Constant constant = null;

        List<Constant> words = Lexicon.parse(formula);
        words.add(END_CONSTANT);
        for (Constant word : words) {
            if (word.isSYMBOL()) {
                List<Constant> constants = new ArrayList<Constant>();
                while(!operators.isEmpty() && isCalculated(word, operators.peek())) {
                    if (operators.peek().equals(LEFT_PARENTHESIS_CONSTANT)) {
                        if (!RIGHT_PARENTHESIS_CONSTANT.equals(word)) {
                            throw new CompilerException("formula parenthesis error: " + formula);
                        }
                        operators.pop();
                        if (!operators.isEmpty() && operators.peek().isFUNCTION()) {
                            if (constant != null) {
                                constants.add(constant);
                            }
                            Collections.reverse(constants);
                            constant = calculator.execute(operators.pop().getValue().toString(), constants);
                        }
                        break;
                    } else if (operators.peek().equals(COMMA_CONSTANT)) {
                        operators.pop();
                        constants.add(constant);
                        constant = numbers.pop();
                    } else if (operators.peek().equals(GREATER_THAN_CONSTANT)) {
                        operators.pop();
                        constant = calculator.gt(numbers.pop(), constant);
                    } else if (operators.peek().equals(LESS_THAN_CONSTANT)) {
                        operators.pop();
                        constant = calculator.lt(numbers.pop(), constant);
                    } else if (operators.peek().equals(EQUAL_CONSTANT)) {
                        operators.pop();
                        constant = calculator.eq(numbers.pop(), constant);
                    } else if (operators.peek().equals(GREATER_EQUAL_CONSTANT)) {
                        operators.pop();
                        constant = calculator.ge(numbers.pop(), constant);
                    } else if (operators.peek().equals(LESS_EQUAL_CONSTANT)) {
                        operators.pop();
                        constant = calculator.le(numbers.pop(), constant);
                    } else if (operators.peek().equals(PLUS_CONSTANT)) {
                        operators.pop();
                        constant = calculator.plus(numbers.pop(), constant);
                    } else if (operators.peek().equals(MINUS_CONSTANT)) {
                        operators.pop();
                        constant = calculator.minus(numbers.pop(), constant);
                    } else if (operators.peek().equals(TIMES_CONSTANT)) {
                        operators.pop();
                        constant = calculator.times(numbers.pop(), constant);
                    } else if (operators.peek().equals(DIVISION_CONSTANT)) {
                        operators.pop();
                        constant = calculator.division(numbers.pop(), constant);
                    } else if (operators.peek().equals(MODULO_CONSTANT)) {
                        operators.pop();
                        constant = calculator.modulo(numbers.pop(), constant);
                    } else if (operators.peek().equals(POW_CONSTANT)) {
                        operators.pop();
                        constant = calculator.pow(numbers.pop(), constant);
                    } else if (operators.peek().equals(AND_CONSTANT)) {
                        operators.pop();
                        constant = calculator.and(numbers.pop(), constant);
                    } else if (operators.peek().equals(OR_CONSTANT)) {
                        operators.pop();
                        constant = calculator.or(numbers.pop(), constant);
                    } else {
                        throw new CompilerException(
                                "operators error " + operators.peek());
                    }
                }
                if (!word.equals(RIGHT_PARENTHESIS_CONSTANT)) {
                    operators.push(word);
                    if (constant != null) {
                        numbers.push(constant);
                        constant = null;
                    }
                }
            } else if (word.isFUNCTION()) {
                operators.push(word);
                if (constant != null) {
                    numbers.push(constant);
                    constant = null;
                }
            } else if (word.isVARIATE()) {
                if (constant != null) {
                    numbers.push(constant);
                }
                constant = calculator.getValue(word.getValue().toString());
            } else {
                if (constant != null) {
                    numbers.push(constant);
                }
                constant = word;
            }
        }

        if (operators.size() == 1 && numbers.size() == 1 ) {
            return numbers.pop();
        }

        throw new CompilerException(formula);
    }
}