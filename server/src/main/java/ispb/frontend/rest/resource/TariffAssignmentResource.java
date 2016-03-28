package ispb.frontend.rest.resource;

import ispb.base.db.dataset.TariffAssignmentDataSet;
import ispb.base.db.field.CmpOperator;
import ispb.base.db.filter.DataSetFilter;
import ispb.base.db.filter.DataSetFilterItem;
import ispb.base.db.sort.DataSetSort;
import ispb.base.db.utils.Pagination;
import ispb.base.frontend.rest.*;
import ispb.base.frontend.utils.AccessLevel;
import ispb.base.service.account.TariffAssignmentService;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;


public class TariffAssignmentResource extends RestResource {

    private static class TariffAssignmentEntity extends RestEntity {

        private final Date applyAt;
        private final boolean processed;
        private final long customerId;
        private final long tariffId;

        private final String customerQualifiedName;
        private final String tariffName;
        private final String customerContractNumber;

        public TariffAssignmentEntity(TariffAssignmentDataSet assignment){

            setId(assignment.getId());
            applyAt = assignment.getApplyAt();
            processed = assignment.isProcessed();
            customerId = assignment.getCustomer().getId();
            tariffId = assignment.getTariff().getId();

            tariffName = assignment.getTariff().getName();
            customerContractNumber = assignment.getCustomer().getContractNumber();

            StringBuilder customerName = new StringBuilder();
            customerName.append(assignment.getCustomer().getSurname());
            customerName.append(" ");
            customerName.append(assignment.getCustomer().getName());
            customerName.append(" ");
            customerName.append(assignment.getCustomer().getPatronymic());
            customerQualifiedName = customerName.toString();
        }

        public boolean verify(){
            return false;
        }
    }

    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    private static class TariffListRestResponse extends RestResponse {

        private final List<TariffAssignmentEntity> tariffAssignmentList = new LinkedList<>();
        private final long total;

        @SuppressWarnings("Convert2streamapi")
        public TariffListRestResponse(List<TariffAssignmentDataSet> assignmentList, long total){
            this.total = total;
            for (TariffAssignmentDataSet assignmentDataSet : assignmentList)
                tariffAssignmentList.add(new TariffAssignmentEntity(assignmentDataSet));
        }
    }

    public int getReadAccessLevel(){
        return AccessLevel.MIN;
    }

    public int getWriteAccessLevel(){
        return AccessLevel.MANAGER;
    }

    public Class<? extends RestEntity> getEntityType() {
        return TariffAssignmentEntity.class;
    }

    public RestResponse getEntityList(RestContext restContext){
        TariffAssignmentService service = getTariffAssignmentService(restContext);
        DataSetFilter filter = restContext.getDataSetFilter();
        DataSetSort sort = restContext.getDataSetSort();
        Pagination pagination = restContext.getPagination();
        List<TariffAssignmentDataSet> assignmentList = service.getList(filter, sort, pagination);
        long total = service.getCount(filter);
        return new TariffListRestResponse(assignmentList, total);
    }

    private TariffAssignmentService getTariffAssignmentService(RestContext restContext){
        return restContext.getApplication().getByType(TariffAssignmentService.class);
    }

    protected DataSetFilterItem restToDataSetFilter(RestFilterItem restItem){
        if (restItem.propertyEquals("customerId__eq")) {
            return new DataSetFilterItem("customerId", CmpOperator.EQ, restItem.asLong());
        }
        return null;
    }
}
