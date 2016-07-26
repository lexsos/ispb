package ispb.frontend.rest.resource;


import ispb.base.db.field.CmpOperator;
import ispb.base.db.filter.DataSetFilterItem;
import ispb.base.frontend.rest.*;
import ispb.base.frontend.utils.AccessLevel;
import ispb.base.radius.server.RadiusServer;
import ispb.base.radius.servlet.RadiusClientRepository;
import ispb.base.radius.servlet.RadiusServlet;
import ispb.base.radius.servlet.descriptor.ParameterDescriptor;
import ispb.base.radius.servlet.descriptor.ServletDescriptor;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class RadiusClientParameterNameResource extends RestResource {

    @SuppressWarnings("unused")
    private static class RadiusClientParameterNameEntity extends RestEntity{

        private final String parameterName;
        private final String displayName;

        public RadiusClientParameterNameEntity(long id, String parameterName, String displayName){
            setId(id);
            this.parameterName = parameterName;
            this.displayName = displayName;
        }

        public boolean verify(){
            return false;
        }
    }

    private static class RadiusClientParameterNameRestResponse extends RestResponse {

        @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
        private final List<RadiusClientParameterNameEntity> radiusClientParameterNameList = new LinkedList<>();

        public RadiusClientParameterNameRestResponse(RadiusServlet servlet){

            ServletDescriptor descriptor = servlet.getDescriptor();
            if (descriptor == null || descriptor.getParameters() == null)
                return;
            Set<String> parameterNames = descriptor.getParameters().keySet();
            int i = 0;
            for (String name: parameterNames){
                ParameterDescriptor parameterDescriptor  = descriptor.getParameter(name);
                if (parameterDescriptor == null)
                    continue;

                RadiusClientParameterNameEntity entity = new RadiusClientParameterNameEntity(++i, name, parameterDescriptor.getDisplay());
                radiusClientParameterNameList.add(entity);
            }
        }
    }

    public int getReadAccessLevel(){
        return AccessLevel.MANAGER;
    }

    public int getWriteAccessLevel(){
        return AccessLevel.MANAGER;
    }

    public Class<? extends RestEntity> getEntityType(){
        return RadiusClientParameterNameEntity.class;
    }

    public RestResponse getEntityList(RestContext restContext){
        DataSetFilterItem filterItem = restContext.getDataSetFilter().getFirst("clientIp");
        if (filterItem == null)
            return null;

        RadiusServer radiusServer = getRadiusServer(restContext);
        RadiusClientRepository clientRepository = radiusServer.getClientRepository();
        RadiusServlet servlet = clientRepository.getServlet(filterItem.getValue().toString());

        if (servlet == null)
            return null;

        return new RadiusClientParameterNameRestResponse(servlet);
    }

    protected DataSetFilterItem restToDataSetFilter(RestFilterItem restItem){
        if (restItem.propertyEquals("clientIp__eq")) {
            return new DataSetFilterItem("clientIp", CmpOperator.EQ, restItem.getValue());
        }
        return null;
    }

    private RadiusServer getRadiusServer(RestContext restContext){
        return restContext.getApplication().getByType(RadiusServer.class);
    }
}
