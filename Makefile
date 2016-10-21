SRC_DIR := ./src
BIN_DIR := ./bin

SRCS = $(shell find "$(SRC_DIR)" -iname '*.java')
CLASSES = $(shell find "$(SRC_DIR)" -iname '*.class')
.SUFFIXES: .java .class

# Applications
MKDIR_P ?= mkdir -p
JC ?= javac
JAR ?= jar
NJC ?= gcj
FIND ?= find

# Paths
TARGET_NATIVE := $(BIN_DIR)/ExBox
TARGET_BYTE := $(BIN_DIR)/ExBox.jar
MANIFEST_FILE := META-INF/MANIFEST.MF

# Compiler flags
JFLAGS = -cp "$(SRC_DIR)" -target 1.5 -Xlint

.java.class:
	$(JC) $(JFLAGS) "$*.java"

.PHONY: all exbox clean

%/:
	@echo "Create directory $*"
	$(MKDIR_P) "$*"

# Default is normal Java-bytecode compilation
all: $(TARGET_BYTE) ;

# Native compilation
native: $(TARGET_NATIVE) ;

.SECONDEXPANSION:
$(TARGET_BYTE): $(SRCS:.java=.class) | $$(@D)/
	$(JAR) -cfm "$(TARGET_BYTE)" "$(MANIFEST_FILE)" $(CLASSES)

.SECONDEXPANSION:
$(TARGET_NATIVE): $(SRCS) | $$(@D)/
	$(NJC) -v --main=ch.zhaw.ads.ExBox -o "$@" $^

clean:
	$(FIND) "$(SRC_DIR)" -iname '*.class' -delete
	$(RM) -r "$(BIN_DIR)"
