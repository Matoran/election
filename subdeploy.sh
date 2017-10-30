#!/bin/bash

scp -i hepia.pem -o StrictHostKeyChecking=no election.jar ubuntu@10.0.0.3:election.jar
scp -i hepia.pem -o StrictHostKeyChecking=no voisin-1.txt ubuntu@10.0.0.3:voisin.txt
scp -i hepia.pem -o StrictHostKeyChecking=no election.jar ubuntu@10.0.0.7:election.jar
scp -i hepia.pem -o StrictHostKeyChecking=no voisin-2.txt ubuntu@10.0.0.7:voisin.txt
scp -i hepia.pem -o StrictHostKeyChecking=no election.jar ubuntu@10.0.0.9:election.jar
scp -i hepia.pem -o StrictHostKeyChecking=no voisin-3.txt ubuntu@10.0.0.9:voisin.txt
scp -i hepia.pem -o StrictHostKeyChecking=no election.jar ubuntu@10.0.0.10:election.jar
scp -i hepia.pem -o StrictHostKeyChecking=no voisin-4.txt ubuntu@10.0.0.10:voisin.txt


#ssh -i hepia.pem ubuntu@10.0.0.3 "sudo apt update"
#ssh -i hepia.pem ubuntu@10.0.0.3 "sudo apt install -y default-jre"
#ssh -i hepia.pem ubuntu@10.0.0.7 "sudo apt update"
#ssh -i hepia.pem ubuntu@10.0.0.7 "sudo apt install -y default-jre"
#ssh -i hepia.pem ubuntu@10.0.0.9 "sudo apt update"
#ssh -i hepia.pem ubuntu@10.0.0.9 "sudo apt install -y default-jre"
#ssh -i hepia.pem ubuntu@10.0.0.10 "sudo apt update"
#ssh -i hepia.pem ubuntu@10.0.0.10 "sudo apt install -y default-jre"
ssh -i hepia.pem ubuntu@10.0.0.3 "pkill java"
ssh -i hepia.pem ubuntu@10.0.0.3 "java -jar election.jar 3000 voisin.txt INIT" &
ssh -i hepia.pem ubuntu@10.0.0.7 "pkill java"
ssh -i hepia.pem ubuntu@10.0.0.7 "java -jar election.jar 3000 voisin.txt INIT" &
ssh -i hepia.pem ubuntu@10.0.0.9 "pkill java"
ssh -i hepia.pem ubuntu@10.0.0.9 "java -jar election.jar 3000 voisin.txt INIT" &
ssh -i hepia.pem ubuntu@10.0.0.10 "pkill java"
ssh -i hepia.pem ubuntu@10.0.0.10 "java -jar election.jar 3000 voisin.txt INIT" &
pkill java
java -jar election.jar 3000 voisin-0.txt INIT
