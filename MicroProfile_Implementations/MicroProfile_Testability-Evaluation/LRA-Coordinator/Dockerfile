FROM open-liberty:beta

COPY --chown=1001:0 src/main/liberty/config/server.xml /config/
COPY --chown=1001:0 src/main/liberty/config/bootstrap.properties /config/

RUN configure.sh