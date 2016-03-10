package ispb.frontend.rest.resource;

import ispb.base.db.dataset.TariffAssignmentDataSet;
import ispb.base.db.filter.DataSetFilter;
import ispb.base.db.sort.DataSetSort;
import ispb.base.db.utils.Pagination;
import ispb.base.frontend.rest.RestContext;
import ispb.base.frontend.rest.RestEntity;
import ispb.base.frontend.rest.RestResource;
import ispb.base.frontend.rest.RestResponse;
import ispb.base.frontend.utils.AccessLevel;
import ispb.base.service.account.TariffAssignmentService;

import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


public class TariffAssignmentResource extends RestResource {

    private static class TariffAssignmentEntity extends RestEntity {

        private Date applyAt;
        private boolean processed;
        private long customerId;
        private long tariffId;

        private String customerQualifiedName;
        private String tariffName;
        private String customerContractNumber;

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

    private static class TariffListRestResponse extends RestResponse {

        private List<TariffAssignmentEntity> tariffAssignmentList;

        public TariffListRestResponse(List<TariffAssignmentDataSet> assignmentList){
            tariffAssignmentList = new LinkedList<>();
            for (Iterator<TariffAssignmentDataSet> i=assignmentList.iterator(); i.hasNext();)
                tariffAssignmentList.add(new TariffAssignmentEntity(i.next()));
        }
    }

    public int getReadAccessLevel(){
        return AccessLevel.MIN;
    }

    public int getWriteAccessLevel(){
        return AccessLevel.MANAGER;
    }

    public Class getEntityType() {
        return TariffAssignmentEntity.class;
    }

    public RestResponse getEntityList(RestContext restContext){
        TariffAssignmentService service = getTariffAssignmentService(restContext);
        DataSetFilter filter = restContext.getDataSetFilter();
        DataSetSort sort = restContext.getDataSetSort();
        Pagination pagination = restContext.getPagination();
        List<TariffAssignmentDataSet> assignmentList = service.getList(filter, sort, pagination);
        return new TariffListRestResponse(assignmentList);
    }

    private TariffAssignmentService getTariffAssignmentService(RestContext restContext){
        return restContext.getApplication().getByType(TariffAssignmentService.class);
    }
}
