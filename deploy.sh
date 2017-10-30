#!/bin/bash
scp -i hepia.pem -P 10110 hepia.pem ubuntu@hepiacloud.hesge.ch:hepia.pem
scp -i hepia.pem -P 10110 out/election.jar ubuntu@hepiacloud.hesge.ch:election.jar
scp -i hepia.pem -P 10110 nodes/* ubuntu@hepiacloud.hesge.ch:
scp -i hepia.pem -P 10110 subdeploy.sh ubuntu@hepiacloud.hesge.ch:subdeploy.sh
ssh -i hepia.pem -p 10110 ubuntu@hepiacloud.hesge.ch "chmod +x subdeploy.sh"
ssh -i hepia.pem -p 10110 ubuntu@hepiacloud.hesge.ch "./subdeploy.sh"

