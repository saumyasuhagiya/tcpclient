# Simple Java Spring Integration TCP Client 

A simple TCP client code that expects a stream of data.

## How to run
You can use [a simple Netty tcp server](https://github.com/saumyasuhagiya/NettySimpleTCPServer.git) to send data to this application. 

# Features

- It uses failover client connection factory to connect to two different server simultaneously. 
If one server is down, it will connect to another server.

- Monitors connections actively and reconnects if connection is lost.
