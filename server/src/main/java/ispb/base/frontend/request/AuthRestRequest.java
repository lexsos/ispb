package ispb.base.frontend.request;

import ispb.base.frontend.utils.RestRequest;


public class AuthRestRequest extends RestRequest {

    private String userName;
    private String passwd;

    public AuthRestRequest(){
        userName = null;
        passwd = null;
    }

    public AuthRestRequest(String userName, String passwd){
        this.userName = userName;
        this.passwd = passwd;
    }

    public boolean verify(){
        if (userName != null && passwd != null)
            return true;
        return false;
    }

    public void setUserName(String userName){
        this.userName = userName;
    }

    public String getUserName(){
        return userName;
    }

    public void setPasswd(String passwd){
        this.passwd = passwd;
    }

    public String getPasswd(){
        return passwd;
    }
}
