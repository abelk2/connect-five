# Connect Five

Simple client-server implementation of the famous Connect Four game in Java,
but with 5 discs.

## Prerequisites 
- Java 17+

## How to run
- First start the server:
    ```shell
    ./gradlew :server:bootRun
    ```
- Then start two instances of the client using:
    ```shell
    ./gradlew :client:run
    ```

## How to run tests
Simply issue the following command:
```shell
./gradlew test
```
It will also generate coverage reports to the `build/reports/jacoco` directory of each module.

## Areas of improvement
- Handle ungraceful disconnects (i.e. when clients do not hit the `/disconnect` endpointt before they exit)
- Cover HTTP client code with tests - OkHttp makes things hard to test; perhaps use something else
- Add read/write locking to backend game logic
- Use truly immutable data structures
