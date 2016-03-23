package ispb.db.dao;

import ispb.base.db.dao.AutoPaymentJournalDataSetDao;
import ispb.base.db.dataset.AutoPaymentJournalDataSet;
import ispb.base.resources.AppResources;
import ispb.db.util.BaseDao;
import org.hibernate.SessionFactory;

import java.util.List;

public class AutoPaymentJournalDataSetDaoImpl extends BaseDao implements AutoPaymentJournalDataSetDao {

    private final String getByPatternHql;

    public AutoPaymentJournalDataSetDaoImpl(SessionFactory sessions, AppResources resources){
        super(sessions);

        getByPatternHql = resources.getAsString(getClass(), "AutoPaymentJournalDataSetDaoImpl/getByPatternHql.hql");
    }

    public long save(AutoPaymentJournalDataSet autoPayment){
        return saveEntity(autoPayment);
    }

    public void delete(AutoPaymentJournalDataSet autoPayment){
        markAsDeleted(autoPayment);
    }

    public AutoPaymentJournalDataSet getByPattern(String pattern){
        Object result = this.doTransaction(
                (session, transaction) ->
                        session.createQuery(getByPatternHql).setParameter("pattern", pattern).list()
        );
        if (result instanceof List){
            List list = (List) result;
            if (!list.isEmpty() && list.get(0) instanceof AutoPaymentJournalDataSet)
                return (AutoPaymentJournalDataSet) list.get(0);
        }
        return null;
    }
}
