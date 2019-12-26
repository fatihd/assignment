# assignment

## How to build

## How to build with docker

    docker-compose build
    docker-compose up


### How to build without docker
Make sure maven uses JDK 13  (adapt w.r.t. platform) 

    $env:JAVA_HOME = "C:\Projects\jdk-13.0.1"
    
Generate spring boot jar
    
    ./mvnw clean package
    
Run as an ordinary spring boot app,
overriding default config as necessary.
e.g. specifying mongodb
    
    $JAVA_HOME/bin/java -jar .\target\demo-0.0.1-SNAPSHOT.jar --spring.data.mongodb.host=192.168.99.100   

### Coverage
Generate JaCoCo report

    mvnw clean verify

Open report in browser (adapt w.r.t. platform)
    
    ./target/site/jacoco/index.html