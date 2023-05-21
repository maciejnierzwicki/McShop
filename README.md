# McShop
A shop web app made for Minecraft servers, written in Java with use of Spring Boot.

### Let your users buy things like items, ranks and everything else what's possible to do with use of in-game commands!

This app integrates with real payment providers and allows users to pay with bank transfer and sms.
There is no limit of how much servers you can connect your shop with.
Application uses H2 or MySQL database to store its data.

**Main features of McShop:**
- Service management (add / edit / delete)
- Category management (add / edit / delete)
- Server management (add / edit / delete)
- User management (add / delete / change password / edit money)
- Edit order of services / categories / servers displaying
- Payments configuration (settings for bank transfer and sms)
- Order management (track all orders made by users)
- EventLog (track events that occur when app is running)
- Registration system (users don't have to make an account in order to buy things)
- Configuration of subpages ('rules' page, 'about' page and 'contact' page)
- Configuration of home page displaying with HTML input support
- General configurations of app like title, address, datetime formatting, pagination

**User features:**
- Navigate through servers, categories and services
- Make orders and track their status
- Pay for orders with bank transfer, sms or virtual wallet (user money)
- Change account password

**Available payment providers:**
- Bank transfer: [DotPay](https://www.dotpay.pl/)
- SMS: [MicroSMS](https://microsms.pl/)

**Technical requirements:**
- Java (version 11 or higher)
- MySQL server if you want to use this way of storing data, otherwise embedded H2 database can be selected (it is by default)

By default, when you launch app for the first time, the 'setup mode' will be active. You will be prompted to set configuration for database connection and to create admin account.  
**Note:** This app displays messages only in default, Polish language. I'm planning into bringing internationalization to it soon. For now, the only way of changing app messages is editing them directly in files located under ``/src/main/resources/static/templates`` dir.

**How to run this app:**

Open command line in project root directory and enter this command: ``java -jar MCShop-<version>-SNAPSHOT.jar --spring.profiles.active=prod``.  
Replace ``<version>`` with correct version number of jar you want use.
The other way is to make a script like 'start.bat' or 'start.sh' and put above command in it, then run this script.

**How to compile this app:**

Open command line in project root directory and enter this command: ``mvnw clean install``


**Tools used during app development:**
- Java 11
- Spring (Boot, MVC, Security, Data)
- HTML, CSS (Bootstrap 5)
- Thymeleaf
- Maven
- JUnit
- MySQL, H2
- [rkon-core](https://github.com/kr5ch/rkon-core)

**Author:** [Maciej Nierzwicki](https://github.com/maciejnierzwicki/)


**Screenshots:** [LINK](https://www.maciejnierzwicki.pl/projects/mcshop/)
