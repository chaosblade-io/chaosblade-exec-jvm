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

package com.alibaba.chaosblade.exec.plugin.jvm.script.java;

import com.alibaba.chaosblade.exec.plugin.jvm.script.base.CompiledScript;
import com.alibaba.chaosblade.exec.plugin.jvm.script.base.ExecutableScript;
import com.alibaba.chaosblade.exec.plugin.jvm.script.base.Script;
import com.alibaba.chaosblade.exec.plugin.jvm.script.base.ScriptEngine;
import com.alibaba.chaosblade.exec.plugin.jvm.script.base.ScriptException;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.net.JarURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.regex.Pattern;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.NestingKind;
import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.JavaFileObject.Kind;
import javax.tools.SimpleJavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.StandardLocation;
import javax.tools.ToolProvider;

public class JavaCodeScriptEngine implements ScriptEngine {

  private static final Charset CHARSET_UTF8 = Charset.forName("UTF-8");
  private static final Pattern PACKAGE_REGREX = Pattern.compile("(^|\n)package\\s+(.*);");
  private static final String EMPTY_STR = "";
  private static String NAME = "Java";
  private static String PACKAGE_NAME = "com.taobao.csp.monkeyking.script.java.source";

  private static void generateDiagnosticReport(
      DiagnosticCollector<JavaFileObject> collector, StringBuilder reporter) throws IOException {
    List<Diagnostic<? extends JavaFileObject>> diagnostics = collector.getDiagnostics();
    for (Diagnostic<? extends JavaFileObject> diagnostic : diagnostics) {
      JavaFileObject source = diagnostic.getSource();
      if (source != null) {
        reporter.append("Source: ").append(source.getName()).append('\n');
        reporter
            .append("Line ")
            .append(diagnostic.getLineNumber())
            .append(": ")
            .append(diagnostic.getMessage(Locale.ENGLISH))
            .append('\n');
        CharSequence content = source.getCharContent(true);
        BufferedReader reader = new BufferedReader(new StringReader(content.toString()));
        int i = 1;
        String line;
        while ((line = reader.readLine()) != null) {
          reporter.append(i).append('\t').append(line).append('\n');
          ++i;
        }
      } else {
        reporter.append("Source: (null)\n");
        reporter
            .append("Line ")
            .append(diagnostic.getLineNumber())
            .append(": ")
            .append(diagnostic.getMessage(Locale.ENGLISH))
            .append('\n');
      }
      reporter.append('\n');
    }
  }

  @Override
  public String getLanguage() {
    return NAME;
  }

  @Override
  public Object compile(Script script, ClassLoader classLoader, Map<String, String> configs) {
    String className = JavaUtils.getClassName(script.getContent());
    if (className.isEmpty()) {
      throw new ScriptException(
          "Not found class name in script content,class name must in alpha format");
    }
    String completeClassName = PACKAGE_NAME + "." + className;
    return compileClass(
        classLoader, completeClassName, script.getId(), replacePackage(script.getContent()));
  }

  /**
   * Replace user package
   *
   * @param content
   * @return
   */
  private String replacePackage(String content) {
    String newContent = PACKAGE_REGREX.matcher(content).replaceFirst(EMPTY_STR).trim();
    return "package " + PACKAGE_NAME + ";\n" + newContent;
  }

  @Override
  public ExecutableScript execute(CompiledScript compiledScript, Map<String, Object> vars) {
    Class<?> scriptClass = (Class<?>) compiledScript.getCompiled();
    try {
      Object instance = scriptClass.newInstance();
      return new JavaExecutableScript(instance, vars);
    } catch (Exception exception) {
      throw convertToScriptException(
          "Failed to build executable script", compiledScript.getId(), exception);
    }
  }

  private Class compileClass(
      ClassLoader classLoader, String className, String scriptId, String content) {
    JavaCompiler javaCompiler = ToolProvider.getSystemJavaCompiler();
    if (javaCompiler == null) {
      throw new ScriptException("Not found system java compile");
    }
    DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();
    JavaFileObject javaFileObject = new InputStringJavaFileObject(className, content);
    StandardJavaFileManager standardFileManager =
        javaCompiler.getStandardFileManager(null, null, CHARSET_UTF8);
    InMemoryJavaFileManager fileManager =
        new InMemoryJavaFileManager(classLoader, standardFileManager);
    JavaCompiler.CompilationTask compilationTask =
        javaCompiler.getTask(
            null,
            fileManager,
            diagnostics,
            Arrays.asList("-XDuseUnsharedTable"),
            null,
            Arrays.asList(javaFileObject));
    if (Boolean.TRUE.equals(compilationTask.call())) {
      try {
        if (classLoader instanceof URLClassLoader) {
          URLClassLoader urlClassLoader = (URLClassLoader) classLoader;
          return new CompiledClassLoader(
                  classLoader, fileManager.getOutputs(), urlClassLoader.getURLs())
              .loadClass(className);
        } else {
          return new CompiledClassLoader(classLoader, fileManager.getOutputs(), new URL[0])
              .loadClass(className);
        }
      } catch (Exception ce) {
        throw convertToScriptException("compile class failed:" + className, scriptId, ce);
      }
    } else {
      StringBuilder reporter = new StringBuilder(1024);
      reporter.append("Compilation failed.\n");
      try {
        generateDiagnosticReport(diagnostics, reporter);
      } catch (IOException e) {
        reporter.append("io exception:" + e.getMessage());
      }
      throw new ScriptException(reporter.toString());
    }
  }

