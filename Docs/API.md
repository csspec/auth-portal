APPICATION PROGRAMMING INTERFACE (API) - AUTHORIZATION SERVICE 
=====================================
### Get User's Public Information
To get the public information about the user i.e. to access username, e-mail and access level of a user against `userid`, the following endpoint can be used:

```
GET /auth/{userid}
``` 
Example:


### Web Application Flow
This is a description for third party applications.

#### 1. Redirect users to request access their information
```
GET LOGIN_URL/login/oauth/authorize
```
##### Parameters
 Name   |   Type  |   Description |
------  |-------  |------------|
`client_id` | `string` | **Required**. The client ID you received after registering your application or service.
`scope` | `string` | A space delimited list of scopes. If not provided scopes default to empty list 
`state` | `string` | An unguessable random string. It is used to protect against cross-site request forgery attacks.

#### 2. Authorization Portal redirects back to your site
If the user accepts your request, then authorization portal redirects back to callback URL provided during registration of your application with a temporary code in a `code` parameter as well as the `state` you provided in the previous step. If the states don't match, the request has been created by a third party and the process should be aborted.

Exchange this for access token:

```
POST LOGIN_URL/login/oauth/access_token
```
##### Parameters

 Name   |   Type  |   Description |
------  |-------  |------------|
`client_id` | `string` | **Required**. Client ID you received after registration
`client_secret` | `string` | **Required**. Client Secret you received after registration
`code` | `string` | **Required**. Code you received as a response to Step 1
`redirect_url` | `string` | The URL of the application where users will be sent after authorization

##### Response
By default, response will take the following form:
```json
{
  "access_token":"e72e16c7e42f292c6912e7710c838347ae178b4a",
  "scope":"user,email",
  "token_type":"bearer"
}
```

### Scopes
Scopes let you specify exactly what type of access your application needs. Scopes are defined according to the user. For example, qualification is valid only for employee not for students. Thus this scope will have no affect if user is student.

 Name | Description
 -----|------------
 (no scope) | Grants read-only access to public information
 user_write | Grants read/write access to full profile info.
 phone | Grants read only access to phone number.
 email | Grants read only access to email id
 notifications | Grants read access to user's notifications
 user | Grants read/write access to public profile info.
 grades | Grants read only access to grades of the user.
 fee_status | Grants read only access to fee_status of the user.
 time_table | Grants read only access to time table of the user.
 position | Grants read only access to position.
 pay | Grants read only access to pay
 qualification | Grants read only access to qualification of user in case if user is employee.
 all | Only for official applications (read/write access to everything)

### Errors
An error message will be returned in case of invalid requests.
