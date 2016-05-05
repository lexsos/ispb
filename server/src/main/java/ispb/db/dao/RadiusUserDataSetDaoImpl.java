package ispb.db.dao;


import ispb.base.db.dao.RadiusUserDataSetDao;
import ispb.base.db.dataset.RadiusUserDataSet;
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

public class RadiusUserDataSetDaoImpl extends BaseDao implements RadiusUserDataSetDao {

    private final QueryBuilder queryBuilder;
    private final String hqlListTmpl;
    private final String hqlCountTmpl;
    private final String hqlClearAuthRequest;
    private final FieldSetDescriptor fieldsDescriptor;

    public RadiusUserDataSetDaoImpl(SessionFactory sessions, AppResources resources, QueryBuilder queryBuilder){
        super(sessions);
        this.queryBuilder = queryBuilder;

        hqlListTmpl = resources.getAsString(this.getClass(), "RadiusUserDataSetDaoImpl/tmpl_list.hql");
        hqlCountTmpl = resources.getAsString(this.getClass(), "RadiusUserDataSetDaoImpl/tmpl_count.hql");
        hqlClearAuthRequest = resources.getAsString(this.getClass(), "RadiusUserDataSetDaoImpl/clear_auth_request.hql");
        fieldsDescriptor = loadFieldDescriptor(resources, "RadiusUserDataSetDaoImpl/fieldSetDescriptor.json");
    }

    public long save(RadiusUserDataSet radiusUser){
        return saveEntity(radiusUser);
    }

    public void delete(RadiusUserDataSet radiusUser){
        markAsDeleted(radiusUser);
    }

    public List<RadiusUserDataSet> getList(DataSetFilter filter, DataSetSort sort, Pagination pagination){
        return getListWithoutDeleted(RadiusUserDataSet.class,
                filter, sort, pagination,
                queryBuilder, fieldsDescriptor, hqlListTmpl);
    }

    public long getCount(DataSetFilter filter){
        return getCountWithoutDeleted(filter, queryBuilder, fieldsDescriptor, hqlCountTmpl);
    }

    public RadiusUserDataSet getById(long id){
        return getEntityById(RadiusUserDataSet.class, id);
    }

    public void clearAuthRequest(){
        this.doTransaction(
                (session, transaction) ->
                        session.createQuery(hqlClearAuthRequest).setParameter("deleteAt", new Date()).executeUpdate()
        );
    }
}
