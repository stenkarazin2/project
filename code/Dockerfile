FROM openjdk:8-jre-alpine
ENV MYSQL_IP_ADDRESS=mysql
COPY server.jar .
COPY public_html ./public_html
EXPOSE 80
CMD ["java", "-jar", "server.jar"]
