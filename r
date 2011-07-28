#!/bin/sh
MAVEN_OPTS="-Xms256m -Xmx2000m" mvn exec:java -Dexec.mainClass=URLEndpoint -e
