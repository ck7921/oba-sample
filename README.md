  
# OBA Sample Project

## How to run the project?

first configure the following env vars:
* authorizationUserName=<FROM_NAT_WEST> ... the name of the user e.g. 123456@your-ap-domain
* clientSecret=<FROM_NAT_WEST>
* clientId=<FROM_NAT_WEST>

afterwards start spring boot application:
* sample.Main

in the spring boot application properties file (src/main/resources/application.properties) you can configure to use dummy data instead of API.
With the following config local dummy data is used.
* app.data.dev=true
* app.data.transactions.dev=true
if you want to use dummy data with pagination, use:
* app.data.transactions.dev.pagination=true

for login use:
* user: sample.user

always use this user name no mater what has been configured in the env vars. This user is mapped to the user you configure in the env var.
* password: 123456

Keep the password a secret!

## UI issues
    
* ui: spinn has no timeout
* ui: error handling
* ui: balances assumes single page, no pagination
* ui: no handling of back button
* ui: no protection of routse with CanActivate
* ui: consent page is dummy/fake implementation
* ui: string lenght of displayed data neither trimmed nor limited to a certain max len
* ui: inline styles used for beautification 
* ui: only tested in chrome browser

## General Implementation Issues

* impl: url hardcoded in api implementation
* impl: error handling in rest api missing
* impl: error handling in api missing
* impl: logging details without masking
* impl: credentials for remote api in enviroment
* impl: REST no proxy and no request limitter
* impl: balance per account caluclation
* impl: all currencies get rounded to 0.05 even if granularity is available
* impl: UTC timestamp no conversion to user timestamp, kept utc
* impl: txn cause too many api calls, can be optimized to just query one account summary insteaad of all

## Implementation Security Issues

* impl-sec: no spring security
* impl-sec: no CSRF token 
* impl-sec: no CORS
* impl-sec: no secure credential store for app credentials
* impl-sec: app auth token fetched everytime a user logs in
* impl-sec: user auth token cached in http session


## other Issues

* build: version numbers in maven file
    


TODO: env vars for start up adding