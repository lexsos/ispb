package ispb.base.frontend.rpc;


public class Ip4AddressArgs extends RpcArg {

    private String ip4Address;

    public Ip4AddressArgs(){

    }

    public Ip4AddressArgs(String ip4Address){
        this.ip4Address = ip4Address;
    }

    public boolean verify() {
        return getIp4Address() != null;
    }

    public String getIp4Address() {
        return ip4Address;
    }
}
