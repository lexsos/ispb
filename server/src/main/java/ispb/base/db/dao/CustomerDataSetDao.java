package ispb.base.db.dao;

import ispb.base.db.dataset.CustomerDataSet;


public interface CustomerDataSetDao {
    long save(CustomerDataSet customer);
    void delete(CustomerDataSet customer);
    CustomerDataSet getById(long id);
}
