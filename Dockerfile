FROM gradle:6.6.1-jdk11-hotspot AS stage1
COPY --chown=gradle:gradle . /usr/data-catalog-versioning-service
WORKDIR /usr/data-catalog-versioning-service
RUN gradle bootJar --no-gradle

FROM openjdk:11-slim
COPY --from=stage1 /usr/data-catalog-versioning-service /usr/data-catalog-versioning-service
CMD java -jar /usr/data-catalog-versioning-service/build/libs/versioning-api-0.0.01-SNAPSHOT.war

EXPOSE 3000
