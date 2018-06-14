FROM java:8
ADD target/tripmaker.jar tripmaker.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "tripmaker.jar"]
