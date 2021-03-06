package ispb.db.dao;

import ispb.base.db.dao.TariffDataSetDao;
import ispb.base.db.dataset.TariffDataSet;
import ispb.base.db.field.FieldSetDescriptor;
import ispb.base.db.filter.DataSetFilter;
import ispb.base.db.sort.DataSetSort;
import ispb.base.db.utils.Pagination;
import ispb.base.db.utils.QueryBuilder;
import ispb.base.resources.AppResources;
import ispb.db.util.BaseDao;
import org.hibernate.SessionFactory;

import java.util.List;

public class TariffDataSetDaoImpl extends BaseDao implements TariffDataSetDao {

    private final QueryBuilder queryBuilder;
    private final FieldSetDescriptor fieldsDescriptor;
    private final String hqlListTmpl;

    public TariffDataSetDaoImpl(SessionFactory sessions, AppResources resources, QueryBuilder queryBuilder){
        super(sessions);

        this.queryBuilder = queryBuilder;
        fieldsDescriptor = loadFieldDescriptor(resources, "TariffDataSetDaoImpl/fieldSetDescriptor.json");
        hqlListTmpl = resources.getAsString(this.getClass(), "TariffDataSetDaoImpl/tmpl_list.hql");
    }

    public long save(TariffDataSet tariff){
        return saveEntity(tariff);
    }

    public void delete(TariffDataSet tariff){
        markAsDeleted(tariff);
    }

    public List<TariffDataSet> getList(DataSetFilter filter, DataSetSort sort, Pagination pagination){
        return getListWithoutDeleted(TariffDataSet.class,
                filter, sort, pagination,
                queryBuilder, fieldsDescriptor, hqlListTmpl);
    }

    public TariffDataSet getById(long id){
        return getEntityById(TariffDataSet.class, id);
    }
}
