# Shortner Backend
API rest do encurtador de links shortner, applicação completa aqui: https://shr-app.herokuapp.com/

## Requisitos
 
- [__Docker__](https://docs.docker.com/install/linux/docker-ce/ubuntu/)
- [__Docker compose__](https://docs.docker.com/compose/install/)

### Execução do projeto

Primeiro deve-se criar um arquivo .env com o mesmo padrão do arquivo .env.example

``` 
JDBC_DATABASE_URL=jdbc:postgresql://db:5432/shortner
JDBC_DATABASE_USERNAME=username
JDBC_DATABASE_PASSWORD=password
APP_SECRET=secret
```

Depois, executar o comando abaixo para gerar um arquivo jar

```sh
$ ./mvnw clean package -DskipTests
```

Copiar o arquivo jar gerado para a raiz do projeto

```sh
$ cp target/shortner-0.0.1-SNAPSHOT.jar .
```

Executar o projeto

```sh
$ docker-compose up -d
```

Popular o banco de dados

```sh
$ docker exec -i db psql -U postgres -d shortner < populate.sql
```
Obs: o usuário padrão criado é "admin" e senha "cccc1234"

Parar a execuçao
```sh
$ docker-compose down
```


