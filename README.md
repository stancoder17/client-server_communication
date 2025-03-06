# client-server_communication
a basic client-server communication program implemented in Java for a university project


= Introduction = 
The program implements a Centralized Computing System, which functions as a computing server providing three key functionalities: service detection, client communication, and statistics reporting. All these functionalities operate simultaneously.

= Application Operation =
The application consists of a single program implementing the CCS (Centralized Computing System) class, executed with the following command: 'java -jar CCS.jar <port>', where <port> is a number defining the UDP/TCP port number.

= Service Discovery =
Upon startup, the application opens a UDP port specified by the <port> parameter and listens for incoming messages. A valid message starts with the line CCS DISCOVER, the remaining content is irrelevant. Upon receiving such a message, the server responds to the sender with CCS FOUND and resumes listening on the UDP socket.
This functionality allows a client to detect an active server within the local network by sending a broadcast packet with the required content.

= Client Communication =
After initialization, the application opens a TCP port defined by <port> and waits for client connections. The client handling process consists of the following cyclic steps:

1. Receiving a command from the client in the format: <OPER> <ARG1> <ARG2>
Where <OPER> represents one of the operations: ADD, SUB, MUL, DIV, corresponding to addition, subtraction, multiplication, or division for integer values.
<ARG1> and <ARG2> are integer-type arguments for the operation.

2. Computing the result based on the received values.

3. Sending the computed result back to the client as a single integer or returning ERROR in case of an invalid operation (e.g., division by zero, missing arguments, or an incorrect operation code).

4. Displaying information about the received operation and its result on the console.

5. Storing data for statistical purposes.

6. Returning to a waiting state for the next client request.

Clients can disconnect at any time by closing their sockets. When this occurs, the respective communication thread terminates.

= Statistics Reporting =
Throughout its operation, the server collects global statistics, including:
- Number of newly connected clients,
- Number of executed operations,
- Count of each operation type,
- Number of unsuccessful operations,
- Sum of all computed results.
  
Every 10 seconds, the server outputs both global statistics and statistics covering the last 10 seconds of operation to the console.

= Client =
The client application interacts with the server according to the following steps:
1. Sending a UDP broadcast packet within the local network to the known server port.

2. Waiting for a CCS FOUND response and extracting the server's IP address.

3. Establishing a TCP connection with the server using the same port that was used for discovering service.

4. Sending computation requests to the server at random time intervals and waiting for responses.

5. Terminating execution at any time (e.g., by killing the client process).

6. This allows the client to dynamically communicate with the server and utilize its computing capabilities.

