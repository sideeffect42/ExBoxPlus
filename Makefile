# Define stupid escape variables
SPACE :=
SPACE +=

BIN_DIR := ./bin
SRC_DIRS := ./src
ifneq (,$(SRCDIR))
SRC_DIRS += $(SRCDIR)
endif

SRCS = $(shell find $(SRC_DIRS:%:"%") -iname '*.java')
CLASSES = $(shell find $(SRC_DIRS:%:"%") -iname '*.class')
.SUFFIXES: .java .class

CP := $(SRC_DIRS)
ifneq (,$(JAVA_HOME))
ifneq (,$(wildcard $(JAVA_HOME)/lib/tools.jar))
	CP += $(JAVA_HOME)/lib/tools.jar
endif
else
$(warning JAVA_HOME is not defined)
endif
CP_ARG := -cp "$(subst $(SPACE),:,$(CP))"

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
JFLAGS = $(CP_ARG) -source 1.5 -target 1.5 -Xlint

.java.class:
	$(JC) $(JFLAGS) '$*.java'

.PHONY: all run clean

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
	$(NJC) --main=ch.zhaw.ads.ExBox -o "$@" $^

run:
ifneq (,$(wildcard $(TARGET_NATIVE)))
	"$(TARGET_NATIVE)" $(args)
else ifneq (,$(wildcard $(TARGET_BYTE)))
	java $(CP_ARG) -jar "$(TARGET_BYTE)" $(args)
else
	java $(CP_ARG) 'ch.zhaw.ads.ExBox' $(args)
endif


clean:
	$(FIND) $(SRC_DIRS:%:"%") -iname '*.class' -delete
	$(RM) -r "$(BIN_DIR)"