  private ScriptException convertToScriptException(
      String message, String scriptId, Throwable cause) {
    if (cause instanceof ScriptException) {
      return (ScriptException) cause;
    }
    List<String> stack = new ArrayList<String>();
    if (cause instanceof ClassNotFoundException) {
      stack.add(cause.getMessage());
    }
    throw new ScriptException(message, cause, stack, scriptId, NAME);
  }

  private static class CompiledClassLoader extends URLClassLoader {
    private final List<OutputClassJavaFileObject> files;

    private CompiledClassLoader(
        ClassLoader parent, List<OutputClassJavaFileObject> files, URL[] urls) {
      super(urls, parent);
      this.files = files;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
      Iterator<OutputClassJavaFileObject> itr = files.iterator();
      while (itr.hasNext()) {
        OutputClassJavaFileObject file = itr.next();
        if (file.getClassName().equals(name)) {
          itr.remove();
          byte[] bytes = file.getBytes();
          return super.defineClass(name, bytes, 0, bytes.length);
        }
      }
      return super.findClass(name);
    }
  }

  /** 将输出类保存在内存中 */
  private static class OutputClassJavaFileObject extends SimpleJavaFileObject {
    private final ByteArrayOutputStream outputStream;
    private final String className;

    protected OutputClassJavaFileObject(String className, Kind kind) {
      super(URI.create("mem:///" + className.replace('.', '/') + kind.extension), kind);
      this.className = className;
      outputStream = new ByteArrayOutputStream();
    }

    @Override
    public OutputStream openOutputStream() throws IOException {
      return outputStream;
    }

    public byte[] getBytes() {
      return outputStream.toByteArray();
    }

    public String getClassName() {
      return className;
    }
  }

  /** 支持从 ClassLoader 的资源中读取编译需要的类信息 */
  private static class InputClassJavaFileObject implements JavaFileObject {

    private final String binaryName;
    private final URI uri;

    protected InputClassJavaFileObject(String binaryName, URI uri) {
      this.binaryName = binaryName;
      this.uri = uri;
    }

    public String getBinaryName() {
      return binaryName;
    }

    @Override
    public URI toUri() {
      return uri;
    }

    @Override
    public InputStream openInputStream() throws IOException {
      return uri.toURL().openStream();
    }

    @Override
    public OutputStream openOutputStream() throws IOException {
      throw new UnsupportedOperationException();
    }

    @Override
    public String getName() {
      // 操作系统用 uri.getPath()，JAR 用 uri.getSchemeSpecificPart()
      return uri.getPath() == null ? uri.getSchemeSpecificPart() : uri.getPath();
    }

    @Override
    public Reader openReader(boolean ignoreEncodingErrors) throws IOException {
      throw new UnsupportedOperationException();
    }

    @Override
    public CharSequence getCharContent(boolean ignoreEncodingErrors) throws IOException {
      throw new UnsupportedOperationException();
    }

    @Override
    public Writer openWriter() throws IOException {
      throw new UnsupportedOperationException();
    }

    @Override
    public long getLastModified() {
      return 0;
    }

    @Override
    public boolean delete() {
      throw new UnsupportedOperationException();
    }

    @Override
    public Kind getKind() {
      return Kind.CLASS;
    }

    @Override
    public boolean isNameCompatible(String simpleName, Kind kind) {
      String baseName = simpleName + kind.extension;
      String name = getName();
      return kind.equals(getKind()) && (baseName.equals(name) || name.endsWith("/" + baseName));
    }

    @Override
    public NestingKind getNestingKind() {
      throw new UnsupportedOperationException();
    }

    @Override
    public Modifier getAccessLevel() {
      return null;
    }

    @Override
    public String toString() {
      return "InputClassJavaFileObject[uri=" + uri + ", binaryName=" + binaryName + "]";
    }
  }

  private static class InMemoryJavaFileManager extends ForwardingJavaFileManager<JavaFileManager> {

    private static final String CLASS_FILE_EXTENSION = ".class";

    private final ClassLoader classLoader;
    private final List<OutputClassJavaFileObject> outputFiles;

    protected InMemoryJavaFileManager(ClassLoader loader, JavaFileManager fileManager) {
      super(fileManager);
      classLoader = loader;
      outputFiles = new ArrayList<OutputClassJavaFileObject>();
    }

