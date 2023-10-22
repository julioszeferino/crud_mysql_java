# CRUD MYSQL + JAVA
> CRUD simples utilizando JAVA e MYSQL.

## Como Executar o Projeto
```bash
# subir o bd
docker compose up -d
# build do projeto
cd geekjava-mysql/
mvn clean install
# execucao do projeto
mvn exec:java -Dexec.mainClass="com.geekjava.App"
```

## Ambiente de Desenvolvimento
- Java 8
- Maven
- Docker
- Docker Compose

```bash
# criando o projeto MAVEN, se necessario
mvn archetype:generate -DgroupId=com.example -DartifactId=my-project -DarchetypeArtifactId=maven-archetype-quickstart -DinteractiveMode=false
```

