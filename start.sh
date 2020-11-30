#!/bin/sh

java -Dlogback.configurationFile=config/Dev/logback.xml \
     -Dvertx.logger-delegate-factory-class-name=io.vertx.core.logging.SLF4JLogDelegateFactory \
     -jar app/build/libs/main.jar \
     -conf ./config/config.json
