package ispb.frontend.rpc.procedure;

import ispb.base.Application;
import ispb.base.frontend.rpc.RpcArg;
import ispb.base.frontend.rpc.RpcProcedure;
import ispb.base.frontend.utils.AccessLevel;
import ispb.base.service.dictionary.StreetDictionaryService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class StreetNameExistRpc extends RpcProcedure {

    private static class StreetNameExistArgs extends RpcArg {

        private String name;
        private long cityId;

        public boolean verify() {
            return getName() != null && getCityId() > 0;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public long getCityId() {
            return cityId;
        }

        public void setCityId(long cityId) {
            this.cityId = cityId;
        }
    }

    public int getAccessLevel(){
        return AccessLevel.MIN;
    }

    public Class<? extends RpcArg> getArgType() {
        return StreetNameExistArgs.class;
    }

    public Object call(HttpServletRequest request,
                       HttpServletResponse response,
                       RpcArg obj,
                       Application application) throws ServletException, IOException {
        StreetNameExistArgs street = (StreetNameExistArgs)obj;
        StreetDictionaryService service = application.getByType(StreetDictionaryService.class);
        return service.exist(street.getCityId(), street.getName());
    }
}
