SRC_DIR := ./src
CLASSES = $(shell find "$(SRC_DIR)" -iname '*.java')
.SUFFIXES: .java .class

# Applications
JC = javac
FIND = find

# Compiler flags
JFLAGS = -cp "$(SRC_DIR)" -g


.java.class:
	$(JC) $(JFLAGS) $*.java


all: exbox-classfiles

exbox-classfiles: $(CLASSES:.java=.class) ;

.PHONY: exbox
exbox: bin/ExBox ;

bin/ExBox: $(shell find . -iname '*.java')
	mkdir -p "$(dir $@)"
	gcj --main=ch.zhaw.ads.ExBox -o "$@" $^

.PHONY: clean
clean:
	$(FIND) "$(SRC_DIR)" -iname '*.class' -delete
	rm -fr ./bin/
