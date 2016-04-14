package ispb.base.db.dao;


import ispb.base.db.dataset.TariffRadiusAttributeDataSet;
import ispb.base.db.filter.DataSetFilter;
import ispb.base.db.sort.DataSetSort;
import ispb.base.db.utils.Pagination;

import java.util.List;

public interface TariffRadiusAttributeDataSetDao {

    long save(TariffRadiusAttributeDataSet attribute);
    void delete(TariffRadiusAttributeDataSet attribute);

    List<TariffRadiusAttributeDataSet> getList(DataSetFilter filter, DataSetSort sort, Pagination pagination);
    TariffRadiusAttributeDataSet getById(long id);
}
