FROM gradle:7-jdk17 AS builder

WORKDIR /app
COPY . ./

RUN gradle build --no-daemon

FROM azul/zulu-openjdk:17-jre AS production

# Add a work directory
WORKDIR /app
#Copy Certs
COPY certs /app/certs

COPY --from=builder /app/build/libs /app
# Expose port
EXPOSE 8090:8090
# Start app

ENTRYPOINT java -Duser.timezone=America/Argentina/Buenos_Aires \
    -jar gt-retail-0.0.1-SNAPSHOT.war \
    --server.port=8090 \
    --jwt.signing.key.secret=$JWT_SIGN_KEY \
    --spring.jpa.show-sql=false \
    --spring.datasource.url=$DB_URL \
    --spring.profiles.active=$PROFILE \
    --spring.application.name=$APP_NAME \
    --spring.datasource.username=$DB_USER \
    --spring.datasource.password=$DB_PASSWRD
