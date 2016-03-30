package ispb.frontend.rest.resource;

import ispb.base.db.dataset.PaymentGroupDataSet;
import ispb.base.db.filter.DataSetFilter;
import ispb.base.db.sort.DataSetSort;
import ispb.base.db.utils.Pagination;
import ispb.base.frontend.rest.*;
import ispb.base.frontend.utils.AccessLevel;
import ispb.base.service.account.PaymentService;
import ispb.base.service.exception.NotFoundException;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;


public class PaymentGroupResource extends RestResource {

    private static class PaymentGroupEntity extends RestEntity {

        private Date createAt;
        private String comment;

        public PaymentGroupEntity(){}

        public PaymentGroupEntity(PaymentGroupDataSet paymentGroup){
            setId(paymentGroup.getId());
            createAt = paymentGroup.getCreateAt();
            comment = paymentGroup.getComment();
        }

        public boolean verify(){
            return comment != null;
        }

        public String getComment() {
            return comment;
        }
    }

    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    private static class PaymentGroupListRestResponse extends RestResponse {

        private final List<PaymentGroupEntity> paymentGroupList = new LinkedList<>();
        private final long total;


        public PaymentGroupListRestResponse(PaymentGroupDataSet dataSet){
            total = 1;
            paymentGroupList.add(new PaymentGroupEntity(dataSet));
        }

        @SuppressWarnings("Convert2streamapi")
        public PaymentGroupListRestResponse(List<PaymentGroupDataSet> dataSetList, long total){
            this.total = total;
            for (PaymentGroupDataSet groupDataSet : dataSetList)
                paymentGroupList.add(new PaymentGroupEntity(groupDataSet));
        }
    }

    public int getReadAccessLevel(){
        return AccessLevel.MIN;
    }

    public int getWriteAccessLevel(){
        return AccessLevel.KASS;
    }

    public Class<? extends RestEntity> getEntityType() {
        return PaymentGroupEntity.class;
    }

    public RestResponse getEntityList(RestContext restContext){
        PaymentService service = getPaymentService(restContext);
        DataSetFilter filter = restContext.getDataSetFilter();
        DataSetSort sort = restContext.getDataSetSort();
        Pagination pagination = restContext.getPagination();

        long totalCount = service.getPaymentGroupCount(filter);
        List<PaymentGroupDataSet> paymentGroupList = service.getPaymentGroups(filter, sort, pagination);

        return new PaymentGroupListRestResponse(paymentGroupList, totalCount);
    }

    public RestResponse updateEntity(RestContext restContext){
        PaymentService service = getPaymentService(restContext);
        PaymentGroupEntity entity = restContext.getEntityByType(PaymentGroupEntity.class);

        try {
            PaymentGroupDataSet group = service.renamePaymentGroup(entity.getId(), entity.getComment());
            return new PaymentGroupListRestResponse(group);
        }
        catch (NotFoundException e){
            return ErrorRestResponse.notFound();
        }
    }

    private PaymentService getPaymentService(RestContext restContext){
        return restContext.getApplication().getByType(PaymentService.class);
    }
}
