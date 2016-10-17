# auth-portal
Central Authentication plus Authorization Portal for PEC University of Technology.

Authorization portal is based on OAuth 2.0 framework (Not all features are supported till now).

### Building
To build and run this project, you will need following packages

 1. Java 8
 2. gradle
 3. Node.js (v4.6.0 or higher)

Building this project,
 1. First create some environment variables as described below,
 ```
 $ export ADMIN_ACCOUNT_ID=YOUR_ACCOUNT_ID
 $ export ADMIN_PASSWORD=YOUR_SECRET_PASSWORD
 $ export ADMIN_EMAIL=YOUR_EMAIL_ID
 $ export THIS_CLIENT_ID=example
 $ export THIS_CLIENT_SECRET=YOUR_SECRET
 ```
 (For debugging purposes, you can set these values to anything)

 2. Install npm dependencies,
 ```
 $ npm install
 # npm install -g webpack
 ```

 3. Run webpack to render React Components,
 ```
 $ webpack --watch --progress
 ```

 4. In another terminal, run following command to start server,
 ```
 $ ./gradlew bootRun
 ```
 5. Now open the browser and hit `http://localhost:8080/home`, Enter your username and password as filled above.
