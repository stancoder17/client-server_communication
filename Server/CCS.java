package Server;

public class CCS {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java-jar CCS.jar <port>");
            System.exit(1);
        }
        Thread UDPservice = new Thread(new UDPservice(Integer.parseInt(args[0])));
        Thread TCPservice = new Thread(new TCPservice(Integer.parseInt(args[0])));
        Thread statisticsThread = new Thread(Statistics.getInstance());
        UDPservice.start();
        TCPservice.start();
        statisticsThread.start();
    }
}
