package ispb.base.radius.servlet;

import ispb.ApplicationImpl;
import ispb.base.Application;
import ispb.base.db.dataset.RadiusClientDataSet;
import ispb.base.radius.packet.RadiusPacket;
import ispb.base.radius.servlet.descriptor.ServletDescriptor;
import ispb.base.resources.AppResources;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RadiusServlet {

    private final Map<String, String> parameters = new ConcurrentHashMap<>();
    private ServletDescriptor descriptor;

    public RadiusServlet(){
        loadParameters();
    }

    public String getSharedSecret(RadiusClientDataSet clientDataSet){
        return clientDataSet.getSecret();
    }

    public RadiusPacket service(RadiusServletContext context){
        RadiusPacket request = context.getRequest();

        if (request.getPacketType() == RadiusPacket.ACCESS_REQUEST)
            return access(context);
        else if (request.getPacketType() == RadiusPacket.ACCOUNTING_REQUEST)
            return accounting(context);

        return null;
    }

    public void setParameter(String parameter, String value){
        parameters.put(parameter, value);
    }

    public String getParameter(String parameter){
        if (parameters.containsKey(parameter))
            return parameters.get(parameter);
        else if (descriptor != null && descriptor.getParameter(parameter) != null)
            return descriptor.getParameter(parameter).getDefaultValue();
        return null;
    }

    public ServletDescriptor getDescriptor(){
        return descriptor;
    }

    @SuppressWarnings({"SameReturnValue", "UnusedParameters"})
    protected RadiusPacket accounting(RadiusServletContext context){
        return null;
    }

    @SuppressWarnings({"SameReturnValue", "UnusedParameters"})
    protected RadiusPacket access(RadiusServletContext context){
        return null;
    }

    protected void loadParameters(){
        Application application = ApplicationImpl.getApplication();
        AppResources resources = application.getByType(AppResources.class);
        Class clazz = this.getClass();
        String fileName = clazz.getSimpleName() + "_descriptor.json";
        try {
            descriptor = resources.getJsonAsObject(clazz, fileName, ServletDescriptor.class);
        }
        catch (Throwable throwable){
            descriptor = null;
        }
    }
}
