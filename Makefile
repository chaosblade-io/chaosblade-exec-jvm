# ChaosBlade JVM Executor Makefile
.PHONY: build clean test all help download-sandbox pre-build build-java

# =============================================================================
# 配置变量
# =============================================================================
BLADE_VERSION ?= 1.7.5
BLADE_SRC_ROOT := $(shell pwd)

# =============================================================================
# 路径配置
# =============================================================================
PLUGINS_PATH := plugins
TARGET_PATH := target
BUILD_TARGET := build-target
BUILD_TARGET_PKG_DIR := $(BUILD_TARGET)/chaosblade-$(BLADE_VERSION)
BUILD_TARGET_CACHE := $(BUILD_TARGET)/cache

# 构建目标子目录
BUILD_TARGET_BIN := $(BUILD_TARGET_PKG_DIR)/bin
BUILD_TARGET_LIB := $(BUILD_TARGET_PKG_DIR)/lib
BUILD_TARGET_YAML := $(BUILD_TARGET_PKG_DIR)/yaml

# =============================================================================
# 文件名称
# =============================================================================
JVM_YAML_FILE_NAME := chaosblade-jvm-spec-$(BLADE_VERSION).yaml
JVM_AGENT_FILE_NAME := chaosblade-java-agent-$(BLADE_VERSION).jar

# =============================================================================
# 外部资源配置
# =============================================================================
BLADE_OSS_URL := https://chaosblade.oss-cn-hangzhou.aliyuncs.com/agent/release
JVM_SANDBOX_VERSION := 1.4.0
JVM_SANDBOX_NAME := sandbox-$(JVM_SANDBOX_VERSION)-bin.zip
JVM_SANDBOX_OSS_URL := https://chaosblade.oss-cn-hangzhou.aliyuncs.com/agent/github/sandbox/$(JVM_SANDBOX_NAME)
JVM_SANDBOX_DEST_PATH := $(BUILD_TARGET_CACHE)/$(JVM_SANDBOX_NAME)
JVM_SANDBOX_TARGET_PATH := $(BUILD_TARGET_LIB)/sandbox

# Java工具配置
BLADE_JAVA_TOOLS_JAR_NAME := tools.jar
BLADE_JAVA_TOOLS_JAR_DEST_PATH := $(BUILD_TARGET_CACHE)/$(BLADE_JAVA_TOOLS_JAR_NAME)
BLADE_JAVA_TOOLS_JAR_DOWNLOAD_URL := $(BLADE_OSS_URL)/$(BLADE_JAVA_TOOLS_JAR_NAME)

# =============================================================================
# 主要目标
# =============================================================================

# 默认目标：构建完整项目
all: build test

# 构建项目
# 产物归一化：maven 产物名由 pom 版本决定，与产品版本 BLADE_VERSION 无关。
# 此处按 glob 定位实际产物并断言恰好一个，强制重命名为 BLADE_VERSION 名，
# 保证「产物名 == BLADE_VERSION」由构造成立（单组件 bump 时 pom 版本不变也能正确产出）。
build: pre-build build-java
	@echo "复制构建产物（归一化到 BLADE_VERSION=$(BLADE_VERSION)）..."
	@set -e; \
	set -- $(PLUGINS_PATH)/chaosblade-jvm-spec-*.yaml; \
	if [ "$$#" -ne 1 ] || [ ! -f "$$1" ]; then \
		echo "错误: $(PLUGINS_PATH) 下应恰好有 1 个 chaosblade-jvm-spec-*.yaml（匹配数: $$#，首个匹配: $$1）"; \
		exit 1; \
	fi; \
	cp "$$1" $(BUILD_TARGET_YAML)/$(JVM_YAML_FILE_NAME); \
	echo "已归一化: $$1 -> $(JVM_YAML_FILE_NAME)"; \
	set -- $(TARGET_PATH)/chaosblade-java-agent-*.jar; \
	if [ "$$#" -ne 1 ] || [ ! -f "$$1" ]; then \
		echo "错误: $(TARGET_PATH) 下应恰好有 1 个 chaosblade-java-agent-*.jar（匹配数: $$#，首个匹配: $$1）"; \
		exit 1; \
	fi; \
	cp "$$1" $(JVM_SANDBOX_TARGET_PATH)/module/$(JVM_AGENT_FILE_NAME); \
	echo "已归一化: $$1 -> $(JVM_AGENT_FILE_NAME)"
	@echo "构建完成: $(BUILD_TARGET_PKG_DIR)"

# 运行测试
test:
	@echo "运行测试..."
	mvn clean test -U

# 清理构建产物
clean:
	@echo "清理构建产物..."
	rm -rf $(BUILD_TARGET)
	@echo "清理完成"

# 显示帮助信息
help:
	@echo "ChaosBlade JVM Executor 构建工具"
	@echo ""
	@echo "可用目标:"
	@echo "  build        - 构建完整项目"
	@echo "  test         - 运行测试"
	@echo "  clean        - 清理构建产物"
	@echo "  all          - 构建并测试 (默认)"
	@echo "  help         - 显示此帮助信息"
	@echo ""
	@echo "环境变量:"
	@echo "  BLADE_VERSION - 版本号 (默认: $(BLADE_VERSION))"

# =============================================================================
# 构建步骤
# =============================================================================

# 下载外部依赖
download-sandbox:
	@echo "检查并下载外部依赖..."
	@mkdir -p $(BUILD_TARGET_CACHE)
	@if [ ! -f $(JVM_SANDBOX_DEST_PATH) ]; then \
		echo "下载 JVM Sandbox..."; \
		curl -L "$(JVM_SANDBOX_OSS_URL)" -o $(JVM_SANDBOX_DEST_PATH); \
	fi
	@if [ ! -f $(BLADE_JAVA_TOOLS_JAR_DEST_PATH) ]; then \
		echo "下载 Java Tools..."; \
		curl -L "$(BLADE_JAVA_TOOLS_JAR_DOWNLOAD_URL)" -o $(BLADE_JAVA_TOOLS_JAR_DEST_PATH); \
	fi

# 预构建准备
pre-build: download-sandbox
	@echo "准备构建环境..."
	rm -rf $(BUILD_TARGET_PKG_DIR)
	mkdir -p $(BUILD_TARGET_BIN) $(BUILD_TARGET_LIB) $(BUILD_TARGET_YAML)
	@echo "解压 JVM Sandbox..."
	unzip -q $(JVM_SANDBOX_DEST_PATH) -d $(BUILD_TARGET_LIB)
	cp $(BLADE_JAVA_TOOLS_JAR_DEST_PATH) $(JVM_SANDBOX_TARGET_PATH)

# 构建Java项目
build-java:
	@echo "构建Java项目..."
	rm -rf $(PLUGINS_PATH)
	mkdir -p $(PLUGINS_PATH)
	@echo "编译插件..."
	mvn clean package -Dmaven.test.skip=true -U -q
	cp $(BLADE_SRC_ROOT)/chaosblade-exec-plugin/chaosblade-exec-plugin-*/target/chaosblade-exec-plugin-*.jar $(PLUGINS_PATH)
	@echo "打包最终产物..."
	mvn clean assembly:assembly -Dmaven.test.skip=true -U -q
