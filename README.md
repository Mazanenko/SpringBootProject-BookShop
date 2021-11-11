# Booksland.shop

----
## Description:
This is a bookshop site.

It provides four levels of access for guest, customer, manager and admin:
- ***Guest*** can only view the list of books.
- ***Customer*** can choose a book, add it to cart, make an order, subscribe to new book arrivals, 
edit his profile, delete his account.
- ***Manager*** can do all customer's actions, add/edit/delete books, edit his profile.
- ***Admin*** can all manager's actions and add/edit/delete manager's account.

This app based on:
- java 11
- spring-boot 2.5.4
- spring security
- thymeleaf
- postgreSQL
- Flyway
- Thymeleaf
- Docker
- JUnit 5
- Mockito


## ___Install this before running the app:___
* Git
* Maven
* Docker
* Docker Compose

## ___Run app:___
1. Clone this repo ```git clone https://github.com/Mazanenko/SpringBootProject-BookShop.git```
2. To enable mail sender, you have to add mail server properties in [application.properties](./src/main/resources/application.properties).
If you don't do this, customers can't activate their accounts. Also, customers and managers 
will not be able to get notifications about orders and new book arrivals. But you still can use 
this app as an admin.
3. To build the project use ```mvn install```
4. Then use ```docker-compose up```. <br/>
It will download docker images, create docker containers and run the application.
5. Open your browser and go to http://localhost:8080/


Login for admin: *admin* <br/>
Password: *admin*

Also, you can just visit www.booksland.shop
