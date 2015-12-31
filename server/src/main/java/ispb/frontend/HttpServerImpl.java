package ispb.frontend;

import ispb.frontend.servlets.CityServlet;
import ispb.frontend.servlets.LoginServlet;
import ispb.frontend.servlets.LogoutServlet;
import org.eclipse.jetty.server.Server;

import ispb.base.Application;
import ispb.base.frontend.HttpServer;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.util.thread.QueuedThreadPool;

public class HttpServerImpl implements HttpServer {

    private Application application;
    private Server server;
    private HandlerList handlers;

    public HttpServerImpl(Application application){
        this.application =application;
        server = null;
        handlers = new HandlerList();
    }

    public void start() throws Exception {

        createServer();
        addHttpConnector();
        addStaticHandler();
        AddAuthServlet();
        AddApiServlet();

        server.setHandler(handlers);
        server.start();
    }

    public void join() throws InterruptedException {
        if (server != null)
            server.join();
    }

    private void createServer(){
        int maxThreads = application.getConfig().getAsInt("frontend.max_threads");

        QueuedThreadPool threadPool = new QueuedThreadPool();
        threadPool.setMaxThreads(maxThreads);

        server = new Server(threadPool);
    }

    private void addHttpConnector(){

        int httpPort = application.getConfig().getAsInt("frontend.port");
        int idleTimeout = application.getConfig().getAsInt("frontend.idle_timeout");

        ServerConnector http =new ServerConnector(server);
        http.setPort(httpPort);
        http.setIdleTimeout(idleTimeout);

        server.addConnector(http);
    }

    private void addStaticHandler(){
        boolean useStatic = application.getConfig().getAsBool("frontend.use_static");
        String staticDir = application.getConfig().getAsStr("frontend.static_dir");
        String staticPrfix = application.getConfig().getAsStr("frontend.static_prefix");
        String[] welcomeFiles = application.getConfig().getAsStr("frontend.static_welcome_file").split(";");

        if (!useStatic)
            return;

        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setDirectoriesListed(false);
        resourceHandler.setWelcomeFiles(welcomeFiles);
        resourceHandler.setResourceBase(staticDir);

        ContextHandler staticHandler = new ContextHandler();
        staticHandler.setHandler(resourceHandler);
        staticHandler.setContextPath(staticPrfix);

        handlers.addHandler(staticHandler);
    }

    private void AddAuthServlet(){
        ServletContextHandler handler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        handler.setContextPath("/auth/");
        handler.addServlet(LoginServlet.class, "/login/");
        handler.addServlet(LogoutServlet.class, "/logout/");
        handlers.addHandler(handler);
    }

    private void AddApiServlet(){
        ServletContextHandler handler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        handler.setContextPath("/api/0.1/");
        handler.addServlet(CityServlet.class, "/city/*");
        handlers.addHandler(handler);
    }
}
