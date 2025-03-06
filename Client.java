import Server.Statistics;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Arrays;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        System.out.println("[Client]: Starting");
        try {
            DatagramSocket socket = new DatagramSocket();
            socket.setBroadcast(true);
            byte[] data = "CCS DISCOVER".getBytes();
            DatagramPacket packetToSend = new DatagramPacket(data, data.length, InetAddress.getByName("255.255.255.255"), 5000);
            System.out.println("[Client]: Sending broadcast");
            socket.send(packetToSend);
            System.out.println("[Client]: Broadcast sent");
            DatagramPacket packetToReceive = new DatagramPacket(new byte[1024], 1024);
            System.out.println("[Client]: Waiting for packet");
            socket.receive(packetToReceive);
            System.out.println("[Client]: Server IP received");
            socket.close();
            System.out.println("[Client]: UDP socket closed");
            String messageReceived = new String(packetToReceive.getData(), 0, packetToReceive.getLength());
            System.out.println("[Client]: Received message from server: " + messageReceived + " : " + packetToReceive.getAddress().getHostAddress() + ":" + packetToReceive.getPort());
            Socket clientSocket = new Socket(packetToReceive.getAddress(), packetToReceive.getPort());
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

            while (true) {
                System.out.println("[Client]: Input values: <OPER> <ARG1> <ARG2> or end the connection by sending \"END\"");
                Scanner scanner = new Scanner(System.in);
                String input = scanner.nextLine();

                if (input.split(" ")[0].equals("END")) {
                    out.write("END");
                    out.flush();
                    System.out.println("[Client]: Exiting");
                    in.close();
                    out.close();
                    clientSocket.close();
                    break;
                } else {
                    System.out.println("[Client]: Sending message: " + input);
                    out.write(input);
                    out.newLine();
                    out.flush();
                }
                Thread.sleep(200);
                String line = in.readLine();
                System.out.println("[Client]: Server response: " + line);
            }
            in.close();
            out.close();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
