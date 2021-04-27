FROM gradle:6.6.1-jdk11-hotspot AS stage1
COPY --chown=gradle:gradle . /usr/datacatalog-versioning-service
WORKDIR /usr/datacatalog-versioning-service
RUN gradle bootJar

FROM openjdk:11-slim
COPY --from=stage1 /usr/datacatalog-versioning-service /usr/datacatalog-versioning-service
CMD java -jar /usr/datacatalog-versioning-service/build/libs/datacatalog-versioning-service-0.0.1-SNAPSHOT.war

EXPOSE 3000
