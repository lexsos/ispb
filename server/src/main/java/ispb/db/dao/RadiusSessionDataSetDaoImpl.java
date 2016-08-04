package ispb.db.dao;

import ispb.base.db.dao.RadiusSessionDataSetDao;
import ispb.base.db.dataset.RadiusSessionDataSet;
import ispb.base.db.field.FieldSetDescriptor;
import ispb.base.db.filter.DataSetFilter;
import ispb.base.db.sort.DataSetSort;
import ispb.base.db.utils.Pagination;
import ispb.base.db.utils.QueryBuilder;
import ispb.base.resources.AppResources;
import ispb.db.util.BaseDao;
import org.hibernate.SessionFactory;

import java.util.Date;
import java.util.List;


public class RadiusSessionDataSetDaoImpl extends BaseDao implements RadiusSessionDataSetDao {

    private final QueryBuilder queryBuilder;
    private final FieldSetDescriptor fieldsDescriptor;
    private final String hqlListTmpl;
    private final String hqlCountTmpl;
    private final String hqlDeleteOlder;

    public RadiusSessionDataSetDaoImpl(SessionFactory sessions, AppResources resources, QueryBuilder queryBuilder){
        super(sessions);

        this.queryBuilder = queryBuilder;
        fieldsDescriptor = loadFieldDescriptor(resources, "RadiusSessionDataSetDaoImpl/fieldSetDescriptor.json");
        hqlListTmpl = resources.getAsString(this.getClass(), "RadiusSessionDataSetDaoImpl/tmpl_list.hql");
        hqlCountTmpl = resources.getAsString(this.getClass(), "RadiusSessionDataSetDaoImpl/tmpl_count.hql");
        hqlDeleteOlder = resources.getAsString(this.getClass(), "RadiusSessionDataSetDaoImpl/delete_old.hql");
    }

    public long save(RadiusSessionDataSet session){
        return saveEntity(session);
    }

    public void delete(RadiusSessionDataSet session){
        markAsDeleted(session);
    }

    public List<RadiusSessionDataSet> getList(DataSetFilter filter, DataSetSort sort, Pagination pagination){
        return getListWithoutDeleted(RadiusSessionDataSet.class,
                filter, sort, pagination,
                queryBuilder, fieldsDescriptor, hqlListTmpl);
    }

    public long getCount(DataSetFilter filter){
        return getCountWithoutDeleted(filter, queryBuilder, fieldsDescriptor, hqlCountTmpl);
    }

    public RadiusSessionDataSet getById(long id){
        return getEntityById(RadiusSessionDataSet.class, id);
    }

    public void eraseOld(Date olderThen){
        this.doTransaction(
                (session, transaction) -> {
                    session.createQuery(hqlDeleteOlder).setParameter("olderThen", olderThen).executeUpdate();
                    return null;
                }
        );
    }
}
