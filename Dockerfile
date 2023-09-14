FROM openjdk:22

WORKDIR /app

COPY usersDb/target/usersDb-0.0.1-SNAPSHOT.jar /app

CMD ["java", "-jar", "usersDb-0.0.1-SNAPSHOT.jar"]

