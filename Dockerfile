FROM openjdk:8-jre
EXPOSE 8080
ENV LOG_LEVEL trace
ADD target/scala-2.12/knapsack.jar /app/knapsack.jar
ENTRYPOINT ["java", "-jar", "\/app\/knapsack.jar"]
