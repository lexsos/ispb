package ispb.db.dao;


import ispb.base.db.dao.RadiusSessionAttributeDataSetDao;
import ispb.base.db.dataset.RadiusSessionAttributeDataSet;
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

public class RadiusSessionAttributeDataSetDaoImpl extends BaseDao implements RadiusSessionAttributeDataSetDao {

    private final QueryBuilder queryBuilder;
    private final FieldSetDescriptor fieldsDescriptor;
    private final String hqlListTmpl;
    private final String hqlCountTmpl;
    private final String hqlDeleteOlder;

    public RadiusSessionAttributeDataSetDaoImpl(SessionFactory sessions, AppResources resources, QueryBuilder queryBuilder){
        super(sessions);

        this.queryBuilder = queryBuilder;
        fieldsDescriptor = loadFieldDescriptor(resources, "RadiusSessionAttributeDataSetDaoImpl/fieldSetDescriptor.json");
        hqlListTmpl = resources.getAsString(this.getClass(), "RadiusSessionAttributeDataSetDaoImpl/tmpl_list.hql");
        hqlCountTmpl = resources.getAsString(this.getClass(), "RadiusSessionAttributeDataSetDaoImpl/tmpl_count.hql");
        hqlDeleteOlder = resources.getAsString(this.getClass(), "RadiusSessionAttributeDataSetDaoImpl/delete_old.hql");
    }

    public long save(RadiusSessionAttributeDataSet attribute){
        return saveEntity(attribute);
    }

    public void delete(RadiusSessionAttributeDataSet attribute){
            markAsDeleted(attribute);
    }

    public List<RadiusSessionAttributeDataSet> getList(DataSetFilter filter, DataSetSort sort, Pagination pagination){
        return getListWithoutDeleted(RadiusSessionAttributeDataSet.class,
                filter, sort, pagination,
                queryBuilder, fieldsDescriptor, hqlListTmpl);
    }

    public long getCount(DataSetFilter filter){
        return getCountWithoutDeleted(filter, queryBuilder, fieldsDescriptor, hqlCountTmpl);
    }

    public RadiusSessionAttributeDataSet getById(long id){
        return getEntityById(RadiusSessionAttributeDataSet.class, id);
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
