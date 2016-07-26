package ispb.base.radius.server;


import ispb.base.radius.servlet.RadiusClientRepository;

public interface RadiusServer {
    void start();
    void stop();
    boolean isStarted();

    void setClientRepository(RadiusClientRepository repository);
    RadiusClientRepository getClientRepository();
}
