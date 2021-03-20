FROM openjdk:11

ADD /target/GithubCrawler-0.0.1-SNAPSHOT.jar ./

CMD java -Djdk.tls.client.protocols=TLSv1.1,TLSv1.2 -jar GithubCrawler-0.0.1-SNAPSHOT.jar
