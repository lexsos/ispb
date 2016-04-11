package ispb.radius;

import ispb.base.radius.RadiusServer;

import java.net.DatagramPacket;
import java.net.DatagramSocket;


public class RadiusServerImpl implements RadiusServer {

    private DatagramSocket authSocket = null;

    public void start(){

        try {
            authSocket = new DatagramSocket(1812);
        }
        catch (Throwable e){
            return;
        }

        while (true) {
            DatagramPacket packetIn = new DatagramPacket(new byte[4096], 4096);
            try {
                authSocket.receive(packetIn);
                System.out.print(packetIn.getAddress());
                System.out.print(" ");
                System.out.println(packetIn.getLength());
            }
            catch (Throwable e){

            }
        }
    }

    public void stop(){

    }
}
