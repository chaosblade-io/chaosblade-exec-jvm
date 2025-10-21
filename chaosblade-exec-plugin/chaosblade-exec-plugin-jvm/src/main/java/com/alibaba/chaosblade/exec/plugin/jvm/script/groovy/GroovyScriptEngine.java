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

package com.alibaba.chaosblade.exec.plugin.jvm.script.groovy;

import com.alibaba.chaosblade.exec.plugin.jvm.script.base.CompiledScript;
import com.alibaba.chaosblade.exec.plugin.jvm.script.base.ExecutableScript;
import com.alibaba.chaosblade.exec.plugin.jvm.script.base.ScriptEngine;
import com.alibaba.chaosblade.exec.plugin.jvm.script.base.ScriptException;
import groovy.lang.Binding;
import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyCodeSource;
import groovy.lang.Script;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.codehaus.groovy.ast.ClassCodeExpressionTransformer;
import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.expr.ConstantExpression;
import org.codehaus.groovy.ast.expr.Expression;
import org.codehaus.groovy.classgen.GeneratorContext;
import org.codehaus.groovy.control.CompilationFailedException;
import org.codehaus.groovy.control.CompilePhase;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.control.MultipleCompilationErrorsException;
import org.codehaus.groovy.control.SourceUnit;
import org.codehaus.groovy.control.customizers.CompilationCustomizer;
import org.codehaus.groovy.control.customizers.ImportCustomizer;
import org.codehaus.groovy.control.messages.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** @author RinaisSuper */
public class GroovyScriptEngine implements ScriptEngine {

  public static final String UNTRUSTED_CODEBASE = "/untrusted";
  public static final String GROOVY_INDY_SETTING_NAME = "indy";
  private static final Logger LOGGER = LoggerFactory.getLogger(GroovyScriptEngine.class);
  private static String NAME = "Groovy";

  @Override
  public String getLanguage() {
    return NAME;
  }

  @Override
  public Object compile(
      com.alibaba.chaosblade.exec.plugin.jvm.script.base.Script script,
      ClassLoader classLoader,
      Map<String, String> configs) {
    String className = "groovy_script_" + script.getId();
    GroovyCodeSource codeSource =
        new GroovyCodeSource(script.getContent(), className, UNTRUSTED_CODEBASE);
    codeSource.setCachable(true);
    CompilerConfiguration compilerConfiguration =
        new CompilerConfiguration()
            .addCompilationCustomizers(new ImportCustomizer().addStaticStars("java.lang.Math"))
            .addCompilationCustomizers(new GroovyBigDecimalTransformer(CompilePhase.CONVERSION));
    compilerConfiguration.getOptimizationOptions().put(GROOVY_INDY_SETTING_NAME, true);
    GroovyClassLoader groovyClassLoader = new GroovyClassLoader(classLoader, compilerConfiguration);
    try {
      return groovyClassLoader.parseClass(script.getContent());
    } catch (Exception ex) {
      throw convertToScriptException("Compile script failed:" + className, script.getId(), ex);
    }
  }

  @Override
  public ExecutableScript execute(CompiledScript compiledScript, Map<String, Object> vars) {
    try {
      Map<String, Object> allVars = new HashMap<String, Object>();
      if (vars != null) {
        allVars.putAll(vars);
      }
      return new GroovyScript(compiledScript, createScript(compiledScript.getCompiled(), allVars));
    } catch (Exception e) {
      throw convertToScriptException(
          "Failed to build executable script", compiledScript.getId(), e);
    }
  }

  private groovy.lang.Script createScript(Object compiledScript, Map<String, Object> vars)
      throws Exception {
    Class<?> scriptClass = (Class<?>) compiledScript;
    Script scriptObject = (Script) scriptClass.getConstructor().newInstance();
    Binding binding = new Binding();
    binding.getVariables().putAll(vars);
    scriptObject.setBinding(binding);
    return scriptObject;
  }

  private ScriptException convertToScriptException(
      String message, String scriptId, Throwable cause) {
    if (cause instanceof ScriptException) {
      return (ScriptException) cause;
    }
    List<String> stack = new ArrayList<String>();
    if (cause instanceof MultipleCompilationErrorsException) {
      @SuppressWarnings({"unchecked"})
      List<Message> errors =
          (List<Message>)
              ((MultipleCompilationErrorsException) cause).getErrorCollector().getErrors();
      StringWriter writer = null;
      for (Message error : errors) {
        try {
          writer = new StringWriter();
          error.write(new PrintWriter(writer));
          stack.add(writer.toString());
        } finally {
          if (writer != null) {
            try {
              writer.close();
            } catch (IOException e) {
            }
          }
        }
      }
    } else if (cause instanceof CompilationFailedException) {
      CompilationFailedException error = (CompilationFailedException) cause;
      stack.add(error.getMessage());
    }
    throw new ScriptException(message, cause, stack, scriptId, NAME);
  }

  private class GroovyBigDecimalTransformer extends CompilationCustomizer {

    private GroovyBigDecimalTransformer(CompilePhase phase) {
      super(phase);
    }

    @Override
    public void call(
        final SourceUnit source, final GeneratorContext context, final ClassNode classNode)
        throws CompilationFailedException {
      new BigDecimalExpressionTransformer(source).visitClass(classNode);
    }
  }

  private class BigDecimalExpressionTransformer extends ClassCodeExpressionTransformer {

    private final SourceUnit source;

    private BigDecimalExpressionTransformer(SourceUnit source) {
      this.source = source;
    }

    @Override
    protected SourceUnit getSourceUnit() {
      return this.source;
    }

    @Override
    public Expression transform(Expression expr) {
      Expression newExpr = expr;
      if (expr instanceof ConstantExpression) {
        ConstantExpression constExpr = (ConstantExpression) expr;
        Object val = constExpr.getValue();
        if (val != null && val instanceof BigDecimal) {
          newExpr = new ConstantExpression(((BigDecimal) val).doubleValue());
        }
      }
      return super.transform(newExpr);
    }
  }
}
