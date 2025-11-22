# sb3-filter-problem
Repo to analyze problems during migration from SpringBoot2 (SB2) via SB3 (old release) to SpringBoot4 (SB4).

The SB2 application works fine, SB3 needs a special fix (Thanks to GlenErrand!) and SB4 is to verify changes are still working properly ....

The self-contained example-app has 3 elements for login (tenant, username and password) and a specific logout handler to perform operations upon successful logout.
H2 is used as a database and data structures are created via liquibase.

Both apps contain no tests as they are taken and adapted from a different (private) project.

[![GH Actions Status](https://github.com/ottlinger/sb3-filter-problem/workflows/JavaCI/badge.svg)](https://github.com/ottlinger/sb3-filter-problem/actions)

## Feature comparison between SB2 and SB4 and quick links into the code

| Description                                                                                         |                                                         SB2 - 2.8.x                                                          | SB4 - 4.0.x                                                                                                                 | Status SB2  |   Status SB4   |
|:----------------------------------------------------------------------------------------------------|:-----------------------------------------------------------------------------------------------------------------------------:|:-------------------------------------------------------------------------------------------------------------------------------|:-----------:|:--------------:|
| i18n via property files                                                                             |                                                                                                                               |                                                                                                                                | 👍 working  |   👍 working   |
| ApplicationUser to encapsulate tenant, user, password for login                                     |                     [ApplicationUser](./sb2/src/main/java/de/aikiit/prototype/user/ApplicationUser.java)                      | [ApplicationUser](./sb3/src/main/java/de/aikiit/prototype3/user/ApplicationUser.java)                                          | 👍 working  |   👍 working   |
| LoginTenantAuthenticationFilter (UsernamePasswordAuthenticationFilter) to extract data upon login   |     [LoginTenantAuthenticationFilter](./sb2/src/main/java/de/aikiit/prototype/login/LoginTenantAuthenticationFilter.java)     | [LoginTenantAuthenticationFilter](./sb3/src/main/java/de/aikiit/prototype3/login/LoginTenantAuthenticationFilter.java)         | 👍 working  |   👍 working   |
| Filter configuration                                                                                |     [AuthenticationConfiguration](./sb2/src/main/java/de/aikiit/prototype/configuration/AuthenticationConfiguration.java)     | [Sb3CustomDsl](./sb3/src/main/java/de/aikiit/prototype3/configuration/Sb3CustomDsl.java)                                       | 👍 working  | 🔥 not working |
| DataSeeding, mechanism to create example tenant, user, password combinations upon application start |                 [Package seeding](./sb2/src/main/java/de/aikiit/prototype/seeding/BootstrapDataCreator.java)                  | [Package Seeding](./sb3/src/main/java/de/aikiit/prototype3/seeding/BootstrapDataCreator.java)                                  | 👍 working  |   👍 working   |
| Authentication configuration                                                                        |     [AuthenticationConfiguration](./sb2/src/main/java/de/aikiit/prototype/configuration/AuthenticationConfiguration.java)     | [AuthenticationConfiguration](./sb3/src/main/java/de/aikiit/prototype3/configuration/AuthenticationConfiguration.java)         | 👍 working  | 🔥 not working |
| SimpleUrlLogoutSuccessHandler to trigger actions upon logout                                        | [LeaveEventsUponLogoutSuccessHandler](./sb2/src/main/java/de/aikiit/prototype/login/LeaveEventsUponLogoutSuccessHandler.java) | [LeaveEventsUponLogoutSuccessHandler](./sb3/src/main/java/de/aikiit/prototype3/login/LeaveEventsUponLogoutSuccessHandler.java) | 👍 working  | 🔥 not working |

## How to run the app

The app uses the Maven wrapper, thus all you need is a recent JDK such as JDK 17.

### Working application with SB2

```
cd sb2
./mvnw spring-boot:run
```
Launch the app via [localhost:8080](http://localhost:8080)
and log in.
After a successful login you may log out again.

### Problematic application with SB4

```
cd sb4
./mvnw spring-boot:run
```
Launch the app via [localhost:8080](http://localhost:8080)

You are unable to login/logout .... which is the reason for this repository.

## Which credentials may I use?

You may use the following combination in order to successfully login:

| Tenant | Username | Password |
|:-------|:--------:|---------:|
| A      |  auser   |    auser |
| A      |  buser   |    buser |
| A      |  cuser   |    cuser |
| B      |  auser   |    auser |
| B      |  buser   |    buser |
| B      |  cuser   |    cuser |
| C      |  auser   |    auser |
| C      |  buser   |    buser |
| C      |  cuser   |    cuser |

## Added e2e test to automatically check if application is working

In order to show the behaviour during migration to SB3 you may run an e2e test based on [Cypress](https://cypress.io).
Do not forget to start the Spring Boot application beforehand!

In order to launch the tests, run:
```
 npx cypress run
 ```
and check the [docs](./cypress-test/README.md) about how to set up the project locally.
## Posted on StackOverflow

As Spring Boot does not want questions in GitHub issues I tried to start a post about the problem at [StackOverflow](https://stackoverflow.com/questions/76799484/usernamepasswordauthenticationfilter-and-simpleurllogoutsuccesshandler-not-worki)

### 2023-07-31 Order of filters

Suspected order of filters to be responsible for the problem, but seems to be similar (disabling CORS in SB3 does not help).

#### SB2 order

```
2023-07-31 13:55:16.652  INFO 217030 --- [           main] o.s.s.web.DefaultSecurityFilterChain     : Will secure any request with [
org.springframework.security.web.session.DisableEncodeUrlFilter@48c42253,
org.springframework.security.web.context.request.async.WebAsyncManagerIntegrationFilter@32647dde,
org.springframework.security.web.context.SecurityContextPersistenceFilter@2af5eab6,
org.springframework.security.web.header.HeaderWriterFilter@4ba056ab,
org.springframework.security.web.authentication.logout.LogoutFilter@397fced4,
de.aikiit.prototype.login.LoginTenantAuthenticationFilter@ace45e9,
org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter@62df1f0e,
org.springframework.security.web.savedrequest.RequestCacheAwareFilter@3d1254b9,
org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestFilter@75c2a35,
org.springframework.security.web.authentication.rememberme.RememberMeAuthenticationFilter@605790e5,
org.springframework.security.web.authentication.AnonymousAuthenticationFilter@5c943847,
org.springframework.security.web.session.SessionManagementFilter@39da0e47,
org.springframework.security.web.access.ExceptionTranslationFilter@4f0b02a3,
org.springframework.security.web.access.intercept.FilterSecurityInterceptor@64a0a1c6]
```
#### SB3 order

```
2023-07-31T13:57:59.184+02:00  INFO 218267 --- [           main] o.s.s.web.DefaultSecurityFilterChain     : Will secure any request with [
org.springframework.security.web.session.DisableEncodeUrlFilter@74fa4891,
org.springframework.security.web.context.request.async.WebAsyncManagerIntegrationFilter@28245839,
org.springframework.security.web.context.SecurityContextHolderFilter@207bf6d8,
org.springframework.security.web.header.HeaderWriterFilter@19f72e12,
org.springframework.web.filter.CorsFilter@640c8cd, - disabling CORS does not change the situation
org.springframework.security.web.authentication.logout.LogoutFilter@2ba7828b,
de.aikiit.prototype4.login.LoginTenantAuthenticationFilter@1e0d70db,
org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter@1377b7bf,
org.springframework.security.web.savedrequest.RequestCacheAwareFilter@3dcc59f5,
org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestFilter@16b1dee7, org.springframework.security.web.authentication.AnonymousAuthenticationFilter@38950d4b,
org.springframework.security.web.access.ExceptionTranslationFilter@1990afa2,
org.springframework.security.web.access.intercept.AuthorizationFilter@662754bb]
```
## 2023-08-04: Filed as a spring-boot github issue

Reported the problem as [#36723](https://github.com/spring-projects/spring-boot/issues/36723) - status: was rejected and closed.

## 2023-08-07: Filed as a spring-security github issue

Reported the problem as [#13620](https://github.com/spring-projects/spring-security/issues/13620)

## 2024-01-20: Filed an issue with baeldung

Asked for any hints/advice via baeldung [#15697](https://github.com/eugenp/tutorials/issues/15697)

## 2024-04-23: Issue solved with the help of GlenErrands

GlenErrands analysed the problem and found a possible solution via intense debugging sessions.
Big kudos and thanks for providing a solution to the problem via [PR#106](https://github.com/ottlinger/sb3-filter-problem/pull/106).

## 2025-11-22: Started migrating of SB3 part to SB4

In order to show that the principles applied to the SB3 part are still working with SB4 I started a migration.
