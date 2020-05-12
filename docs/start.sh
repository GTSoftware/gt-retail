#!/bin/bash
#Configuration
JAVA_HOME="/opt/java/jdk-11.0.6/bin"
APP_NAME="gt-retail"
APP_JAR="gt-retail-0.0.1-SNAPSHOT.war"
SERVER_PORT=8080
RAND_STR=$(openssl rand -base64 12)
JWT_SIGN_KEY="1ncr3d1bl3#S3cr3t_L0ng3st/3v3r$RAND_STR"
DB_URL="jdbc:postgresql://localhost:5432/retail_rt"
DB_PASSWRD="postgres"
DB_USER="postgres"
PROFILE="prod"

#Command execution
nohup $JAVA_HOME/java -jar $APP_JAR --server.port=$SERVER_PORT \
  --jwt.signing.key.secret="$JWT_SIGN_KEY" \
  --spring.jpa.show-sql=false \
  --spring.datasource.url=$DB_URL \
  --spring.profiles.active=$PROFILE \
  --spring.application.name=$APP_NAME \
  --spring.datasource.username=$DB_USER \
  --spring.datasource.password=$DB_PASSWRD & echo $! >./pid.$APP_NAME &
