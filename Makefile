MODULE = files
JAVA  = file_listImpl.java regular_fileImpl.java directoryImpl.java Serveur.java Client.java Test.java
IDL   = files.idl

CLASS = $(JAVA:%.java=classes/$(MODULE)/%.class) 

default: idl src
src:	subdir $(CLASS)
idl:	subdir $(IDL:.idl=.jacorb)

####

classes/$(MODULE)/%.class : %.java
	javac -d classes  $<

####

.SUFFIXES: .idl .jacorb

.idl.jacorb:
	idl -d generated  $<
	javac -d classes generated/$(MODULE)/*.java
	touch $*.jacorb 

####

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



