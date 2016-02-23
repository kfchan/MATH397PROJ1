JAVAC = javac
sources = $(wildcard src/project1/*.java)
classes = $(addprefix bin/project1/, $(notdir $(sources:.java=.class)))
tests = $(wildcard src/test/*.java)
test_classes = $(addprefix bin/test/, $(notdir $(tests:.java=.class)))

all: $(classes)

compiletest: $(test_classes)

clean:
	rm -f $(classes) $(test_classes)

bin/project1/%.class: src/project1/%.java
	$(JAVAC) -cp src/ -d bin/ $<

bin/test/%.class: src/test/%.java
	$(JAVAC) -cp src/:src/test:bin/:lib/junit-4.12.jar -d bin/test/ $<