    // --------------------------------- Output ---------------------------------
    @Override
    public JavaFileObject getJavaFileForOutput(
        Location location, String className, JavaFileObject.Kind kind, FileObject sibling)
        throws IOException {
      OutputClassJavaFileObject file = new OutputClassJavaFileObject(className, kind);
      outputFiles.add(file);
      return file;
    }

    public List<OutputClassJavaFileObject> getOutputs() {
      return outputFiles;
    }

    // --------------------------------- Input ---------------------------------
    @Override
    public boolean hasLocation(Location location) {
      return location == StandardLocation.CLASS_PATH
          || location == StandardLocation.PLATFORM_CLASS_PATH;
    }

    @Override
    public String inferBinaryName(Location location, JavaFileObject file) {
      if (file instanceof InputClassJavaFileObject) {
        return ((InputClassJavaFileObject) file).getBinaryName();
      } else {
        return super.inferBinaryName(location, file);
      }
    }

    @Override
    public Iterable<JavaFileObject> list(
        Location location, String packageName, Set<Kind> kinds, boolean recurse)
        throws IOException {
      if (location.getName().contains("SYSTEM_MODULES")
          && location.getName().contains("java.base")) {
        return super.list(location, packageName, kinds, recurse);
      } else if (location == StandardLocation.PLATFORM_CLASS_PATH) {
        return super.list(location, packageName, kinds, recurse);
      } else if (location == StandardLocation.CLASS_PATH
          && kinds.contains(JavaFileObject.Kind.CLASS)) {
        if (packageName.startsWith("java")) {
          return super.list(location, packageName, kinds, recurse);
        } else {
          return find(packageName);
        }
      }
      return Collections.emptyList();
    }

    public List<JavaFileObject> find(String packageName) throws IOException {
      String javaPackageName = packageName.replaceAll("\\.", "/");

      List<JavaFileObject> result = new ArrayList<JavaFileObject>();

      // 从 classLoader 里面查找编译需要的类
      Enumeration<URL> urlEnumeration = classLoader.getResources(javaPackageName);
      while (urlEnumeration.hasMoreElements()) {
        URL packageFolderURL = urlEnumeration.nextElement();
        result.addAll(listUnder(packageName, packageFolderURL));
      }

      return result;
    }

    private Collection<JavaFileObject> listUnder(String packageName, URL packageFolderURL) {
      File directory = new File(packageFolderURL.getFile());
      if (directory.isDirectory()) {
        return processDir(packageName, directory);
      } else {
        return processJar(packageFolderURL);
      }
    }

    private List<JavaFileObject> processJar(URL packageFolderURL) {
      List<JavaFileObject> result = new ArrayList<JavaFileObject>();
      try {
        String externalURL = packageFolderURL.toExternalForm();
        int laste = externalURL.lastIndexOf('!');
        String jarUri = laste <= 0 ? externalURL : externalURL.substring(0, laste);

        JarURLConnection jarConn = (JarURLConnection) packageFolderURL.openConnection();
        String rootEntryName = jarConn.getEntryName();
        if (rootEntryName == null) {
          return result;
        }
        int rootEnd = rootEntryName.length() + 1;

        Enumeration<JarEntry> entryEnum = jarConn.getJarFile().entries();
        while (entryEnum.hasMoreElements()) {
          JarEntry jarEntry = entryEnum.nextElement();
          String name = jarEntry.getName();
          if (name.startsWith(rootEntryName)
              && name.indexOf('/', rootEnd) == -1
              && name.endsWith(CLASS_FILE_EXTENSION)) {
            URI uri = URI.create(jarUri + "!/" + name);
            String binaryName = name.replace('/', '.');
            binaryName = binaryName.replaceAll(CLASS_FILE_EXTENSION + "$", "");

            result.add(new InputClassJavaFileObject(binaryName, uri));
          }
        }
      } catch (Exception e) {
        throw new RuntimeException("Fail to open " + packageFolderURL + " as a jar file", e);
      }
      return result;
    }

    private List<JavaFileObject> processDir(String packageName, File directory) {
      List<JavaFileObject> result = new ArrayList<JavaFileObject>();

      File[] childFiles = directory.listFiles();
      for (File childFile : childFiles) {
        if (childFile.isFile()) {
          if (childFile.getName().endsWith(CLASS_FILE_EXTENSION)) {
            String binaryName = packageName + "." + childFile.getName();
            binaryName = binaryName.replaceAll(CLASS_FILE_EXTENSION + "$", "");

            result.add(new InputClassJavaFileObject(binaryName, childFile.toURI()));
          }
        }
      }

      return result;
    }
  }

  /** 支持从 String 中读取源码内容用于编译 */
  private static class InputStringJavaFileObject extends SimpleJavaFileObject {
    private final String code;

    public InputStringJavaFileObject(String name, String code) {
      super(URI.create("string:///" + name.replace('.', '/') + Kind.SOURCE.extension), Kind.SOURCE);
      this.code = code;
    }

    @Override
    public CharSequence getCharContent(boolean ignoreEncodingErrors) throws IOException {
      return code;
    }
  }
}
