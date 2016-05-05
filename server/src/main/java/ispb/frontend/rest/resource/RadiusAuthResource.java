package ispb.frontend.rest.resource;

import ispb.base.db.container.RadiusUserContainer;
import ispb.base.db.dataset.CustomerDataSet;
import ispb.base.db.dataset.RadiusUserDataSet;
import ispb.base.db.field.CmpOperator;
import ispb.base.db.filter.DataSetFilter;
import ispb.base.db.filter.DataSetFilterItem;
import ispb.base.db.sort.DataSetSort;
import ispb.base.db.utils.Pagination;
import ispb.base.frontend.rest.*;
import ispb.base.frontend.utils.AccessLevel;
import ispb.base.service.account.RadiusUserService;
import ispb.base.service.exception.AlreadyExistException;
import ispb.base.service.exception.InvalidIpAddressException;
import ispb.base.service.exception.NotFoundException;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;


public class RadiusAuthResource extends RestResource {

    private static class RadiusAuthEntity extends RestEntity implements RadiusUserContainer {

        private Date createAt;
        private String userName;
        private String password;
        private String ip4Address;
        private long customerId;

        private String customerQualifiedName;
        private String customerQualifiedAddress;
        private String contractNumber;
        private long cityId;
        private long streetId;
        private long buildingId;

        public RadiusAuthEntity(){

        }

        public RadiusAuthEntity(RadiusUserDataSet radiusUser){
            setId(radiusUser.getId());
            createAt = radiusUser.getCreateAt();
            userName = radiusUser.getUserName();
            password = radiusUser.getPassword();
            ip4Address = radiusUser.getIp4Address();

            CustomerDataSet customer = radiusUser.getCustomer();
            if (customer != null){
                customerId = customer.getId();
                contractNumber = customer.getContractNumber();

                customerQualifiedAddress = customer.getBuilding().getStreet().getCity().getName();
                customerQualifiedAddress += ", " + customer.getBuilding().getStreet().getName();
                customerQualifiedAddress += ", " + customer.getBuilding().getName();

                customerQualifiedName = customer.getSurname();
                customerQualifiedName += " " + customer.getName();
                customerQualifiedName += " " + customer.getPatronymic();

                cityId = customer.getBuilding().getStreet().getCity().getId();
                streetId = customer.getBuilding().getStreet().getId();
                buildingId = customer.getBuilding().getId();
            }
        }

        public boolean verify(){
            return userName != null && password != null && ip4Address != null && customerId > 0;
        }

        public Date getDeleteAt(){
            return null;
        }

        public Date getCreateAt(){
            return createAt;
        }

        public String getUserName(){
            return userName;
        }

        public String getPassword(){
            return password;
        }

        public String getIp4Address(){
            return ip4Address;
        }

        public Long getCustomerId(){
            if (customerId > 0)
                return customerId;
            return null;
        }

    }

    private static class RadiusAuthRestResponse extends RestResponse {

        @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
        private final List<RadiusAuthEntity> radiusAuthList = new LinkedList<>();
        private final long total;

        public RadiusAuthRestResponse(RadiusUserDataSet radiusUser){
            total = 1;
            radiusAuthList.add(new RadiusAuthEntity(radiusUser));
        }

        @SuppressWarnings("Convert2streamapi")
        public RadiusAuthRestResponse(List<RadiusUserDataSet> radiusUsers, long total){
            this.total = total;
            for (RadiusUserDataSet user: radiusUsers)
                radiusAuthList.add(new RadiusAuthEntity(user));
        }

    }

    public int getReadAccessLevel(){
        return AccessLevel.MIN;
    }

    public int getWriteAccessLevel(){
        return AccessLevel.MANAGER;
    }

    public Class<? extends RestEntity> getEntityType(){
        return RadiusAuthEntity.class;
    }

    public RestResponse getEntityList(RestContext restContext){
        RadiusUserService service = getRadiusUserService(restContext);
        DataSetFilter filter = restContext.getDataSetFilter();
        DataSetSort sort = restContext.getDataSetSort();
        Pagination pagination = restContext.getPagination();

        long totalCount = service.getCount(filter);
        List<RadiusUserDataSet> userList = service.getList(filter, sort, pagination);
        return new RadiusAuthRestResponse(userList, totalCount);
    }

    public RestResponse createEntity(RestContext restContext){
        RadiusUserService service = getRadiusUserService(restContext);
        RadiusAuthEntity entity = restContext.getEntityByType(RadiusAuthEntity.class);
        try {
            RadiusUserDataSet radiusUser = service.create(entity);
            return new RadiusAuthRestResponse(radiusUser);
        }
        catch (AlreadyExistException e){
            return ErrorRestResponse.alreadyExist();
        }
        catch (NotFoundException e){
            return ErrorRestResponse.notFound();
        }
        catch (InvalidIpAddressException e){
            return ErrorRestResponse.incompatibleDataStructure();
        }
    }

    public RestResponse updateEntity(RestContext restContext){
        RadiusUserService service = getRadiusUserService(restContext);
        RadiusAuthEntity entity = restContext.getEntityByType(RadiusAuthEntity.class);
        try {
            RadiusUserDataSet radiusUser = service.update(restContext.getId(), entity);
            return new RadiusAuthRestResponse(radiusUser);
        }
        catch (AlreadyExistException e){
            return ErrorRestResponse.alreadyExist();
        }
        catch (NotFoundException e){
            return ErrorRestResponse.notFound();
        }
        catch (InvalidIpAddressException e){
            return ErrorRestResponse.incompatibleDataStructure();
        }
    }

    public RestResponse deleteEntity(RestContext restContext){
        RadiusUserService service = getRadiusUserService(restContext);
        try {
            service.delete(restContext.getId());
            return new RestResponse();
        }
        catch (NotFoundException e){
            return ErrorRestResponse.notFound();
        }
    }

    protected DataSetFilterItem restToDataSetFilter(RestFilterItem restItem){
        if (restItem.propertyEquals("contractNumber__like")) {
            return new DataSetFilterItem("contractNumber", CmpOperator.LIKE, restItem.getValue());
        }
        else if (restItem.propertyEquals("customer__is_null")) {
            return new DataSetFilterItem("customer", CmpOperator.IS_NULL, null);
        }
        else if (restItem.propertyEquals("customer__is_not_null")) {
            return new DataSetFilterItem("customer", CmpOperator.IS_NOT_NULL, null);
        }
        else if (restItem.propertyEquals("buildingId__eq")) {
            return new DataSetFilterItem("buildingId", CmpOperator.EQ, restItem.asLong());
        }
        else if (restItem.propertyEquals("streetId__eq")) {
            return new DataSetFilterItem("streetId", CmpOperator.EQ, restItem.asLong());
        }
        else if (restItem.propertyEquals("cityId__eq")) {
            return new DataSetFilterItem("cityId", CmpOperator.EQ, restItem.asLong());
        }
        return null;
    }

    private RadiusUserService getRadiusUserService(RestContext restContext){
        return restContext.getApplication().getByType(RadiusUserService.class);
    }

}
