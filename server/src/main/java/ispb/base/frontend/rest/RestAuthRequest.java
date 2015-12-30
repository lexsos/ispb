package ispb.base.frontend.rest;

import ispb.base.frontend.utils.RestBase;
import ispb.base.frontend.utils.Verifiable;


public class RestAuthRequest extends RestBase implements Verifiable {

    private String userName;
    private String passwd;

    public RestAuthRequest(){
        userName = null;
        passwd = null;
    }

    public RestAuthRequest(String userName, String passwd){
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
