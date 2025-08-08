# Client-server commmunication

A basic client-server communication program implemented in Java for my computer science studies at Polish-Japanese Academy of Information Technology  

## Introduction
This program implements a **Centralized Computing System (CCS)**, which functions as a computing server providing three main functionalities:
- Service discovery
- Client communication
- Statistics reporting  

All these features operate simultaneously.

## Application Operation
The application consists of a single program implementing the `CCS` (Centralized Computing System) class.  
It is executed with the following command:
  java -jar CCS.jar <port>

Where `<port>` specifies the UDP/TCP port number.

## Service Discovery
Upon startup, the application:
1. Opens a UDP port specified by the `<port>` parameter.
2. Listens for incoming messages.  
   - A valid discovery message starts with:  
     ```
     CCS DISCOVER
     ```  
     The rest of the content is ignored.
3. When such a message is received, the server responds with 'CCS FOUND'

4. The server then continues listening for more discovery requests.

This mechanism allows a client to detect an active server within the local network by sending a broadcast packet containing the required content.

## Client Communication
After initialization, the application:
1. Opens a TCP port defined by `<port>`.
2. Waits for client connections.
3. Handles client requests in a continuous loop:

1. **Receiving a command** in the format:
   ```
   <OPER> <ARG1> <ARG2>
   ```
   - `<OPER>`: One of `ADD`, `SUB`, `MUL`, `DIV` for integer addition, subtraction, multiplication, or division.
   - `<ARG1>`, `<ARG2>`: Integer arguments for the operation.

2. **Computing the result** based on the provided values.

3. **Sending the result** back to the client as a single integer.  
   If the request is invalid (e.g., division by zero, missing arguments, incorrect operation), the server returns:
   ```
   ERROR
   ```

4. **Displaying operation details** and results in the server console.

5. **Storing the operation** for statistical purposes.

6. Returning to a waiting state for the next request.

Clients may disconnect at any time by closing their sockets. When a client disconnects, its corresponding communication thread terminates.

## Statistics Reporting
The server maintains global statistics, including:
- Total number of connected clients
- Total number of executed operations
- Count of each operation type
- Number of unsuccessful operations
- Sum of all computed results

Every **10 seconds**, the server displays:
- Global statistics (since server start)
- Statistics from the last 10 seconds

## Client
The client application works as follows:
1. Sends a UDP broadcast packet to the known server port.
2. Waits for a `CCS FOUND` response and retrieves the server's IP address.
3. Establishes a TCP connection with the server using the same port.
4. Sends computation requests at random time intervals and waits for responses.
5. May terminate at any time (e.g., by stopping the process).

This enables the client to dynamically discover the server and use its computing capabilities.
