#!/bin/bash
pkill -f "java -classpath*"
for i in {0..4};
do
    echo java -classpath src Main $((3000 + $i)) nodes/voisin-$i.txt INIT &
	java -classpath src Main $((3000 + $i)) nodes/voisin-$i.txt INIT &
done
	
