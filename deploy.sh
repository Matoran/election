#!/bin/bash
scp -i hepia.pem -P 10165 hepia.pem ubuntu@hepiacloud.hesge.ch:hepia.pem
scp -i hepia.pem -P 10165 out/election.jar ubuntu@hepiacloud.hesge.ch:election.jar

ssh -i hepia.pem -p 10165 ubuntu@hepiacloud.hesge.ch "scp -i hepia.pem -o StrictHostKeyChecking=no election.jar ubuntu@10.0.0.7:election.jar"
ssh -i hepia.pem -p 10165 ubuntu@hepiacloud.hesge.ch "scp -i hepia.pem -o StrictHostKeyChecking=no election.jar ubuntu@10.0.0.8:election.jar"
ssh -i hepia.pem -p 10165 ubuntu@hepiacloud.hesge.ch "scp -i hepia.pem -o StrictHostKeyChecking=no election.jar ubuntu@10.0.0.9:election.jar"
ssh -i hepia.pem -p 10165 ubuntu@hepiacloud.hesge.ch "scp -i hepia.pem -o StrictHostKeyChecking=no election.jar ubuntu@10.0.0.11:election.jar"

ssh -i hepia.pem -p 10165 ubuntu@hepiacloud.hesge.ch "ssh -i hepia.pem -p ubuntu@10.0.0.7 \"java -jar election.jar 3000 voisin.txt INIT\""
ssh -i hepia.pem -p 10165 ubuntu@hepiacloud.hesge.ch "ssh -i hepia.pem -p ubuntu@10.0.0.8"
ssh -i hepia.pem -p 10165 ubuntu@hepiacloud.hesge.ch "ssh -i hepia.pem -p ubuntu@10.0.0.9"
ssh -i hepia.pem -p 10165 ubuntu@hepiacloud.hesge.ch "ssh -i hepia.pem -p ubuntu@10.0.0.11"
