# Database Schema Design for Authorization Portal

### Credential Table
In this table the user's credentials will be stored.


**CREDENTIAL**

| Attribute | Type     | Description
| :------------- | :------------- |:------------- |
| userID    | String | Automatically generated ID by the Portal for a particular user (We are using Java's UUID).
| userName  | String | Unique username for every user.
| email     | String | Email id of the user.
| accessLevel | Integer | Access level of the user. It is the value which represents how much access the user has over database and services. It is a integer ranging from 0-10 (0 means most priority and 10 means lowest priority).
| passwordHash | String | Hash of the password generated after applying salt.
| salt  | String | Salt value used.

### Client Table
**NOTE**: Here client refers to another application or service (e.g. Feedback, etc.).
This table stores the information about various services and **registered** applications.

This table is important as some students may want to develop application based on this API. For example a student develops a notification app which notifies about the happenings in the college to other students. So the developer has to register his/her application to Authorization Portal.

After registering he will get a `clientID` and `clientSecret` which he can use to access other APIs.


**CLIENT**

| Attribute | Type | Description
| :------------- | :------------- |:------------- |
| clientID  | String | Unique ID assigned to the application after registration
| clientSecret | String | clientSecret provided to the application
| scope | Array | scope let you specify exactly what type of access you need. Clients could not grant any additional permission beyond that which the user already has.
| URL   | String | URL of the application
| callbackURL | String | URL where the authorization service will redirect after authorization is complete

### ClientInfo Table
**NOTE**: This table will contain information about the client.

**CLIENTINFO**

| Attribute | Type | Description
| :------------- | :------------- |:------------- |
| clientID | String       | Unique identity provided to each client
| clientName | String | Name of the client service/application
| scopes | Array | Array of required scopes to use this service/application
| authorName | String | Name of the author or the developer of the client service
| authorEmail | String | Email ID of the developer of the service/application
| authorContact | String | Contact number of the author of service/application

### Authorization table
**NOTES**: This table is a mapping between UserID and ClientID. It specifies the level of access which a user has given to a certain client.

**AUTHORIZATION**

| Attribute | Type | Description
| :------------- | :------------- |:------------- |
| userID       | String | Unique identity of each user
| clientID    | String | Unique identity of each client service
| accessToken | String | Token which develoer can use to access user information
| allowedScope | Array | Granted scopes to the client

### TemporaryToken

This table stores the data for temporary time. After certain period of time, the entry in the table becomes invalid and is hence removed. This database stores the `code` strings issued to the service/application when the user has accepted the request of the service.

**TEMPORARY**

| Attribute  | Type | Description
| :------------- | :------------- |:------------- |
| userID | String | User ID
| clientID | String | Client ID of the service/application
| accessToken | String | Token a developer can use to access user information on behalf of user
| code | String | Temporary string involved in two-tier registration of the client service. This string is sent to the callback url after user has accepted the request of client.
| timeStamp | Object | Stores the time at which the temporary Code is sent to the client and it is helpful as the temporary code is valid only for a short time period.
  **NOTES**
  + `userID` and `clientID` uniquely defines a userID and ClientID respectively.
  + `accessToken` is a token a developer can use to access user information on behalf of user.
  + `code` is a temporary string involved in two-tier registration of the client service. This string is sent to the callback URL after user has accepted the request of client.
  + `timeStamp` stores the time at which the temporary Code is sent to the client and it is helpful as the temporary code is valid only for a short time period.
