# Define stupid escape variables
SPACE :=
SPACE +=

BIN_DIR := ./bin
SRC_DIRS := ./src
SRC_DIRS_ALL := $(SRC_DIRS)
ifneq (,$(SRCDIR))
SRC_DIRS_ALL += '$(SRCDIR)'
endif

SRCS = $(shell find -L $(SRC_DIRS) -iname '*.java')
SRCS_ALL = $(shell find -L $(SRC_DIRS_ALL) -iname '*.java')
CLASSES = $(shell find -L $(SRC_DIRS) -iname '*.class')
CLASSES_ALL = $(shell find -L $(SRC_DIRS_ALL) -iname '*.class')

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
TARGET_JAR := $(BIN_DIR)/ExBox.jar
MANIFEST_FILE := META-INF/MANIFEST.MF

# Compiler flags
JFLAGS = $(CP_ARG) -source 1.5 -target 1.5 -Xlint

.java.class:
	$(JC) $(JFLAGS) '$*.java'

.PHONY: all jar native run clean

%/:
	@echo "Create directory $*"
	$(MKDIR_P) "$*"

# Default is normal Java-bytecode compilation
all: $(SRCS_ALL:.java=.class) ;

# Native compilation
native: $(TARGET_NATIVE) ;

# Bytecode compilation, but pack classfiles in jar
jar: $(TARGET_JAR) ;

.SECONDEXPANSION:
$(TARGET_JAR): $(SRCS:.java=.class) | $$(@D)/
	$(JAR) -cfm "$@" "$(MANIFEST_FILE)" $(CLASSES:%='%')

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
