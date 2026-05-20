FROM amazonlinux:2023

LABEL maintainer="nurudeen"
LABEL purpose="Jenkins CI/CD Agent for PropertyFind (Java 21/Maven)"

# 1. Install base dependencies
RUN dnf remove -y curl-minimal || true && \
    dnf update -y && \
    dnf install -y --allowerasing \
        git curl unzip tar gzip jq \
        python3 python3-pip \
        shadow-utils findutils which openssl docker sudo && \
    dnf clean all

# 2. Install AWS CLI v2
RUN curl "https://awscli.amazonaws.com/awscli-exe-linux-x86_64.zip" -o "awscliv2.zip" && \
    unzip awscliv2.zip && ./aws/install && rm -rf aws awscliv2.zip

# 3. Install Amazon Corretto 21 (Java 21)
RUN rpm --import https://yum.corretto.aws/corretto.key && \
    curl -Lo /etc/yum.repos.d/corretto.repo https://yum.corretto.aws/corretto.repo && \
    dnf install -y java-21-amazon-corretto-devel && \
    dnf clean all
ENV JAVA_HOME=/usr/lib/jvm/java-21-amazon-corretto
ENV PATH="${JAVA_HOME}/bin:${PATH}"

# 4. Install Maven
ENV MAVEN_VERSION=3.9.6
RUN curl -fsSL https://archive.apache.org/dist/maven/maven-3/${MAVEN_VERSION}/binaries/apache-maven-${MAVEN_VERSION}-bin.tar.gz -o maven.tar.gz && \
    tar -xzf maven.tar.gz -C /opt && \
    ln -s /opt/apache-maven-${MAVEN_VERSION}/bin/mvn /usr/bin/mvn && \
    rm -f maven.tar.gz
ENV MAVEN_HOME=/opt/apache-maven-${MAVEN_VERSION}

# 5. Install kubectl
RUN curl -Ls "https://dl.k8s.io/release/$(curl -Ls https://dl.k8s.io/release/stable.txt)/bin/linux/amd64/kubectl" \
    -o /usr/local/bin/kubectl && chmod +x /usr/local/bin/kubectl

# 6. Jenkins user with Docker access
RUN groupadd docker || true && \
    useradd -m -s /bin/bash jenkins && \
    usermod -aG docker jenkins && \
    echo "jenkins ALL=(ALL) NOPASSWD: ALL" >> /etc/sudoers

USER jenkins
WORKDIR /home/jenkins
