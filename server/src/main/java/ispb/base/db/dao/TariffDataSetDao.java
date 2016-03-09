package ispb.base.db.dao;


import ispb.base.db.dataset.TariffDataSet;
import ispb.base.db.filter.DataSetFilter;
import ispb.base.db.sort.DataSetSort;
import ispb.base.db.utils.Pagination;

import java.util.List;

public interface TariffDataSetDao {
    long save(TariffDataSet tariff);
    void delete(TariffDataSet tariff);
    List<TariffDataSet> getList(DataSetFilter filter, DataSetSort sort, Pagination pagination);
    TariffDataSet getById(long id);
}
