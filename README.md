### Hexlet tests and linter status:
[![Actions Status](https://github.com/Rbeat542/java-project-72/actions/workflows/hexlet-check.yml/badge.svg)](https://github.com/Rbeat542/java-project-72/actions)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=Rbeat542_java-project-72&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=Rbeat542_java-project-72)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=Rbeat542_java-project-72&metric=coverage)](https://sonarcloud.io/summary/new_code?id=Rbeat542_java-project-72)

Demo deploy: https://java-project-72-blv4.onrender.com

## Проект. Анализатор сайтов.

Сервис, который анализирует указанные страницы на SEO пригодность.

Данный проект - полноценный веб-сайт на базе фреймворка Javalin. Здесь отрабатываются базовые принципы построения современных сайтов на MVC-архитектуре: работа с роутингом, обработчиками запросов и шаблонизатором, взаимодействие с базами данных (H2 и Postgres) через JDBC.

## Работа с программой
### Требования
Linux


### Запуск
```
$ git clone git@github.com:Rbeat542/java-project-72.git  
$ cd java-projet-72  
$ make run  
http://localhost:7070  
```

### Работа с программой
1 На главной странице (Main page) присутствует форма для добавления адреса страницы/сайта для дальнейшего анализа.  
2 Вставьте в поле формы адрес страницы/сайта, которые необходимо проверить.
3 Нажмите кнопку **Add**  
4 При корректных данных откроется страница с информацией о успешном добавлении сайта. В случае некорректных данных появится сообщение "Некорректный URL" и будет предложено внести исправления в проверяемый адрес. При попытке добавить существуюший в базе адрес, появится сообщение "Страница уже существует"  
5 Если вы хотите проанализировать сайт, нажмите на его имя. Откроется страница с информацией о сайте и выполненных проверках.  
6 При нажатии на кнопку **Check url** происходит обращение к проверяемому сайту, проверяется его ответ на запрос, возвращаются: статус ответа (поле "Код ответа"), значения тегов Description, Title, H1 при их наличии. В поле "Дата проверки" вносится дата и время данного проверки.  
7 При открытии ссылки "Urls in database" открывается страница c перечнем сайтов, добавленных в базу данных, дата и время последней проверки и последний полученный статус ответа.
