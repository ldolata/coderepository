# Code Repository Crawler

### Overview
Application allows to crawl the code repository information. Current implementation supports Github repository,
although the application can be extended to retrieve data from other repositories

### Configuration
Application requires Github authentication token which can be acquired:
* by registering Github App and getting token for this application - [How to register Github App](https://docs.github.com/en/developers/apps/building-github-apps/creating-a-github-app)
* by getting private token - [How to get token](https://docs.github.com/en/authentication/keeping-your-account-and-data-secure/creating-a-personal-access-token)

Acquired token have to be assigned to system variable:
```
export GITHUB_TOKEN=<token>
```
Additionally application allows customizing username and password for web application

```
export APP_USERNAME=<new username>
export APP_PASSWORD=<new password>
```

### Application Build And Run
Application can be build and run:
```
export GITHUB_TOKEN=<token> & ./gcrawler-0.0.1-SNAPSHOT.jar
```

### Conteiner deployment
The app can be deployed as a container using jib plugin. The following maven properties
have to be change in pom.xml. The sample config bellow:
```
	<jib.url>registry.hub.docker.com/bob/coderepository</jib.url>
	<jib.username>bob</jib.username>
	<jib.password>password</jib.password>
```
And run
```
mvn compile jib:build
```




