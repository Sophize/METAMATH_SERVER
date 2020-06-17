#!/bin/bash
gcloud config set account abc@sophize.org && \
gcloud config set project sophize-machines && \
mvn clean package && \
mvn appengine:deploy
