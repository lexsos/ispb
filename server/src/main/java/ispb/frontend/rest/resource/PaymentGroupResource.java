package ispb.frontend.rest.resource;

import ispb.base.db.dataset.PaymentGroupDataSet;
import ispb.base.db.field.CmpOperator;
import ispb.base.db.filter.DataSetFilter;
import ispb.base.db.filter.DataSetFilterItem;
import ispb.base.db.sort.DataSetSort;
import ispb.base.db.utils.Pagination;
import ispb.base.frontend.rest.*;
import ispb.base.frontend.utils.AccessLevel;
import ispb.base.service.account.PaymentService;

import java.util.Date;
import java.util.Iterator;
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
            return false;
        }
    }

    private static class PaymentListRestResponse extends RestResponse {

        private List<PaymentGroupEntity> paymentGroupList;
        private long total;


        public PaymentListRestResponse(PaymentGroupDataSet dataSet){
            total = 1;
            paymentGroupList = new LinkedList<>();
            paymentGroupList.add(new PaymentGroupEntity(dataSet));
        }

        public PaymentListRestResponse(List<PaymentGroupDataSet> dataSetList, long total){
            this.total = total;
            paymentGroupList = new LinkedList<>();
            for (Iterator<PaymentGroupDataSet> i = dataSetList.iterator(); i.hasNext(); )
                paymentGroupList.add(new PaymentGroupEntity(i.next()));
        }
    }

    public int getReadAccessLevel(){
        return AccessLevel.MIN;
    }

    public int getWriteAccessLevel(){
        return AccessLevel.KASS;
    }

    public Class getEntityType() {
        return PaymentGroupEntity.class;
    }

    public RestResponse getEntityList(RestContext restContext){
        PaymentService service = getPaymentService(restContext);
        DataSetFilter filter = restContext.getDataSetFilter();
        DataSetSort sort = restContext.getDataSetSort();
        Pagination pagination = restContext.getPagination();

        long totalCount = service.getPaymentGroupCount(filter);
        List<PaymentGroupDataSet> paymentGroupList = service.getPaymentGroups(filter, sort, pagination);

        return new PaymentListRestResponse(paymentGroupList, totalCount);
    }

    private PaymentService getPaymentService(RestContext restContext){
        return restContext.getApplication().getByType(PaymentService.class);
    }
}
