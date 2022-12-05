# Stoker

<details open=""><summary><h2>Описание</h2></summary>
  <div>
    Stoker &ndash; сервис размещения объявлений.
  </div>
</details>

<details><summary><h2>Сборка и запуск приложения</h2></summary>

```
mvn clean package
docker image build -t backend:1.0 --build-arg JAR_FILE=stoker-1.0-SNAPSHOT.jar .
docker-compose up -d
```
</details>