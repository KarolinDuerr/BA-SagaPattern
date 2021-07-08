FROM open-liberty:beta

COPY --chown=1001:0 src/main/liberty/config/server.xml /config/
COPY --chown=1001:0 src/main/liberty/config/bootstrap.properties /config/
COPY --chown=1001:0 build/libs/mysql-connector-java-*.jar /config/jdbc/
COPY --chown=1001:0 build/libs/extension liberty/usr/extension/
COPY --chown=1001:0 build/libs/TravelService-*.war /config/apps/

RUN configure.sh