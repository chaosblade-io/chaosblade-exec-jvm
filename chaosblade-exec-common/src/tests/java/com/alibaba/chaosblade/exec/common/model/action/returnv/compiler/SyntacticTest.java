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

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static com.alibaba.chaosblade.exec.common.model.action.returnv.compiler.ConstantType.*;
import static org.junit.Assert.*;

public class SyntacticTest {

    @Test
    public void test() throws CompilerException {
        final Map<String, Object> json = new HashMap<String, Object>();
        json.put("a", 1);
        json.put("b", 2);
        json.put("lon_1", 1);
        json.put("lon_2", 2);
        json.put("lon_3", 3);
        json.put("lat_1", 1);
        json.put("lat_2", 2);
        json.put("lat_3", 3);
        json.put("base_length_12", 53121.441588996175);
        json.put("base_length_13", 64872.86350974114);
        json.put("base_length_23", 34123.96353497961);
        json.put("triangle", "XJQQ");
        json.put("azimuth", "312.198:234.831:21.443:79.07:0.0:133.567:123.199:50.737:321.319:174.162|0.0:0.0:0.0:0.0:0.0:0.0:0.0:0.0|0.0:0.0:0.0:0.0:0.0:0.0:0.0|0.0:0.0:0.0:0.0:0.0:0.0:0.0:0.0:0.0:0.0:0.0:0.0:0.0:0.0:0.0:0.0:0.0");
        final Calculator calculator = new Calculator()  {

            @Override
            public Constant getValue(String name) throws CompilerException {
                if (!json.containsKey(name) || json.get(name) == null) {
                    return Constant.build(NULL, null);
                } else if (json.get(name) instanceof Number) {
                    return Constant.build(NUMERIC, ((Number) json.get(name)).doubleValue());
                } else if (json.get(name) instanceof String) {
                    return Constant.build(STRING, json.get(name).toString());
                } else if (json.get(name) instanceof Boolean) {
                    return Constant.build(BOOLEAN, Boolean.parseBoolean(json.get(name).toString()));
                }
                return Constant.build(NULL, null);
            }

            @Override
            public boolean isVariate(String name) {
                return json.containsKey(name);
            }
        };

        final Syntactic syntactic = new Syntactic(calculator);
        assertTrue(syntactic.getFormulaValue("match('[\\d|\\.|:]*', azimuths, -1)").isNULL());

        assertTrue(syntactic.getFormulaValue("match('[\\d|\\.|:]*', azimuth, -1)").getAsArray().length > 1);

        assertTrue(syntactic.getFormulaValue("nonNull(lat_2) and nonNull(lat_2)").getAsBoolean());

        assertFalse(syntactic.getFormulaValue("nonNull(abc)").getAsBoolean());
        assertTrue(syntactic.getFormulaValue("nonNull(lat_2)").getAsBoolean());

        assertEquals(syntactic
                .getFormulaValue("match('[0-9|.]+', 'RTK_BETA_40.21_116.48', -1)")
                .getAsArray()[0].toString(), "40.21");

        assertEquals(syntactic
                        .getFormulaValue("numeric(match('[0-9|.]+', 'RTK_BETA_40.21_116.48', 0))")
                        .getAsNumber().doubleValue(),
                40.21, 1e-5);

        assertEquals(syntactic
                        .getFormulaValue("if(1>2, 1, 4)")
                        .getAsNumber().intValue(),
                4);

        assertEquals(syntactic
                        .getFormulaValue("if(1<2, 1, 4)")
                        .getAsNumber().intValue(),
                1);

        assertEquals(syntactic
                        .getFormulaValue("match('(?!' + triangle + ')[A-Z]{4}', 'XJML_XJQQ_XJSS;XJML_XJQR_XJQT;XJML_XJQQ_XJQT;XJQQ_XJQT_XJSE', 1)")
                        .getAsString(),
                "XJSS");

        assertEquals(syntactic.getFormulaValue("acos((base_length_12^2+base_length_13^2+base_length_23^2-2*min(base_length_12,base_length_13,base_length_23)^2)" +
                        "/(2*base_length_12*base_length_13*base_length_23/min(base_length_12,base_length_13,base_length_23)))*180/3.1415926").getAsNumber().doubleValue(),
                31.66988760241305, 1e-5);

        assertEquals(syntactic.getFormulaValue("base_length_12^2+base_length_13^2+base_length_23^2-min(base_length_12,base_length_13,base_length_23)^2").getAsNumber().doubleValue(),
                64872.86350974114*64872.86350974114+53121.441588996175*53121.441588996175, 1e-5);
        assertEquals(syntactic.getFormulaValue("2*base_length_12*base_length_13*base_length_23/min(base_length_12,base_length_13,base_length_23)").getAsNumber().doubleValue(),
                64872.86350974114*53121.441588996175*2, 1e-5);

        assertEquals(syntactic.getFormulaValue("((lon_1-lon_2)^2)^0.5 + ((lon_1-lon_3)^2)^0.5").getAsNumber().doubleValue(),
                3, 1e-5);

        assertEquals(syntactic.getFormulaValue("numeric('2.0')^numeric('0.5')").getAsNumber().doubleValue(),
                1.4142135623730951, 1e-5);

        assertEquals(syntactic.getFormulaValue("2.0^numeric('0.5')").getAsNumber().doubleValue(),
                1.4142135623730951, 1e-5);

        assertEquals(syntactic.getFormulaValue("2.0^0.5").getAsNumber().doubleValue(),
                1.4142135623730951, 1e-5);

        assertEquals(syntactic
                        .getFormulaValue("match('[A-Z]+', 'XJML_XJQQ_XJSS;XJML_XJQR_XJQT;XJML_XJQQ_XJQT;XJQQ_XJQT_XJSE', 1)")
                        .getAsString(),
                "XJQQ");

        assertTrue(syntactic.getFormulaValue("random()^2 + random()^2 < 2").getAsBoolean());

        assertEquals(syntactic.getFormulaValue("min(10, 20.555) + 10").getAsNumber().doubleValue(),
                20.0, 1e-5);
        assertEquals(syntactic.getFormulaValue("100%3 + b*b").getAsNumber().doubleValue(),
                5.0, 1e-5);
        assertEquals(syntactic.getFormulaValue("a*a + b*b").getAsNumber().doubleValue(),
                5.0, 1e-5);
        assertEquals(syntactic.getFormulaValue("(a^3 + b)^3").getAsNumber().doubleValue(),
                27.0, 1e-5);


        assertEquals(syntactic.getFormulaValue("10 + 10").getAsNumber().doubleValue(),
                20.0, 1e-5);
        assertEquals(syntactic.getFormulaValue("max(10, 20.555) + 10").getAsNumber().doubleValue(),
                30.555, 1e-5);
        assertEquals(syntactic.getFormulaValue("max(10, min(0, 20*20.555)) + 10").getAsNumber().doubleValue(),
                20, 1e-5);

    }

    @Test
    public void test1() throws CompilerException {
        final Calculator calculator = new Calculator() {
            @Override
            public Constant getValue(String name) throws CompilerException {
                return null;
            }

            @Override
            public boolean isVariate(String name) {
                return false;
            }
        };

        final Syntactic syntactic = new Syntactic(calculator);
        //System.out.println(syntactic.getFormulaValue("random()"));
        System.out.println(syntactic.getFormulaValue("(random()-0.5)*2"));

    }
}
