FROM bellsoft/liberica-openjdk-alpine-musl:17
ARG JAR_FILE
RUN mkdir -p /app
EXPOSE 8080
COPY ./target/${JAR_FILE} /app/app.jar
CMD java -jar /app/app.jar
