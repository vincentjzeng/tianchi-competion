FROM alpine:latest
MAINTAINER hssoxtt "hssoxtt@hsbc.com"

# setup environment
WORKDIR /usr/local/src
ADD jdk-8u271-linux-x64.tar.gz /usr/local/src/jdk/
COPY glibc-2.32-r0.apk /usr/local/src/
COPY glibc-bin-2.32-r0.apk /usr/local/src/
COPY glibc-i18n-2.32-r0.apk /usr/local/src/
RUN echo http://mirrors.aliyun.com/alpine/v3.10/main/ > /etc/apk/repositories && \
    echo http://mirrors.aliyun.com/alpine/v3.10/community/ >> /etc/apk/repositories
RUN apk update && apk upgrade
RUN apk --no-cache add ca-certificates wget \
    && wget -q -O /etc/apk/keys/sgerrand.rsa.pub https://alpine-pkgs.sgerrand.com/sgerrand.rsa.pub \
#    && wget -q -O glibc-2.32-r0.apk https://github.com/sgerrand/alpine-pkg-glibc/releases/download/2.32-r0/glibc-2.32-r0.apk\
#    && wget -q -O glibc-bin-2.32-r0.apk https://github.com/sgerrand/alpine-pkg-glibc/releases/download/2.32-r0/glibc-bin-2.32-r0.apk\
#    && wget -q -O glibc-i18n-2.32-r0.apk https://github.com/sgerrand/alpine-pkg-glibc/releases/download/2.32-r0/glibc-i18n-2.32-r0.apk\
    && apk add glibc-2.32-r0.apk glibc-bin-2.32-r0.apk glibc-i18n-2.32-r0.apk \
    && rm -rf /var/cache/apk/* glibc-2.32-r0.apk glibc-bin-2.32-r0.apk glibc-i18n-2.32-r0.apk
ENV JAVA_HOME=/usr/local/src/jdk/jdk-8u271-linux-x64
ENV CLASSPATH=.:$JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar
ENV PATH=$JAVA_HOME/bin:$PATH

#setup application
COPY tailbaseSampling-1.0-SNAPSHOT.jar /usr/local/src
COPY start.sh /usr/local/src
RUN chmod +x /usr/local/src/start.sh
ENTRYPOINT ["/bin/sh", "/usr/local/src/start.sh"]