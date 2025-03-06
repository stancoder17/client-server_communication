package Server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class UDPservice extends Thread {
    private final int port;

    public UDPservice(int port) {
        this.port = port;
    }

    public void run() {
        System.out.println("[UDP service]: Starting");
        try (DatagramSocket socket = new DatagramSocket(port)) {
            while (true) {
                System.out.println("[UDP service]: Waiting for incoming packets");
                DatagramPacket packetToReceive = new DatagramPacket(new byte[1024], 1024);

                socket.receive(packetToReceive);
                System.out.println("[UDP service]: Received packet from: " + packetToReceive.getAddress().getHostAddress() + ":" + packetToReceive.getPort());

                String message = new String(packetToReceive.getData(), 0, packetToReceive.getLength());

                if (message.startsWith("CCS DISCOVER")) {
                    System.out.println("[UDP service]: Message starts with \"CCS DISCOVER\"");
                    byte[] buffer = "CCS FOUND".getBytes();
                    DatagramPacket packetToSend = new DatagramPacket(buffer, buffer.length, packetToReceive.getAddress(), packetToReceive.getPort());
                    socket.send(packetToSend);
                    System.out.println("[UDP service]: Sending \"CCS FOUND\" packet to: " + packetToSend.getAddress().getHostAddress() + ":" + packetToSend.getPort());
                }
            }
        } catch (IOException e) {
            System.out.println("[UDP service]: ERROR - " + e.getMessage());
        }
    }
}

