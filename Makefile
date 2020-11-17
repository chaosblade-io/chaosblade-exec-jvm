.PHONY: build clean

BLADE_SRC_ROOT=`pwd`
UNAME := $(shell uname)

ifeq ($(BLADE_VERSION), )
	BLADE_VERSION=0.9.0
endif

PLUGINS_PATH=plugins
TARGET_PATH=target
BUILD_TARGET=build-target
BUILD_TARGET_DIR_NAME=chaosblade-$(BLADE_VERSION)
BUILD_TARGET_PKG_DIR=$(BUILD_TARGET)/chaosblade-$(BLADE_VERSION)
BUILD_TARGET_BIN=$(BUILD_TARGET_PKG_DIR)/bin
BUILD_TARGET_LIB=$(BUILD_TARGET_PKG_DIR)/lib
BUILD_TARGET_YAML=$(BUILD_TARGET_PKG_DIR)/yaml
# cache downloaded file
BUILD_TARGET_CACHE=$(BUILD_TARGET)/cache
# yaml file name
JVM_YAML_FILE_NAME=chaosblade-jvm-spec-$(BLADE_VERSION).yaml
# agent file name
JVM_AGENT_FILE_NAME=chaosblade-java-agent-$(BLADE_VERSION).jar

# oss url
BLADE_OSS_URL=https://chaosblade.oss-cn-hangzhou.aliyuncs.com/agent/release
# sandbox
JVM_SANDBOX_VERSION=1.3.1
JVM_SANDBOX_NAME=sandbox-$(JVM_SANDBOX_VERSION)-bin.zip
JVM_SANDBOX_OSS_URL=https://ompc.oss-cn-hangzhou.aliyuncs.com/jvm-sandbox/release/$(JVM_SANDBOX_NAME)
JVM_SANDBOX_DEST_PATH=$(BUILD_TARGET_CACHE)/$(JVM_SANDBOX_NAME)
JVM_SANDBOX_TARGET_PATH=$(BUILD_TARGET_LIB)/sandbox
# used to java agent attachp
BLADE_JAVA_TOOLS_JAR_NAME=tools.jar
BLADE_JAVA_TOOLS_JAR_DEST_PATH=$(BUILD_TARGET_CACHE)/$(BLADE_JAVA_TOOLS_JAR_NAME)
BLADE_JAVA_TOOLS_JAR_DOWNLOAD_URL=$(BLADE_OSS_URL)/$(BLADE_JAVA_TOOLS_JAR_NAME)

build: pre_build build_java
	cp $(PLUGINS_PATH)/$(JVM_YAML_FILE_NAME) $(BUILD_TARGET_YAML)/
	cp $(TARGET_PATH)/$(JVM_AGENT_FILE_NAME) $(JVM_SANDBOX_TARGET_PATH)/module/

# download sandbox for java chaos experiment
download_sandbox:
ifneq ($(BUILD_TARGET_CACHE), $(wildcard $(BUILD_TARGET_CACHE)))
	mkdir -p $(BUILD_TARGET_CACHE)
endif
ifneq ($(JVM_SANDBOX_DEST_PATH), $(wildcard $(JVM_SANDBOX_DEST_PATH)))
	wget "$(JVM_SANDBOX_OSS_URL)" -O $(JVM_SANDBOX_DEST_PATH)
endif
ifneq ($(BLADE_JAVA_TOOLS_JAR_DEST_PATH), $(wildcard $(BLADE_JAVA_TOOLS_JAR_DEST_PATH)))
	wget "$(BLADE_JAVA_TOOLS_JAR_DOWNLOAD_URL)" -O $(BLADE_JAVA_TOOLS_JAR_DEST_PATH)
endif

pre_build: download_sandbox
	rm -rf $(BUILD_TARGET_PKG_DIR)
	mkdir -p $(BUILD_TARGET_BIN) $(BUILD_TARGET_LIB) $(BUILD_TARGET_YAML)
	# unzip jvm-sandbox
	unzip $(JVM_SANDBOX_DEST_PATH) -d $(BUILD_TARGET_LIB)
	# cp tools.jar to lib/sandbox
	cp $(BLADE_JAVA_TOOLS_JAR_DEST_PATH) $(JVM_SANDBOX_TARGET_PATH)

build_java:
	rm -rf $(PLUGINS_PATH)
	mkdir -p $(PLUGINS_PATH)
	mvn clean package -Dmaven.test.skip=true -U
	cp $(BLADE_SRC_ROOT)/chaosblade-exec-plugin/chaosblade-exec-plugin-*/target/chaosblade-exec-plugin-*.jar $(PLUGINS_PATH)
	mvn clean assembly:assembly -Dmaven.test.skip=true -U

# test
test:
	mvn clean test -U
# clean all build result
clean:
	rm -rf $(BUILD_TARGET)

all: build test
