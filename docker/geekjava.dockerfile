# Use uma imagem base que inclua o Java (por exemplo, openjdk)
FROM openjdk:8

# Diretório de trabalho dentro do contêiner
WORKDIR /app

# Copie o arquivo JAR construído pelo Maven para o contêiner
COPY target/seu-aplicativo.jar /app/

# Use o plugin maven-dependency-plugin para copiar as dependências
RUN mvn dependency:copy-dependencies -DincludeScope=runtime -DoutputDirectory=lib

# Expor a porta que seu aplicativo irá escutar (se aplicável)
# EXPOSE 8080

# Comando para executar o aplicativo quando o contêiner for iniciado
CMD ["java", "-cp", "seu-aplicativo.jar:lib/*", "com.geekjava.App"]