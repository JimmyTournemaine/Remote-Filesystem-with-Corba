#!/bin/bash

if [ $# -gt 0 ]; then
	if [ -r $1 ]; then
		FILE="$1"
	else
		echo "Cannot read $1"
		exit 1
	fi
else
	FILE="checklist.txt"
fi

for LINE in `cat $FILE`; do
	echo $LINE
	TYPE=`cut -d: -f1 <<< "$LINE"`
	PATH=`cut -d: -f2 <<< "$LINE"`
	echo "Type : $TYPE — PATH : $PATH"
	if [ $TYPE = "d" ]; then
		if [ ! -d $PATH ]; then
			echo "The $PATH folder does not exist."
		fi
	else 
		if [ $TYPE = "f" ]; then
			if [ ! -f $PATH ] ; then
				echo "The $PATH file does not exist."
			fi
		else
			echo "checklist.txt parse error"
		fi
	fi
done
