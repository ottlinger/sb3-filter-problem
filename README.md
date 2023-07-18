# sb3-filter-problem
Repo to analyze problems during migration from SpringBoot2 to SpringBoot3

## background

The SB2 application works fine, but SB3 does not allow a login ....

### SB2 features

* login with 3 fields: tenant, username, password
* filter during logout to perform on-logout actions
* i18n

### how to run

```
cd sb2
mvn spring-boot:run
```
Launch the app via [localhost:8080](http://localhost:8080)

### valid credentials

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
