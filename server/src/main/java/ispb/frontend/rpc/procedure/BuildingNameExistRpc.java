package ispb.frontend.rpc.procedure;


import ispb.base.Application;
import ispb.base.frontend.rpc.RpcArg;
import ispb.base.frontend.rpc.RpcProcedure;
import ispb.base.frontend.utils.AccessLevel;
import ispb.base.service.dictionary.BuildingDictionaryService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class BuildingNameExistRpc extends RpcProcedure {

    private static class BuildingNameExistArgs extends RpcArg{
        private String name;
        private long streetId;

        public boolean verify() {
            if (getName() != null && getStreetId() > 0)
                return true;
            return false;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public long getStreetId() {
            return streetId;
        }

        public void setStreetId(long streetId) {
            this.streetId = streetId;
        }
    }

    public int getAccessLevel(){
        return AccessLevel.MIN;
    }

    public Class getArgType() {
        return BuildingNameExistArgs.class;
    }

    public Object call(HttpServletRequest request,
                       HttpServletResponse response,
                       RpcArg obj,
                       Application application) throws ServletException, IOException {
        BuildingNameExistArgs building = (BuildingNameExistArgs)obj;
        BuildingDictionaryService service = application.getByType(BuildingDictionaryService.class);
        return service.exist(building.getStreetId(), building.getName());
    }
}
