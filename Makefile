MODULE = files
JAVA  = regular_fileImpl.java file_listImpl.java directoryImpl.java Serveur.java  Client.java
IDL   = files.idl

CLASS = $(JAVA:%.java=classes/$(MODULE)/%.class) 


all: idl src root


src:	subdir $(CLASS)
idl:	subdir $(IDL:.idl=.jacorb)
root:
	if [ ! -e root ]; then
		mkdir root
		chmod a+rwx root
	fi



############################################
## Do not change anything after this line
############################################

classes/$(MODULE)/%.class : %.java
	javac -d classes  $<

####

.SUFFIXES:
.SUFFIXES:      .idl .jacorb

.idl.jacorb:
	idl -d generated  $<
	javac -d classes generated/$(MODULE)/*.java
	touch $*.jacorb 

####

clean::
	rm -rf core *.jacorb *.ref 
	rm -rf classes generated

####

subdir:
	if [ ! -d classes ]; then \
	   mkdir classes;  \
	fi;
	if [ ! -d generated ]; then \
	   mkdir generated;  \
	fi;



