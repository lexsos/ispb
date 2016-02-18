package ispb.frontend.rest.resource;

import ispb.base.db.field.CmpOperator;
import ispb.base.db.filter.DataSetFilter;
import ispb.base.db.filter.DataSetFilterItem;
import ispb.base.db.sort.DataSetSort;
import ispb.base.db.utils.Pagination;
import ispb.base.db.view.CustomerSummeryView;
import ispb.base.frontend.rest.*;
import ispb.base.frontend.utils.AccessLevel;
import ispb.base.service.account.CustomerAccountService;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class CustomerResource extends RestResource {

    private static class CustomerEntity extends RestEntity {

        private String name;
        private String surname;
        private String patronymic;
        private String passport;
        private String phone;
        private String comment;
        private String contractNumber;
        private long buildingId;
        private String room;

        private String buildingName;
        private String streetName;
        private String cityName;
        private String fullName;

        public boolean verify(){
            if (name != null && surname != null && patronymic != null && passport != null && phone != null &&
                    comment != null && contractNumber != null && buildingId > 0 && room != null)
                return true;
            return false;
        }

        public CustomerEntity(CustomerSummeryView customer){

            setId(customer.getId());
            name = customer.getCustomer().getName();
            surname = customer.getCustomer().getSurname();
            patronymic = customer.getCustomer().getPatronymic();
            passport = customer.getCustomer().getPassport();
            phone = customer.getCustomer().getPhone();
            comment = customer.getCustomer().getComment();
            contractNumber = customer.getCustomer().getContractNumber();
            buildingId = customer.getCustomer().getBuilding().getId();
            room = customer.getCustomer().getRoom();

            buildingName = customer.getCustomer().getBuilding().getName();
            streetName = customer.getCustomer().getBuilding().getStreet().getName();
            cityName = customer.getCustomer().getBuilding().getStreet().getCity().getName();

            StringBuilder fullName = new StringBuilder();
            fullName.append(surname);
            fullName.append(" ");
            fullName.append(name);
            fullName.append(" ");
            fullName.append(patronymic);
            this.fullName = fullName.toString();
        }

        public CustomerEntity(){
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSurname() {
            return surname;
        }

        public void setSurname(String surname) {
            this.surname = surname;
        }

        public String getPatronymic() {
            return patronymic;
        }

        public void setPatronymic(String patronymic) {
            this.patronymic = patronymic;
        }

        public String getPassport() {
            return passport;
        }

        public void setPassport(String passport) {
            this.passport = passport;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        public String getContractNumber() {
            return contractNumber;
        }

        public void setContractNumber(String contractNumber) {
            this.contractNumber = contractNumber;
        }

        public long getBuildingId() {
            return buildingId;
        }

        public void setBuildingId(long buildingId) {
            this.buildingId = buildingId;
        }

        public String getRoom() {
            return room;
        }

        public void setRoom(String room) {
            this.room = room;
        }

        public String getBuildingName() {
            return buildingName;
        }

        public void setBuildingName(String buildingName) {
            this.buildingName = buildingName;
        }

        public String getStreetName() {
            return streetName;
        }

        public void setStreetName(String streetName) {
            this.streetName = streetName;
        }

        public String getCityName() {
            return cityName;
        }

        public void setCityName(String cityName) {
            this.cityName = cityName;
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

        return new CustomerSummeryListRestResponse(service.getList(filter, sort, pagination), service.getCount(filter));
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
}
