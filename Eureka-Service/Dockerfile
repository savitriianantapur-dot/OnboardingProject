# Dockerfile — place inside each microservice folder
FROM eclipse-temurin:17-jdk AS build

# Create app dir
WORKDIR /app

# Copy maven build files and install dependencies (leverage layer caching)
COPY pom.xml mvnw ./
COPY .mvn .mvn
# copy only pom to install dependencies (optional speed-up)
RUN --mount=type=cache,target=/root/.m2 ./mvnw -q -B dependency:go-offline

# Copy source and build
COPY src ./src
RUN --mount=type=cache,target=/root/.m2 ./mvnw -q -B -DskipTests package

# Create run image
FROM eclipse-temurin:17-jre
WORKDIR /app

# Copy jar produced in build stage (pattern may match app jar)
COPY --from=build /app/target/*-SNAPSHOT.jar app.jar
# If your artifact is named differently, change the above path.

# Expose sensible default port — override on docker run if needed
# (This is informational only)
EXPOSE 8080

ENTRYPOINT ["sh", "-c", "java -jar /app/app.jar --server.port=${SERVER_PORT:-8080}"]
