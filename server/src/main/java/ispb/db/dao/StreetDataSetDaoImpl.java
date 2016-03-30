package ispb.db.dao;

import java.util.List;

import ispb.base.db.field.FieldSetDescriptor;
import ispb.base.db.filter.*;
import ispb.base.db.utils.QueryBuilder;
import ispb.base.resources.AppResources;
import org.hibernate.SessionFactory;
import ispb.base.db.dao.StreetDataSetDao;
import ispb.base.db.dataset.StreetDataSet;
import ispb.db.util.BaseDao;


public class StreetDataSetDaoImpl extends BaseDao implements StreetDataSetDao {

    private final QueryBuilder queryBuilder;
    private final FieldSetDescriptor fieldsDescriptor;
    private final String hqlListTmpl;

    public StreetDataSetDaoImpl(SessionFactory sessions, AppResources resources, QueryBuilder queryBuilder){
        super(sessions);
        this.queryBuilder = queryBuilder;

        hqlListTmpl = resources.getAsString(this.getClass(), "StreetDataSetDaoImpl/hql_list.tmpl");
        fieldsDescriptor = loadFieldDescriptor(resources, "StreetDataSetDaoImpl/fieldSetDescriptor.json");
    }

    public long save(StreetDataSet street){
        return this.saveEntity(street);
    }

    public void delete(StreetDataSet street){
        this.markAsDeleted(street);
    }

    public List<StreetDataSet> getList(DataSetFilter filter){
        return getListWithoutDeleted(StreetDataSet.class,
                filter, null, null,
                queryBuilder, fieldsDescriptor, hqlListTmpl);
    }

    public StreetDataSet getById(long id){
        return this.getEntityById(StreetDataSet.class, id);
    }
}
