package Server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPservice implements Runnable {
    private final int port;
    private static int id = 1;

    public TCPservice(int port) {
        this.port = port;
    }

    public void run() {
        System.out.println("[TCP service]: Starting");
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (true) {
                System.out.println("[TCP service " + id + "]: Waiting for incoming connections");
                Socket socket = serverSocket.accept();

                Statistics.clientsCount.getAndIncrement();
                Statistics.tenSec_clientsCount.getAndIncrement();

                System.out.println("[TCP service " + id + "]: Client connected: " + socket.getInetAddress().getHostAddress() + ":" + socket.getPort());
                id++;
                Thread clientCommunicationThread = new Thread(() -> handleClient(socket));
                clientCommunicationThread.start();
            }
        } catch (IOException e) {
            System.out.println("[TCP service " + id + "]: Error: " + e.getMessage());
        }
    }

    private void handleClient(Socket socket) {
        // -1 because id++ in run() method is most of the times faster than this var assignment and because of that I placed id++ before the method invocation and used -1 here
        int id = TCPservice.id - 1;
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            while (true) {
                String line = in.readLine();
                System.out.println("[TCP service " + id + "]: Streams collected");
                System.out.println("[TCP service " + id + "]: Message received: " + line);

                String[] messageElements = line.split(" ");
                double result = 0;
                String messageToSend = null;

                if (messageElements.length == 3) {
                    try {
                        double arg1 = Double.parseDouble(messageElements[1]);
                        double arg2 = Double.parseDouble(messageElements[2]);

                        switch (messageElements[0]) {
                            case "ADD":
                                result = arg1 + arg2;
                                Statistics.addCount.getAndIncrement();
                                Statistics.tenSec_addCount.getAndIncrement();
                                break;
                            case "SUB":
                                result = arg1 - arg2;
                                Statistics.subCount.getAndIncrement();
                                Statistics.tenSec_subCount.getAndIncrement();
                                break;
                            case "MUL":
                                result = arg1 * arg2;
                                Statistics.mulCount.getAndIncrement();
                                Statistics.tenSec_mulCount.getAndIncrement();
                                break;
                            case "DIV":
                                if (arg2 == 0) {
                                    System.out.println("[TCP service " + id + "]: ERROR - Division by zero");
                                    messageToSend = "ERROR - Division by zero";
                                    Statistics.invalidOperationsCount.getAndIncrement();
                                    Statistics.tenSec_invalidOperationsCount.getAndIncrement();
                                } else
                                    result = arg1 / arg2;
                                Statistics.divCount.getAndIncrement();
                                Statistics.tenSec_divCount.getAndIncrement();
                                break;
                            default:
                                System.out.println("[TCP service " + id + "]: ERROR - Invalid operation");
                                messageToSend = "ERROR - Invalid operation";
                                Statistics.invalidOperationsCount.getAndIncrement();
                                Statistics.tenSec_invalidOperationsCount.getAndIncrement();
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("[TCP service " + id + "]: Invalid arguments type");
                        messageToSend = "ERROR - Invalid arguments type";
                        Statistics.invalidOperationsCount.getAndIncrement();
                        Statistics.tenSec_invalidOperationsCount.getAndIncrement();
                    }
                } else {
                    if (messageElements[0].equals("END")) {
                        System.out.println("[TCP service " + id + "]: Communication ended by the client");
                        in.close();
                        out.close();
                        socket.close();
                        return;
                    } else {
                        System.out.println("[TCP service " + id + "]: Invalid message");
                        messageToSend = "ERROR - Invalid message";
                        Statistics.invalidOperationsCount.getAndIncrement();
                        Statistics.tenSec_invalidOperationsCount.getAndIncrement();
                    }
                }
                // messageToSend is null when there was no error and the result was correctly calculated
                if (messageToSend == null) {
                    messageToSend = String.valueOf(result);
                    Statistics.addToSum(result);
                    Statistics.addToTenSecSum(result);
                    Statistics.successfulOperationsCount.getAndIncrement();
                    Statistics.tenSec_successfulOperationsCount.getAndIncrement();
                }
                System.out.println("[TCP service " + id + "]: Sending message: \"" + messageToSend + "\"");
                out.write(messageToSend);
                out.newLine();
                out.flush();
                System.out.println("[TCP service " + id + "]: Result sent to " + socket.getInetAddress().getHostAddress() + ":" + socket.getPort());
            }
        } catch (IOException e) {
            System.out.println("[TCP service " + id + "]: Error with connection - " + e.getMessage());
        }
    }
}

