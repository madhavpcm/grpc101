# gRPC 101

This repository hosts a basic gRPC server and client, implemented in java. 

The source code of interest lies in the `app/src` directory and has the following hierarchy.

```dtd
app/src
├── main
│   ├── java
│   │   └── org
│   │       └── example
│   │           ├── Book.java
│   │           ├── DBHandler.java
│   │           ├── GRPCClient.java
│   │           ├── GRPCServer.java
│   │           └── SimpleFormatter.java
│   ├── proto
│   │   └── task.proto
│   └── resources
└── test
    ├── java
    │   └── org
    │       └── example
    │           └── RPCTest.java
    └── resources
```
## Build

The project is managed using `gradle`, use the `gradlew` in the repository to manage builds.

```bash
git clone https://github.com/madhavpcm/grpc101.git
cd grpc101
./gradlew build
```

## Run

```bash
# Run Server
./gradlew runServer

# Run Client in another terminal window
./gradlew runClient
# outputs 

```

## Test

By default the `./gradlew build` runs the junit test files, however you can run `./gradlew test` to run all tests manually


## Docs

JavaDoc files are generated in the `app/build/docs` directory. The docs specify the details of the implemented RPC handlers.

```
./gradlew javadoc
```