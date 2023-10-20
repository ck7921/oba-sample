  
# OBA Sample Project
  
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
    
