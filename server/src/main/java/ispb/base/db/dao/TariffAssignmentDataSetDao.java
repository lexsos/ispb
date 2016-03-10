package ispb.base.db.dao;


import ispb.base.db.dataset.TariffAssignmentDataSet;
import ispb.base.db.filter.DataSetFilter;
import ispb.base.db.sort.DataSetSort;
import ispb.base.db.utils.Pagination;

import java.util.List;

public interface TariffAssignmentDataSetDao {
    long save(TariffAssignmentDataSet assignment);
    void delete(TariffAssignmentDataSet assignment);
    List<TariffAssignmentDataSet> getList(DataSetFilter filter, DataSetSort sort, Pagination pagination);
    long getCount(DataSetFilter filter);
    TariffAssignmentDataSet getById(long id);
}
