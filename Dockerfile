FROM registry.cn-zhangjiakou.aliyuncs.com/knowin-iot/java8:latest
MAINTAINER FengXiao <fengxiao@knowin.com>

ARG AcmNS=AIoTPlatform_op
ENV AIoTEnv=$AcmNS

COPY config/${AIoTEnv}/logback.xml /app/
COPY app/build/libs/main.jar /app/
COPY config/launcher.json /app/

ENTRYPOINT exec \
java -Dlogback.configurationFile=/app/logback.xml \
     -Dvertx.logger-delegate-factory-class-name=io.vertx.core.logging.SLF4JLogDelegateFactory \
     -jar /app/main.jar \
     -conf /app/launcher.json

EXPOSE 8010