FROM gradle:6.6.1-jdk11-hotspot AS stage1
COPY --chown=gradle:gradle . /usr/data-catalog-versioning-service
WORKDIR /usr/data-catalog-versioning-service
RUN gradle bootJar

FROM openjdk:11-slim
COPY --from=stage1 /usr/data-catalog-versioning-service /usr/data-catalog-versioning-service
CMD java -jar /usr/data-catalog-versioning-service/build/libs/versioning-api-1.0.0-SNAPSHOT.jar

EXPOSE 3000
