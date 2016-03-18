package ispb.frontend.rest.resource;


import ispb.base.db.dataset.CustomerStatusDataSet;
import ispb.base.db.field.CmpOperator;
import ispb.base.db.fieldtype.CustomerStatus;
import ispb.base.db.fieldtype.CustomerStatusCause;
import ispb.base.db.filter.DataSetFilter;
import ispb.base.db.filter.DataSetFilterItem;
import ispb.base.db.sort.DataSetSort;
import ispb.base.db.utils.Pagination;
import ispb.base.frontend.rest.*;
import ispb.base.frontend.utils.AccessLevel;
import ispb.base.service.account.CustomerAccountService;
import ispb.base.service.account.CustomerStatusService;

import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class CustomerStatusResource extends RestResource {

    private static class CustomerStatusEntity extends RestEntity {

        private Date createAt;
        private Date applyAt;
        private boolean processed;
        private CustomerStatus status;
        private CustomerStatusCause cause;
        private long customerId;

        public CustomerStatusEntity(){

        }

        public CustomerStatusEntity(CustomerStatusDataSet statusDataSet){
            setId(statusDataSet.getId());
            createAt = statusDataSet.getCreateAt();
            applyAt = statusDataSet.getApplyAt();
            processed = statusDataSet.isProcessed();
            setStatus(statusDataSet.getStatus());
            cause = statusDataSet.getCause();
            setCustomerId(statusDataSet.getCustomer().getId());
        }

        public boolean verify(){
            if (customerId > 0 && status != null)
                return true;
            return false;
        }

        public CustomerStatus getStatus() {
            return status;
        }

        public void setStatus(CustomerStatus status) {
            this.status = status;
        }

        public long getCustomerId() {
            return customerId;
        }

        public void setCustomerId(long customerId) {
            this.customerId = customerId;
        }
    }

    private static class CustomerStatusListRestResponse extends RestResponse {

        private List<CustomerStatusEntity> statusList;
        private long total;

        public CustomerStatusListRestResponse(List<CustomerStatusDataSet>  dataSetList, long total){
            this.total = total;
            statusList = new LinkedList<>();
            for (Iterator<CustomerStatusDataSet> i = dataSetList.iterator(); i.hasNext(); )
                statusList.add(new CustomerStatusEntity(i.next()));
        }

        public CustomerStatusListRestResponse(CustomerStatusDataSet dataSet){
            total = 0;
            statusList = new LinkedList<>();
            statusList.add(new CustomerStatusEntity(dataSet));
        }
    }

    public int getReadAccessLevel(){
        return AccessLevel.MIN;
    }

    public int getWriteAccessLevel(){
        return AccessLevel.MANAGER;
    }

    public Class getEntityType(){
        return CustomerStatusEntity.class ;
    }

    public RestResponse getEntityList(RestContext restContext){
        DataSetFilter filter = restContext.getDataSetFilter();
        DataSetSort sort = restContext.getDataSetSort();
        Pagination pagination = restContext.getPagination();

        CustomerStatusService service = getCustomerService(restContext);

        List<CustomerStatusDataSet>  statusList = service.getStatusList(filter, sort, pagination);
        long total = service.getStatusCount(filter);

        return new CustomerStatusListRestResponse(statusList, total);
    }

    private CustomerStatusService getCustomerService(RestContext restContext){
        return restContext.getApplication().getByType(CustomerStatusService.class);
    }

    protected DataSetFilterItem restToDataSetFilter(RestFilterItem restItem) {
        if (restItem.propertyEquals("customerId__eq")) {
            return new DataSetFilterItem("customerId", CmpOperator.EQ, restItem.asLong());
        }
        return null;
    }
}
