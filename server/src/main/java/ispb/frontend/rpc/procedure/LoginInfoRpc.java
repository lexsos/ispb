package ispb.frontend.rpc.procedure;

import ispb.base.Application;
import ispb.base.db.dataset.UserDataSet;
import ispb.base.frontend.rpc.RpcArg;
import ispb.base.frontend.rpc.RpcProcedure;
import ispb.base.frontend.rpc.RpcReturnValue;
import ispb.base.frontend.rpc.VoidArg;
import ispb.base.frontend.utils.AccessLevel;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginInfoRpc extends RpcProcedure {

    private static class LoginInfoValue extends RpcReturnValue {

        private String name = null;
        private String surname = null;
        private String login = null;
        private int accessLevel = AccessLevel.ALL;
        private boolean authed = false;

        public LoginInfoValue(){}

        public LoginInfoValue(UserDataSet user){
            setName(user.getName());
            setSurname(user.getSurname());
            setLogin(user.getLogin());
            setAccessLevel(user.getAccessLevel());
            setAuthed(true);
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSurname() {
            return surname;
        }

        public void setSurname(String surname) {
            this.surname = surname;
        }

        public String getLogin() {
            return login;
        }

        public void setLogin(String login) {
            this.login = login;
        }

        public int getAccessLevel() {
            return accessLevel;
        }

        public void setAccessLevel(int accessLevel) {
            this.accessLevel = accessLevel;
        }

        public boolean isAuthed() {
            return authed;
        }

        public void setAuthed(boolean authed) {
            this.authed = authed;
        }
    }

    public Object call(HttpServletRequest request,
                       HttpServletResponse response,
                       RpcArg obj,
                       Application application) throws ServletException, IOException {

        UserDataSet user = (UserDataSet)request.getSession().getAttribute("user");
        if (user == null)
            return new LoginInfoValue();
        return new LoginInfoValue(user);
    }

    public int getAccessLevel(){
        return AccessLevel.ALL;
    }

    public Class getArgType(){
        return VoidArg.class;
    }

}
