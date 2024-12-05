FROM mcr.microsoft.com/playwright/java:v1.49.0-noble

WORKDIR /app
COPY pom.xml /app/
COPY src /app/src/

RUN mvn clean package -DskipTests

CMD ["mvn", "test"]
