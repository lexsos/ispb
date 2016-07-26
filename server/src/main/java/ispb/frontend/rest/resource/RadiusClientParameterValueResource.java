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
import ispb.base.radius.servlet.descriptor.ValueDescriptor;

import java.util.LinkedList;
import java.util.List;

public class RadiusClientParameterValueResource extends RestResource {

    @SuppressWarnings("unused")
    private static class RadiusClientParameterValueEntity extends RestEntity {

        private final String parameterValue;
        private final String displayValue;

        public RadiusClientParameterValueEntity(long id, String parameterValue, String displayValue){
            setId(id);
            this.parameterValue = parameterValue;
            this.displayValue = displayValue;
        }

        public boolean verify(){
            return false;
        }
    }

    private static class RadiusClientParameterValueRestResponse extends RestResponse {

        @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
        private final List<RadiusClientParameterValueEntity> radiusClientParameterValueList = new LinkedList<>();

        public RadiusClientParameterValueRestResponse(ParameterDescriptor descriptor){
            if (descriptor == null || descriptor.getValues() == null)
                return;

            int i = 0;
            for (ValueDescriptor valueDescriptor: descriptor.getValues()) {
                RadiusClientParameterValueEntity entity = new RadiusClientParameterValueEntity(++i,
                        valueDescriptor.getValue(), valueDescriptor.getDisplay());
                radiusClientParameterValueList.add(entity);
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
        return RadiusClientParameterValueEntity.class;
    }

    public RestResponse getEntityList(RestContext restContext){
        DataSetFilterItem ipFilter = restContext.getDataSetFilter().getFirst("clientIp");
        DataSetFilterItem parameterFilter = restContext.getDataSetFilter().getFirst("parameterName");
        if (ipFilter == null || parameterFilter == null)
            return null;

        RadiusServer radiusServer = getRadiusServer(restContext);
        RadiusClientRepository clientRepository = radiusServer.getClientRepository();
        RadiusServlet servlet = clientRepository.getServlet(ipFilter.getValue().toString());
        ServletDescriptor servletDescriptor = servlet.getDescriptor();
        ParameterDescriptor descriptor = servletDescriptor.getParameter(parameterFilter.getValue().toString());

        return new RadiusClientParameterValueRestResponse(descriptor);
    }

    protected DataSetFilterItem restToDataSetFilter(RestFilterItem restItem){
        if (restItem.propertyEquals("clientIp__eq")) {
            return new DataSetFilterItem("clientIp", CmpOperator.EQ, restItem.getValue());
        }
        else if (restItem.propertyEquals("parameterName__eq")) {
            return new DataSetFilterItem("parameterName", CmpOperator.EQ, restItem.getValue());
        }
        return null;
    }

    private RadiusServer getRadiusServer(RestContext restContext){
        return restContext.getApplication().getByType(RadiusServer.class);
    }
}
