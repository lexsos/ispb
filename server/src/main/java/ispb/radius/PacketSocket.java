package ispb.radius;


import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class PacketSocket {

    private final DatagramSocket serverSocket;
    private final DatagramPacket datagram;

    public PacketSocket(DatagramPacket datagram, DatagramSocket serverSocket){

        this.serverSocket = serverSocket;
        this.datagram = datagram;
    }

    public DatagramSocket getServerSocket() {
        return serverSocket;
    }

    public DatagramPacket getDatagram() {
        return datagram;
    }
}
