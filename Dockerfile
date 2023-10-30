#
# Build stage
#
FROM maven:3.8.3-openjdk-17-slim AS build
COPY . /app/movie-api/
RUN mvn -f /app/movie-api/ clean package

#
# Package stage
#
FROM openjdk:latest
RUN  echo --from=build /app/movie-api/target/movie-api.jar
COPY --from=build /app/movie-api/target/movie-api.jar /usr/local/lib/movie-api.jar
EXPOSE 8080
ENTRYPOINT ["java","-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:8000","-jar","/usr/local/lib/movie-api.jar"]