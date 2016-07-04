package ispb.base.radius;


import ispb.base.db.dataset.RadiusClientDataSet;
import ispb.base.service.LogService;
import org.tinyradius.packet.AccessRequest;
import org.tinyradius.packet.AccountingRequest;
import org.tinyradius.packet.RadiusPacket;

public class RadiusServlet {

    private final LogService logService;

    public RadiusServlet(LogService logService){
        this.logService = logService;
    }

    public RadiusPacket service(RadiusPacket request, RadiusClientDataSet clientDataSet){

        if (request instanceof AccessRequest)
            return access((AccessRequest)request, clientDataSet);
        else if (request instanceof AccountingRequest)
            return accounting((AccountingRequest)request, clientDataSet);

        return null;
    }

    public String getSharedSecret(RadiusClientDataSet clientDataSet){
        return clientDataSet.getSecret();
    }

    protected LogService getLogService(){
        return logService;
    }

    protected RadiusPacket access(AccessRequest request, RadiusClientDataSet clientDataSet){
        return null;
    }

    protected RadiusPacket accounting(AccountingRequest request, RadiusClientDataSet clientDataSet){
        return null;
    }

    protected RadiusPacket makeAccessAccept(RadiusPacket request){
        return new RadiusPacket(RadiusPacket.ACCESS_ACCEPT, request.getPacketIdentifier());
    }

    protected RadiusPacket makeAccessReject(RadiusPacket request){
        return new RadiusPacket(RadiusPacket.ACCESS_REJECT, request.getPacketIdentifier());
    }

    protected RadiusPacket makeAccountingResponse(RadiusPacket request){
        return new RadiusPacket(RadiusPacket.ACCOUNTING_RESPONSE, request.getPacketIdentifier());
    }

    protected void addAttributes(RadiusPacket radiusPacket, Iterable<? extends RadiusAttribute> attributeList){
        for (RadiusAttribute attribute: attributeList)
            try {
                radiusPacket.addAttribute(attribute.getAttributeName(), attribute.getAttributeValue());
            }
            catch (Throwable e){
                getLogService().warn("Error while add RADIUS attribute " + attribute.getAttributeName() + " to RADIUS packet", e);
            }
    }
}
