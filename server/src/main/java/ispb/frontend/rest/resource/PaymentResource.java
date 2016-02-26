package ispb.frontend.rest.resource;


import ispb.base.db.dataset.PaymentDataSet;
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

public class PaymentResource extends RestResource {

    private static class PaymentEntity extends RestEntity {

        private Date createAt;
        private Date applyAt;
        private long customerId;
        private double paymentSum;
        private boolean processed;
        private long paymentGroupId;

        private String customerQualifiedName;
        private String paymentGroupComment;
        private String customerContractNumber;

        public PaymentEntity(){

        }

        public PaymentEntity(PaymentDataSet payment){
            setId(payment.getId());
            createAt = payment.getCreateAt();
            applyAt = payment.getApplyAt();
            customerId = payment.getCustomer().getId();
            paymentSum = payment.getPaymentSum();
            processed = payment.isProcessed();
            paymentGroupId = payment.getGroup().getId();

            paymentGroupComment = payment.getGroup().getComment();
            customerContractNumber = payment.getCustomer().getContractNumber();

            StringBuilder customerName = new StringBuilder();
            customerName.append(payment.getCustomer().getName());
            customerName.append(" ");
            customerName.append(payment.getCustomer().getSurname());
            customerName.append(" ");
            customerName.append(payment.getCustomer().getPatronymic());
            customerQualifiedName = customerName.toString();
        }

        public boolean verify(){
            return false;
        }

    }

    private static class PaymentListRestResponse extends RestResponse {

        private List<PaymentEntity> paymentList;
        private long total;

        public PaymentListRestResponse(PaymentDataSet payment){
            paymentList = new LinkedList<>();
            paymentList.add(new PaymentEntity(payment));
            total = 1;
        }

        public PaymentListRestResponse(List<PaymentDataSet> payments, long total){
            this.total = total;
            paymentList = new LinkedList<>();
            for (Iterator<PaymentDataSet> i = payments.iterator(); i.hasNext(); )
                paymentList.add(new PaymentEntity(i.next()));
        }

    }

    public int getReadAccessLevel(){
        return AccessLevel.MIN;
    }

    public int getWriteAccessLevel(){
        return AccessLevel.KASS;
    }

    public Class getEntityType(){
        return PaymentEntity.class ;
    }

    public RestResponse getEntityList(RestContext restContext){
        PaymentService service = getPaymentService(restContext);
        DataSetFilter filter = restContext.getDataSetFilter();
        DataSetSort sort = restContext.getDataSetSort();
        Pagination pagination = restContext.getPagination();

        long totalCount = service.getPaymentCount(filter);
        List<PaymentDataSet>  paymentList = service.getPayments(filter, sort, pagination);
        return new PaymentListRestResponse(paymentList, totalCount);
    }

    private PaymentService getPaymentService(RestContext restContext){
        return restContext.getApplication().getByType(PaymentService.class);
    }

    protected DataSetFilterItem restToDataSetFilter(RestFilterItem restItem){
        if (restItem.propertyEquals("customerId__eq")) {
            return new DataSetFilterItem("customerId", CmpOperator.EQ, restItem.asLong());
        }
        return null;
    }
}
