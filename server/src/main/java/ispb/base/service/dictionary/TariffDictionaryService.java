package ispb.base.service.dictionary;


import ispb.base.db.container.TariffContainer;
import ispb.base.db.container.TariffRadiusAttributeContainer;
import ispb.base.db.dataset.TariffDataSet;
import ispb.base.db.dataset.TariffRadiusAttributeDataSet;
import ispb.base.db.fieldtype.RadiusAttributeCondition;
import ispb.base.db.filter.DataSetFilter;
import ispb.base.db.sort.DataSetSort;
import ispb.base.db.utils.Pagination;
import ispb.base.service.exception.AlreadyExistException;
import ispb.base.service.exception.NotFoundException;

import java.util.List;

public interface TariffDictionaryService {
    List<TariffDataSet> getList(DataSetFilter filter, DataSetSort sort);
    List<TariffRadiusAttributeDataSet> getAttributeList(DataSetFilter filter, DataSetSort sort, Pagination pagination);
    List<TariffRadiusAttributeDataSet> getAttributeList(long tariffId, RadiusAttributeCondition condition);

    TariffDataSet create(TariffContainer container) throws AlreadyExistException;
    TariffDataSet update(long tariffId, TariffContainer container) throws AlreadyExistException, NotFoundException;

    TariffRadiusAttributeDataSet createAttribute(TariffRadiusAttributeContainer container) throws NotFoundException;
    TariffRadiusAttributeDataSet updateAttribute(long attributeId, TariffRadiusAttributeContainer container) throws NotFoundException;

    void delete(long tariffId) throws NotFoundException;
    boolean exist(String tariffName);

    void deleteAttribute(long attributeId) throws NotFoundException;
}
