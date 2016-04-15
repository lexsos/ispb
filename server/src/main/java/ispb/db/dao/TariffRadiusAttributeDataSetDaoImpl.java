package ispb.db.dao;

import ispb.base.db.dao.TariffRadiusAttributeDataSetDao;
import ispb.base.db.dataset.TariffDataSet;
import ispb.base.db.dataset.TariffRadiusAttributeDataSet;
import ispb.base.db.field.FieldSetDescriptor;
import ispb.base.db.filter.DataSetFilter;
import ispb.base.db.sort.DataSetSort;
import ispb.base.db.utils.Pagination;
import ispb.base.db.utils.QueryBuilder;
import ispb.base.resources.AppResources;
import ispb.db.util.BaseDao;
import org.hibernate.SessionFactory;

import java.util.List;


public class TariffRadiusAttributeDataSetDaoImpl extends BaseDao implements TariffRadiusAttributeDataSetDao {

    private final QueryBuilder queryBuilder;
    private final FieldSetDescriptor fieldsDescriptor;
    private final String hqlListTmpl;
    private final String hqlCountTmpl;

    public TariffRadiusAttributeDataSetDaoImpl(SessionFactory sessions, AppResources resources, QueryBuilder queryBuilder){
        super(sessions);

        this.queryBuilder = queryBuilder;
        fieldsDescriptor = loadFieldDescriptor(resources, "TariffRadiusAttributeDataSetDaoImpl/fieldSetDescriptor.json");
        hqlListTmpl = resources.getAsString(this.getClass(), "TariffRadiusAttributeDataSetDaoImpl/tmpl_list.hql");
        hqlCountTmpl = resources.getAsString(this.getClass(), "TariffRadiusAttributeDataSetDaoImpl/tmpl_count.hql");

    }

    public long save(TariffRadiusAttributeDataSet attribute){
        return saveEntity(attribute);
    }

    public void delete(TariffRadiusAttributeDataSet attribute){
        markAsDeleted(attribute);
    }

    public TariffRadiusAttributeDataSet getById(long id){
        return getEntityById(TariffRadiusAttributeDataSet.class, id);
    }

    public long getCount(DataSetFilter filter){
        return getCountWithoutDeleted(filter, queryBuilder, fieldsDescriptor, hqlCountTmpl);
    }

    public List<TariffRadiusAttributeDataSet> getList(DataSetFilter filter, DataSetSort sort, Pagination pagination){
        return getListWithoutDeleted(TariffRadiusAttributeDataSet.class,
                filter, sort, pagination,
                queryBuilder, fieldsDescriptor, hqlListTmpl);
    }
}
