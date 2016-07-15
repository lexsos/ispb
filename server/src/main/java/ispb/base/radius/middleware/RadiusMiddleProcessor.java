package ispb.base.radius.middleware;


import ispb.base.radius.exception.RadiusException;
import ispb.base.radius.packet.RadiusPacket;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class RadiusMiddleProcessor implements RadiusMiddleIn, RadiusMiddleOut {

    private final List<RadiusMiddleIn> inMiddlewareList = new CopyOnWriteArrayList<>();
    private final List<RadiusMiddleOut> outMiddlewareList = new CopyOnWriteArrayList<>();


    public void in(RadiusPacket request, byte[] secret) throws RadiusException{
        for (RadiusMiddleIn middleIn: inMiddlewareList)
            middleIn.in(request, secret);
    }

    public void in(RadiusPacket request, RadiusPacket reply, byte[] secret) throws RadiusException{
        for (RadiusMiddleIn middleIn: inMiddlewareList)
            middleIn.in(request, reply, secret);
    }

    public void out(RadiusPacket request, byte[] secret) throws RadiusException{
        for (RadiusMiddleOut middleOut: outMiddlewareList)
            middleOut.out(request, secret);
    }

    public void out(RadiusPacket request, RadiusPacket reply, byte[] secret) throws RadiusException{
        for (RadiusMiddleOut middleOut: outMiddlewareList)
            middleOut.out(request, reply, secret);
    }

    public void addIn(RadiusMiddleIn inMiddleware){
        inMiddlewareList.add(inMiddleware);
    }

    public void addOut(RadiusMiddleOut outMiddleware){
        outMiddlewareList.add(outMiddleware);
    }
}
