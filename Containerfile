FROM eclipse-temurin:17-jdk

ENV ANDROID_SDK_ROOT=/opt/android-sdk
ENV PATH=$PATH:$ANDROID_SDK_ROOT/cmdline-tools/latest/bin:$ANDROID_SDK_ROOT/platform-tools

ENV GRADLE_VERSION=8.6

RUN apt-get update && apt-get install -y --no-install-recommends \
    unzip \
    librsvg2-bin \
 && rm -rf /var/lib/apt/lists/*

RUN mkdir -p $ANDROID_SDK_ROOT/cmdline-tools \
    && curl -o /tmp/cmdline-tools.zip https://dl.google.com/android/repository/commandlinetools-linux-14742923_latest.zip \
    && unzip /tmp/cmdline-tools.zip -d $ANDROID_SDK_ROOT/cmdline-tools \
    && mv $ANDROID_SDK_ROOT/cmdline-tools/cmdline-tools $ANDROID_SDK_ROOT/cmdline-tools/latest \
    && rm /tmp/cmdline-tools.zip \
    && yes | sdkmanager --licenses

RUN sdkmanager "platform-tools" "platforms;android-33" "build-tools;34.0.0" \
    && rm -rf \
       $ANDROID_SDK_ROOT/cmdline-tools/.cache \
       $ANDROID_SDK_ROOT/.android \
       $ANDROID_SDK_ROOT/.temp

RUN curl -fsSL https://services.gradle.org/distributions/gradle-${GRADLE_VERSION}-bin.zip -o /tmp/gradle.zip \
    && unzip /tmp/gradle.zip -d /opt \
    && ln -s /opt/gradle-${GRADLE_VERSION}/bin/gradle /usr/local/bin/gradle \
    && rm /tmp/gradle.zip

RUN curl -LO https://github.com/google/google-java-format/releases/download/v1.34.1/google-java-format_linux-x86-64 \
    && install google-java-format_linux-x86-64 /usr/local/bin/google-java-format \
    && rm google-java-format_linux-x86-64

RUN curl -LO https://github.com/ByteHamster/android-xml-formatter/releases/download/1.1.0/android-xml-formatter.jar
