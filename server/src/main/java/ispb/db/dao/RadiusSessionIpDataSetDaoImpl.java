package ispb.db.dao;

import ispb.base.db.dao.RadiusSessionIpDataSetDao;
import ispb.base.db.dataset.RadiusSessionIpDataSet;
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


public class RadiusSessionIpDataSetDaoImpl extends BaseDao implements RadiusSessionIpDataSetDao {

    private final QueryBuilder queryBuilder;
    private final FieldSetDescriptor fieldsDescriptor;
    private final String hqlListTmpl;
    private final String hqlCountTmpl;
    private final String hqlDeleteOlder;

    public RadiusSessionIpDataSetDaoImpl(SessionFactory sessions, AppResources resources, QueryBuilder queryBuilder){
        super(sessions);

        this.queryBuilder = queryBuilder;
        fieldsDescriptor = loadFieldDescriptor(resources, "RadiusSessionIpDataSetDaoImpl/fieldSetDescriptor.json");
        hqlListTmpl = resources.getAsString(this.getClass(), "RadiusSessionIpDataSetDaoImpl/tmpl_list.hql");
        hqlCountTmpl = resources.getAsString(this.getClass(), "RadiusSessionIpDataSetDaoImpl/tmpl_count.hql");
        hqlDeleteOlder = resources.getAsString(this.getClass(), "RadiusSessionIpDataSetDaoImpl/delete_old.hql");
    }

    public long save(RadiusSessionIpDataSet address){
        return saveEntity(address);
    }

    public void delete(RadiusSessionIpDataSet address){
        markAsDeleted(address);
    }

    public List<RadiusSessionIpDataSet> getList(DataSetFilter filter, DataSetSort sort, Pagination pagination){
        return getListWithoutDeleted(RadiusSessionIpDataSet.class,
                filter, sort, pagination,
                queryBuilder, fieldsDescriptor, hqlListTmpl);
    }

    public long getCount(DataSetFilter filter){
        return getCountWithoutDeleted(filter, queryBuilder, fieldsDescriptor, hqlCountTmpl);
    }

    public RadiusSessionIpDataSet getById(long id){
        return getEntityById(RadiusSessionIpDataSet.class, id);
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
