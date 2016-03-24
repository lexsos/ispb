package ispb.frontend.rest.resource;

import ispb.base.db.container.TariffContainer;
import ispb.base.db.dataset.TariffDataSet;
import ispb.base.db.filter.DataSetFilter;
import ispb.base.db.sort.DataSetSort;
import ispb.base.frontend.rest.*;
import ispb.base.frontend.utils.AccessLevel;
import ispb.base.service.dictionary.TariffDictionaryService;
import ispb.base.service.exception.AlreadyExistException;
import ispb.base.service.exception.NotFoundException;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;


public class TariffResource extends RestResource {

    private static class TariffEntity extends RestEntity implements TariffContainer {

        private String name = null;
        private double dailyPayment = 0;
        private boolean autoDailyPayment = false;
        private double offThreshold = 0;
        private double upRate = 0;
        private double downRate = 0;

        public TariffEntity(){}

        public TariffEntity(TariffDataSet tariff){
            setId(tariff.getId());
            setName(tariff.getName());
            setDailyPayment(tariff.getDailyPayment());
            setAutoDailyPayment(tariff.isAutoDailyPayment());
            setOffThreshold(tariff.getOffThreshold());
            setUpRate(tariff.getUpRate());
            setDownRate(tariff.getDownRate());
        }

        public boolean verify(){
            return getName() != null;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public double getDailyPayment() {
            return dailyPayment;
        }

        public void setDailyPayment(double dailyPayment) {
            this.dailyPayment = dailyPayment;
        }

        public boolean isAutoDailyPayment() {
            return autoDailyPayment;
        }

        public void setAutoDailyPayment(boolean autoDailyPayment) {
            this.autoDailyPayment = autoDailyPayment;
        }

        public double getOffThreshold() {
            return offThreshold;
        }

        public void setOffThreshold(double offThreshold) {
            this.offThreshold = offThreshold;
        }

        public double getUpRate() {
            return upRate;
        }

        public void setUpRate(double upRate) {
            this.upRate = upRate;
        }

        public double getDownRate() {
            return downRate;
        }

        public void setDownRate(double downRate) {
            this.downRate = downRate;
        }

        public Date getDeleteAt(){
            return null;
        }
    }

    private static class TariffListRestResponse extends RestResponse {

        private List<TariffEntity> tariffList = null;

        public TariffListRestResponse(List<TariffDataSet> dataSetList){
            tariffList = new LinkedList<>();
            for (TariffDataSet tariff : dataSetList)
                tariffList.add(new TariffEntity(tariff));
        }

        public TariffListRestResponse(TariffDataSet tariff){
            tariffList = new LinkedList<>();
            tariffList.add(new TariffEntity(tariff));
        }
    }

    public int getReadAccessLevel(){
        return AccessLevel.MIN;
    }

    public int getWriteAccessLevel(){
        return AccessLevel.MANAGER;
    }

    public Class<? extends RestEntity> getEntityType(){
        return TariffEntity.class;
    }

    public RestResponse getEntityList(RestContext restContext){
        TariffDictionaryService service = getTariffDicService(restContext);
        DataSetFilter filter = restContext.getDataSetFilter();
        DataSetSort sort = restContext.getDataSetSort();
        List<TariffDataSet> tariffList = service.getList(filter, sort);
        return new TariffListRestResponse(tariffList);
    }

    public RestResponse createEntity(RestContext restContext){
        TariffDictionaryService service = getTariffDicService(restContext);
        TariffEntity entity = restContext.getEntityByType(TariffEntity.class);
        try {
            TariffDataSet tariff = service.create(entity);
            return new TariffListRestResponse(tariff);
        }
        catch (AlreadyExistException e){
            return ErrorRestResponse.alreadyExist();
        }
    }

    public RestResponse deleteEntity(RestContext restContext){
        TariffDictionaryService service = getTariffDicService(restContext);
        try {
            service.delete(restContext.getId());
            return new RestResponse();
        }
        catch (NotFoundException e){
            return ErrorRestResponse.notFound();
        }
    }

    public RestResponse updateEntity(RestContext restContext){
        TariffDictionaryService service = getTariffDicService(restContext);
        TariffEntity entity = restContext.getEntityByType(TariffEntity.class);
        try {
            TariffDataSet tariff = service.update(restContext.getId(), entity);
            return new TariffListRestResponse(tariff);
        }
        catch (AlreadyExistException e){
            return ErrorRestResponse.alreadyExist();
        }
        catch (NotFoundException e){
            return ErrorRestResponse.notFound();
        }
    }

    private TariffDictionaryService getTariffDicService(RestContext restContext){
        return restContext.getApplication().getByType(TariffDictionaryService.class);
    }
}
