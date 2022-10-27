# Cinema application

### To run this application you need to point out program arguments. For example:
To build project: mvn clean install
To run project: java -Dserver.port=$PORT $JAVA_OPTS -jar target/CinemaSpringBootApp-0.0.1-SNAPSHOT.jar 
        --DATABASE_URL=jdbc:postgresql://localhost:5432/cinema 
        --DATABASE_USERNAME=postgres
        --DATABASE_PASSWORD=postgres
        --EMAIL=email@gmail.com 
        --EMAIL_PASSWORD=password 
        --APPLICATION_URL=http://localhost:8080

Note: email password is not usual password you log in account but special password to smtp server.
Follow this link to see the details and to generate your Google App Password : https://support.google.com/accounts/answer/185833