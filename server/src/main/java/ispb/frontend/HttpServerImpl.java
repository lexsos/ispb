package ispb.frontend;

import ispb.base.resources.Config;
import ispb.frontend.rest.RestServlet;
import ispb.frontend.rpc.RpcServlet;
import org.eclipse.jetty.server.Server;

import ispb.base.frontend.HttpServer;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.util.thread.QueuedThreadPool;

public class HttpServerImpl implements HttpServer {

    private Server server;
    private final HandlerList handlers;
    private final Config config;

    public HttpServerImpl(Config config){
        this.config = config;
        server = null;
        handlers = new HandlerList();
    }

    public void start() throws Exception {

        createServer();
        addHttpConnector();
        addStaticHandler();
        addApiServlets();

        server.setHandler(handlers);
        server.start();
    }

    public void join() throws InterruptedException {
        if (server != null)
            server.join();
    }

    public void stop() throws Exception{
        server.stop();
    }

    private void createServer(){
        int maxThreads = config.getAsInt("frontend.max_threads");

        QueuedThreadPool threadPool = new QueuedThreadPool();
        threadPool.setMaxThreads(maxThreads);

        server = new Server(threadPool);
    }

    private void addHttpConnector(){

        int httpPort = config.getAsInt("frontend.port");
        int idleTimeout = config.getAsInt("frontend.idle_timeout");

        ServerConnector http =new ServerConnector(server);
        http.setPort(httpPort);
        http.setIdleTimeout(idleTimeout);

        server.addConnector(http);
    }

    private void addStaticHandler(){
        boolean useStatic = config.getAsBool("frontend.use_static");
        String staticDir = config.getAsStr("frontend.static_dir");
        String staticPrefix = config.getAsStr("frontend.static_prefix");
        String[] welcomeFiles = config.getAsStr("frontend.static_welcome_file").split(";");

        if (!useStatic)
            return;

        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setDirectoriesListed(false);
        resourceHandler.setWelcomeFiles(welcomeFiles);
        resourceHandler.setResourceBase(staticDir);

        ContextHandler staticHandler = new ContextHandler();
        staticHandler.setHandler(resourceHandler);
        staticHandler.setContextPath(staticPrefix);

        handlers.addHandler(staticHandler);
    }

    private void addApiServlets(){
        ServletContextHandler handler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        handler.setContextPath("/api");
        handler.addServlet(RestServlet.class, "/rest/*");
        handler.addServlet(RpcServlet.class, "/rpc/*");
        handlers.addHandler(handler);
    }
}
