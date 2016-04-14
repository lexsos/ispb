package ispb.base.service.dictionary;


import ispb.base.db.container.TariffRadiusAttributeContainer;
import ispb.base.db.dataset.TariffRadiusAttributeDataSet;
import ispb.base.db.filter.DataSetFilter;
import ispb.base.db.sort.DataSetSort;
import ispb.base.db.utils.Pagination;
import ispb.base.service.exception.NotFoundException;

import java.util.List;

public interface TariffRadiusAttributeService {
    List<TariffRadiusAttributeDataSet> getList(DataSetFilter filter, DataSetSort sort, Pagination pagination);

    TariffRadiusAttributeDataSet create(TariffRadiusAttributeContainer container);
    TariffRadiusAttributeDataSet update(long attributeId, TariffRadiusAttributeContainer container) throws NotFoundException;

    void delete(long attributeId) throws NotFoundException;
}
