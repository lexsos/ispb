package ispb.base.frontend;

public interface HttpServer {

    void start() throws Exception;
    void join()  throws InterruptedException;
    void stop() throws Exception;
}
