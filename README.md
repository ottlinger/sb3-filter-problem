# sb3-filter-problem
Repo to analyze problems during migration from SpringBoot2 (SB2) to SpringBoot3 (SB3)
The SB2 application works fine, but SB3 does not allow a login ....
the app has 3 elements for login (tenant, username and password) and a logout handler performs operations upon successful logout.
Both apps contain no tests as they are extracted and adapted from a different (private) project.

### Feature comparison between SB2 and SB3 version of the same application

| Description                                                                                         |                                             SB2                                              | SB3                                                                                                                            | Status SB2                       |   Status SB3    |
|:----------------------------------------------------------------------------------------------------|:--------------------------------------------------------------------------------------------:|:-------------------------------------------------------------------------------------------------------------------------------|:---------------------------------:|:---------------:|
| ApplicationUser to encapsulate tenant, user, password for login                                     |     [ApplicationUser](./sb2/src/main/java/de/aikiit/prototype/user/ApplicationUser.java)     | [ApplicationUser](./sb3/src/main/java/de/aikiit/prototype3/user/ApplicationUser.java)                                          | 👍 working                       |   👍 working    |
| LoginTenantAuthenticationFilter (UsernamePasswordAuthenticationFilter) to extract data upon login   |     [LoginTenantAuthenticationFilter](./sb2/src/main/java/de/aikiit/prototype/login/LoginTenantAuthenticationFilter.java)     | [LoginTenantAuthenticationFilter](./sb3/src/main/java/de/aikiit/prototype3/login/LoginTenantAuthenticationFilter.java)         | 👍 working                       |   👍 working    |
| DataSeeding, mechanism to create example tenant, user, password combinations upon application start | [Package seeding](./sb2/src/main/java/de/aikiit/prototype/seeding/BootstrapDataCreator.java) | [Package Seeding](./sb3/src/main/java/de/aikiit/prototype3/seeding/BootstrapDataCreator.java)                                  | 👍 working                       |   👍 working    |
| Authentication configuration                                                                        |     [AuthenticationConfiguration](./sb2/src/main/java/de/aikiit/prototype/configuration/AuthenticationConfiguration.java)                                | [AuthenticationConfiguration](./sb3/src/main/java/de/aikiit/prototype3/configuration/AuthenticationConfiguration.java)         | 👍 working                       | 🔥 not working  |
| SimpleUrlLogoutSuccessHandler to trigger actions upon logout                                        |     [LeaveEventsUponLogoutSuccessHandler](./sb2/src/main/java/de/aikiit/prototype/login/LeaveEventsUponLogoutSuccessHandler.java)                                | [LeaveEventsUponLogoutSuccessHandler](./sb3/src/main/java/de/aikiit/prototype3/login/LeaveEventsUponLogoutSuccessHandler.java) | 👍 working                       | 🔥 not working  |
| Authentication configuration                                                                        |     [AuthenticationConfiguration](./sb2/src/main/java/de/aikiit/prototype/configuration/AuthenticationConfiguration.java)                                | [AuthenticationConfiguration](./sb3/src/main/java/de/aikiit/prototype3/configuration/AuthenticationConfiguration.java)         | 👍 working                       | 🔥 not working  |

./sb2/src/main/java/de/aikiit/prototype/login/LeaveEventsUponLogoutSuccessHandler.java

* login with 3 fields: tenant, username, password
* filter during logout to perform on-logout actions
* i18n

## How to run the app

### Working application with SB2

```
cd sb2 
mvn spring-boot:run
```
Launch the app via [localhost:8080](http://localhost:8080)
and log in.
After a successful login you may logout again.

### Problematic application with SB3

```
cd sb3 
mvn spring-boot:run
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
