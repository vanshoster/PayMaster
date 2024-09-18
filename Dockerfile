# Stage 1: Build the application
FROM gradle:7.5.1-jdk17 AS build
WORKDIR /app
COPY . .
RUN gradle build --no-daemon

# Stage 2: Run the application
FROM openjdk:17-jdk-slim
WORKDIR /app

#RUN apt-get update && \
#    apt-get install -y fontconfig libfreetype6 libx11-6 && \
#    apt-get clean && \
#    rm -rf /var/lib/apt/lists/*

COPY --from=build /app/build/libs/*.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-Djava.awt.headless=true", "-jar", "app.jar"]