MODULE = files
JAVA  = file_listImpl.java regular_fileImpl.java directoryImpl.java Serveur.java Client.java

IDL   = files.idl

CLASS = $(JAVA:%.java=classes/$(MODULE)/%.class) 

default: idl src
test: idl src test_compile

src:	subdir $(CLASS)
idl:	subdir $(IDL:.idl=.jacorb)

####

classes/$(MODULE)/%.class : src/files/%.java
	javac -d classes  $<

####

.SUFFIXES: .idl .jacorb

.idl.jacorb:
	idl -d generated  $<
	javac -d classes generated/$(MODULE)/*.java
	touch $*.jacorb 

####

test_compile: test/files/test/Test.java
	javac -d classes test/files/test/Test.java

clean:
	rm -rf core *.jacorb *.ref *~ classes generated root

####

subdir:
	if [ ! -d classes ]; then \
	   mkdir classes;  \
	fi;
	if [ ! -d generated ]; then \
	   mkdir generated;  \
	fi;
	if [ ! -d root ]; then \
	   mkdir root;  \
	   chmod a+rwx root; \
	fi;



