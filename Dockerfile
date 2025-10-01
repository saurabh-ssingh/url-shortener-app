FROM eclipse-temurin:17-jdk-alpine

#set working dir
WORKDIR /app

#copy jar
COPY target/tinyurler-0.0.1-SNAPSHOT.jar app.jar

#Expose port
EXPOSE 8080

#Run the app
ENTRYPOINT ["java", "-jar", "app.jar"]