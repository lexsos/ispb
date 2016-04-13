package ispb.base.radius;


import java.net.InetAddress;

public interface RadiusServer {
    void start();
    void stop();
    void clearServlets();
    void addServlet(InetAddress client, RadiusServlet servlet);
}
