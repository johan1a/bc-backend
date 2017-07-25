FROM java:8-alpine
MAINTAINER Your Name <you@example.com>

COPY target/uberjar/bc-backend.jar /bc-backend/app.jar

EXPOSE 3000

CMD ["java", "-jar", "/bc-backend/app.jar"]
