FROM ubuntu:22.04

ENV DEBIAN_FRONTEND=noninteractive

LABEL version="1.0"
LABEL description="ubuntu_python_java_kotlin"
LABEL tags="ubuntu, python, java"

RUN apt-get update && \
    apt-get install -y software-properties-common && \
    add-apt-repository -y ppa:deadsnakes/ppa && \
    apt-get update && \
    apt-get install -y python3.8 && \
    apt-get install -y openjdk-8-jdk && \
    apt-get install -y kotlin && \
    apt-get install -y curl && \
    apt-get install -y unzip

CMD [""]