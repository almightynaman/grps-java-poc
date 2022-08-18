### gRPC POC

This is the POC of gRPC between different Spring Boot applications. It provides Restful API endpoint to show the implementation of different types:

- Unary - Client sends a single request and gets back a single response.
- Client-side Stream: Client sends a stream of request and gets back a single response.
- Server-side Stream: Client sends a single request and gets back a stream of response.
- Bi-directional Stream: Stream is used as request and response from Client and server side.

### Module Overview:

There are 4 modules in the application:

- UserService: This will work as a server for gRPC.
  - Provide services to fetch user details.
  - It provides the implementation of Unary, client-side stream, and server-side stream.


- NavigationService - This will work as a server for gRPC.
  - It contains a method to print the remaining distance between source and destination in a drive. 
  - It provides the implementation bi-directional stream.


- ResultService - This will work as a client for gRPC. It also exposes web-side to use User and Navigation services.


- Proto - It contains all the required proto files.
