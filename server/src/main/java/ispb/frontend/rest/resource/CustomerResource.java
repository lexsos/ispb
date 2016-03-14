package ispb.frontend.rest.resource;

import ispb.base.db.container.CustomerContainer;
import ispb.base.db.field.CmpOperator;
import ispb.base.db.fieldtype.CustomerStatus;
import ispb.base.db.fieldtype.CustomerStatusCause;
import ispb.base.db.filter.DataSetFilter;
import ispb.base.db.filter.DataSetFilterItem;
import ispb.base.db.sort.DataSetSort;
import ispb.base.db.utils.Pagination;
import ispb.base.db.view.CustomerSummeryView;
import ispb.base.frontend.rest.*;
import ispb.base.frontend.utils.AccessLevel;
import ispb.base.service.LogService;
import ispb.base.service.account.CustomerAccountService;
import ispb.base.service.account.PaymentService;
import ispb.base.service.account.TariffAssignmentService;
import ispb.base.service.exception.*;
import ispb.base.utils.TextMessages;

import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class CustomerResource extends RestResource {

    private static class CustomerEntity extends RestEntity implements CustomerContainer {

        private String name;
        private String surname;
        private String patronymic;
        private String passport;
        private String phone;
        private String comment;
        private String contractNumber;
        private long buildingId;
        private String room;
        private CustomerStatus status;

        private long streetId;
        private long cityId;
        private String qualifiedName;
        private String qualifiedAddress;
        private double balance;
        private long tariffId;
        private String tariffName;

        public boolean verify(){
            if (getName() != null && getSurname() != null && getPatronymic() != null && getPassport() != null && getPhone() != null &&
                    getComment() != null && getContractNumber() != null && getBuildingId() > 0 && getRoom() != null && getStatus() != null)
                return true;
            return false;
        }

        public CustomerEntity(CustomerSummeryView customer){

            setId(customer.getId());
            setName(customer.getCustomer().getName());
            setSurname(customer.getCustomer().getSurname());
            setPatronymic(customer.getCustomer().getPatronymic());
            setPassport(customer.getCustomer().getPassport());
            setPhone(customer.getCustomer().getPhone());
            setComment(customer.getCustomer().getComment());
            setContractNumber(customer.getCustomer().getContractNumber());
            setBuildingId(customer.getCustomer().getBuilding().getId());
            setRoom(customer.getCustomer().getRoom());

            streetId = customer.getCustomer().getBuilding().getStreet().getId();
            cityId = customer.getCustomer().getBuilding().getStreet().getCity().getId();
            balance = customer.getBalance();
            status = customer.getStatus();

            StringBuilder qualifiedName = new StringBuilder();
            qualifiedName.append(getSurname());
            qualifiedName.append(" ");
            qualifiedName.append(getName());
            qualifiedName.append(" ");
            qualifiedName.append(getPatronymic());
            this.qualifiedName = qualifiedName.toString();

            StringBuilder qualifiedAddress = new StringBuilder();
            qualifiedAddress.append(customer.getCustomer().getBuilding().getStreet().getCity().getName());
            qualifiedAddress.append(", ");
            qualifiedAddress.append(customer.getCustomer().getBuilding().getStreet().getName());
            qualifiedAddress.append(", ");
            qualifiedAddress.append(customer.getCustomer().getBuilding().getName());
            this.qualifiedAddress = qualifiedAddress.toString();

            if (customer.getTariff() != null){
                setTariffId(customer.getTariff().getId());
                setTariffName(customer.getTariff().getName());
            }
        }

        public CustomerEntity(){
        }

        @Override
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String getSurname() {
            return surname;
        }

        public void setSurname(String surname) {
            this.surname = surname;
        }

        @Override
        public String getPatronymic() {
            return patronymic;
        }

        public void setPatronymic(String patronymic) {
            this.patronymic = patronymic;
        }

        @Override
        public String getPassport() {
            return passport;
        }

        public void setPassport(String passport) {
            this.passport = passport;
        }

        @Override
        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        @Override
        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        @Override
        public String getContractNumber() {
            return contractNumber;
        }

        public void setContractNumber(String contractNumber) {
            this.contractNumber = contractNumber;
        }

        @Override
        public long getBuildingId() {
            return buildingId;
        }

        public void setBuildingId(long buildingId) {
            this.buildingId = buildingId;
        }

        @Override
        public String getRoom() {
            return room;
        }

        public void setRoom(String room) {
            this.room = room;
        }

        public long getTariffId() {
            return tariffId;
        }

        public void setTariffId(long tariffId) {
            this.tariffId = tariffId;
        }

        public String getTariffName() {
            return tariffName;
        }

        public void setTariffName(String tariffName) {
            this.tariffName = tariffName;
        }

        public double getBalance() {
            return balance;
        }

        public CustomerStatus getStatus() {
            return status;
        }
    }

    private static class CustomerSummeryListRestResponse extends RestResponse {

        private List<CustomerEntity> customerList;
        private long total;

        public CustomerSummeryListRestResponse(CustomerSummeryView customer){
            customerList = new LinkedList<>();
            customerList.add(new CustomerEntity(customer));
            total = 1;
        }

        public CustomerSummeryListRestResponse(List<CustomerSummeryView> customers, long total){
            customerList = new LinkedList<>();
            this.total = total;
            for (Iterator<CustomerSummeryView> i = customers.iterator(); i.hasNext(); )
                customerList.add(new CustomerEntity(i.next()));
        }

        public List<CustomerEntity> getCustomerList() {
            return customerList;
        }

        public void setCustomerList(List<CustomerEntity> customerList) {
            this.customerList = customerList;
        }
    }

    public int getReadAccessLevel(){
        return AccessLevel.MIN;
    }

    public int getWriteAccessLevel(){
        return AccessLevel.MANAGER;
    }

    public Class getEntityType(){
        return CustomerEntity.class ;
    }

    public RestResponse getEntityList(RestContext restContext){

        CustomerAccountService service = getCustomerService(restContext);
        DataSetFilter filter = restContext.getDataSetFilter();
        DataSetSort sort = restContext.getDataSetSort();
        Pagination pagination = restContext.getPagination();

        long totalCount = service.getCount(filter);
        List<CustomerSummeryView> customers = service.getSummeryList(filter, sort, pagination);
        return new CustomerSummeryListRestResponse(customers, totalCount);
    }

    public RestResponse updateEntity(RestContext restContext){
        CustomerAccountService service = getCustomerService(restContext);
        CustomerEntity entity = (CustomerEntity)restContext.getEntity();
        try {
            CustomerSummeryView customer = service.updateSummery(entity);
            return new CustomerSummeryListRestResponse(customer);
        }
        catch (AlreadyExistException e){
            return ErrorRestResponse.alreadyExist();
        }
        catch (DicElementNotFoundException e){
            return ErrorRestResponse.notFound();
        }
        catch (NotFoundException e){
            return ErrorRestResponse.notFound();
        }
    }

    public RestResponse createEntity(RestContext restContext){
        CustomerAccountService customerService = getCustomerService(restContext);
        TariffAssignmentService assignmentService = getTariffAssignmentService(restContext);
        LogService logService = getLogService(restContext);
        PaymentService paymentService = getPaymentService(restContext);
        TextMessages msg = getTextMessages(restContext);

        CustomerEntity entity = (CustomerEntity)restContext.getEntity();
        CustomerSummeryView customer;

        Date now =  new Date();

        try {
            customer = customerService.createSummery(entity);
        }
        catch (AlreadyExistException e){
            return ErrorRestResponse.alreadyExist();
        }
        catch (DicElementNotFoundException e){
            return ErrorRestResponse.notFound();
        }

        try {
            assignmentService.assignTariff(customer.getId(), entity.getTariffId(), now);
        }
        catch (NotFoundException e){
            logService.warn("Entity not found in data base", e);
        }
        catch (BadDateException e){
            logService.warn("Bad date in tariff assignment", e);
        }

        try {
            String comment = msg.getInitPaymentName(customer.getCustomer().getContractNumber());
            paymentService.addPayment(customer.getId(), entity.getBalance(), comment);
        }
        catch (NotFoundException e){
            logService.warn("Entity not found in data base", e);
        }

        try {
            customerService.setStatus(customer.getId(), entity.getStatus(), CustomerStatusCause.MANAGER, now);
        }
        catch (NotFoundException e){
            logService.warn("Entity not found in data base", e);
        }

        return new  CustomerSummeryListRestResponse(customer);
    }

    protected DataSetFilterItem restToDataSetFilter(RestFilterItem restItem){
        if (restItem.propertyEquals("cityId__eq"))
            return new DataSetFilterItem("cityId", CmpOperator.EQ, restItem.asLong());
        else if (restItem.propertyEquals("streetId__eq"))
            return new DataSetFilterItem("streetId", CmpOperator.EQ, restItem.asLong());
        else if (restItem.propertyEquals("buildingId__eq"))
            return new DataSetFilterItem("buildingId", CmpOperator.EQ, restItem.asLong());
        else if (restItem.propertyEquals("contractNumber__like"))
            return new DataSetFilterItem("contractNumber", CmpOperator.LIKE, restItem.getValue());

        return null;
    }

    private CustomerAccountService getCustomerService(RestContext restContext){
        return restContext.getApplication().getByType(CustomerAccountService.class);
    }

    private TariffAssignmentService getTariffAssignmentService(RestContext restContext){
        return restContext.getApplication().getByType(TariffAssignmentService.class);
    }

    private LogService getLogService(RestContext restContext){
        return restContext.getApplication().getByType(LogService.class);
    }

    private PaymentService getPaymentService(RestContext restContext){
        return restContext.getApplication().getByType(PaymentService.class);
    }

    private TextMessages getTextMessages(RestContext restContext){
        return restContext.getApplication().getByType(TextMessages.class);
    }
}
