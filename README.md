# Cinema Application

This is my first Spring Boot project that I have completed independently! I developed this project as the final project for the Java program at Nix Solutions.

## 💡 Functionality

Anonymous users can browse the cinema schedule, view movie details, check seat availability in the halls, and log in or sign up for an account.

Authenticated users can purchase tickets for films and access their profile to view all their transactions.

Admins have the ability to view user profiles, manage them, and block/unblock users. Additionally, admins can perform CRUD operations on films and sessions.

Super-admins can manage admins, add or remove them, and also have access to all admin functionalities.

## 🔑 Value of the Project

Throughout this project, I learned various modules of the Spring Framework, such as MVC, Data JPA, Security, and Mail. Thymeleaf was used as the template engine. I discovered how to send emails using the SMTP protocol with Spring Mail. These email messages are not just plain text but HTML pages with attachments. Moreover, I learned how to work with PDF files using the iText library.

This has been an incredibly valuable experience!

## 🏠 Building the Project:

Run `mvn clean install`

## 🚀 Running the Project:

Execute the following command: 

java -Dserver.port=$PORT $JAVA_OPTS -jar target/CinemaSpringBootApp-0.0.1-SNAPSHOT.jar --DATABASE_URL=jdbc:postgresql://localhost:5432/cinema --DATABASE_USERNAME=postgres --DATABASE_PASSWORD=postgres --EMAIL=email@gmail.com --EMAIL_PASSWORD=password --APPLICATION_URL=http://localhost:8080

Note: The email password mentioned here is not the usual password used for logging into your account, but a special password for the SMTP server. Please follow this link to see the details and generate your Google App Password: https://support.google.com/accounts/answer/185833
