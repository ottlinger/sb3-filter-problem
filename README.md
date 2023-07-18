# sb3-filter-problem
Repo to analyze problems during migration from SpringBoot2 to SpringBoot3

## background

The SB2 application works fine, but SB3 does not allow a login ....

### SB2

* login with 3 fields: tenant, username, password
* filter during logout
* i18n

### how to run

```
cd sb2
mvn spring-boot:run
```

### valid credentials

You may use the following combination in order to successfully login:

| Item              | In Stock | Price |
| :---------------- | :------: | ----: |
| Python Hat        |   True   | 23.99 |
| SQL Hat           |   True   | 23.99 |
| Codecademy Tee    |  False   | 19.99 |
| Codecademy Hoodie |  False   | 42.99 |

