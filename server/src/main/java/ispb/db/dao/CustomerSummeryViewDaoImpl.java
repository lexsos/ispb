package ispb.db.dao;


import ispb.base.db.dao.CustomerSummeryViewDao;
import ispb.base.db.view.CustomerSummeryView;
import ispb.db.util.BaseDao;
import org.hibernate.SessionFactory;

import java.util.List;


public class CustomerSummeryViewDaoImpl extends BaseDao implements CustomerSummeryViewDao {

    public CustomerSummeryViewDaoImpl(SessionFactory sessions){
        super(sessions);
    }

    public List<CustomerSummeryView> getList(){
        long size;
        String hql = "select customer, (select avg(id) from CustomerDataSet as c2 where c2.id = customer.id) from CustomerDataSet as customer";
        Object result = this.doTransaction(
                (session, transaction) -> {
                        return session.createQuery(hql).setFirstResult(0).setMaxResults(2).list();
                }
        );
        return (List<CustomerSummeryView>)result;
    }
}
