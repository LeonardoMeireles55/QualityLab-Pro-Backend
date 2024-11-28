# Etapa de construção do Maven
FROM maven:3.8.3-openjdk-17 AS build
WORKDIR /usr/src/app
COPY . .
RUN mvn clean package -DskipTests

# Etapa de execução
FROM eclipse-temurin:17-jdk
WORKDIR /usr/src/app

# Copia o arquivo gerado pelo Maven
COPY --from=build /usr/src/app/target/QualityLabPro-0.0.1-beta.jar app.jar

# Copia o script start.sh para o contêiner
COPY start.sh /usr/src/app/start.sh

# Dá permissão de execução ao start.sh
RUN chmod +x /usr/src/app/start.sh

# Usa o script start.sh como ponto de entrada
ENTRYPOINT ["./start.sh"]

# Comando de fallback se o start.sh não for encontrado ou falhar
CMD ["java", "-jar", "-Dspring.profiles.active=prod", "app.jar"]
