FROM openliberty/open-liberty:full-java11-openj9-ubi

COPY --chown=1001:0 src/main/liberty/config/server.xml /config/
COPY --chown=1001:0 src/main/liberty/config/bootstrap.properties /config/
COPY --chown=1001:0 build/libs/mysql-connector-java-*.jar /config/jdbc/
COPY --chown=1001:0 build/libs/TravelService-*.war /config/apps/

RUN configure.sh