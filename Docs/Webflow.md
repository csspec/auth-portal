# Webflow for client applications

#### 1) Client calls /oauth/authorize for authorization with following parameters

| Parameter | Type | Description |
|-----------| -----|-------------|
| `client_id` | `String` | **Required**. client_id is one that is issued to the application while registration |
| `services` | `String` | **Optional**. Space separated list of services which application want to access. Note application cannot access the service for which it has not registered. If this parameter is not provided, then the services will default to those that were listed in registration form. |
| `redirect_uri` | `String` | **Optional**. Uri to which user will be redirected after completing the authorization |


#### 2) Authorization Portal sends `code` to the `redirect_uri` parameter.
After the user has accepted the request, then a string in parameter `code` will be sent to the `redirect_uri` which can be used to receive `access_token` with following request:

```
    GET /oauth/access_token
```

with following parameters in `x-auth-token` header

| Parameter | Type | Description |
|----------|-------|-------------|
| `client_id` | `String` | **Required**. client_id is one that is issued to the applciation while registration |
| `client_secret` | `String` | **Required**. client_secret is the secret provided to the client while registration |
| `code` | `String` | **Required**. code string which was sent to the redirect_uri. |

Example,
```bash
$ curl -H 'x-auth-token:code=0n4i9&client_id=YOUR_CLIENT_ID&client_secret=YOUR_CLIENT_SECRET' URL/oauth/access_token
{
    "access_token": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwczovL2Nzc3BlYy5vcmciLCJzdWIiOiJwcmluY2UiLCJleHAiOjE0NzYxNjc4OTA2MzcsImlhdCI6MTQ3NjE2NzY3NDYzN30.VjAt2Gk_QyQOBh31iixFqQX3UdDGgKsdYFenteWftwE"
}

```

#### 3) Check the `access_token`'s validity using `/oauth/check_token` endpoint.
```
    GET /oauth/check_token
```
with following parameter in `x-auth-token` header

| Parameter | Type | Description |
|----------|-------|-------------|
| `client_id` | `String` | **Required**. client_id is one that is issued to the applciation while registration |
| `client_secret` | `String` | **Required**. client_secret is the secret provided to the client while registration |

and `access_token` in the `Authorization` header. Example,

```bash
$ set AUTH_TOKEN=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwczovL2Nzc3BlYy5vcmciLCJzdWIiOiJwcmluY2UiLCJleHAiOjE0NzYxNjc4OTA2MzcsImlhdCI6MTQ3NjE2NzY3NDYzN30.VjAt2Gk_QyQOBh31iixFqQX3UdDGgKsdYFenteWftwE
$ curl -H "Authorization: Bearer $AUTH_TOKEN" -H "x-auth-token:client_id=YOUR_CLIENT_ID&client_secret=YOUR_CLIENT_SECRET" AUTH_BASE_URL/oauth/check_token

```
