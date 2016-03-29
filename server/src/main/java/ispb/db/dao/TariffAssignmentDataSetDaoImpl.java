package ispb.db.dao;


import ispb.base.db.dao.TariffAssignmentDataSetDao;
import ispb.base.db.dataset.TariffAssignmentDataSet;
import ispb.base.db.field.FieldSetDescriptor;
import ispb.base.db.filter.DataSetFilter;
import ispb.base.db.sort.DataSetSort;
import ispb.base.db.utils.Pagination;
import ispb.base.db.utils.QueryBuilder;
import ispb.base.resources.AppResources;
import ispb.db.util.BaseDao;
import org.hibernate.SessionFactory;

import java.util.List;

public class TariffAssignmentDataSetDaoImpl extends BaseDao implements TariffAssignmentDataSetDao {

    private final QueryBuilder queryBuilder;
    private final FieldSetDescriptor fieldsDescriptor;
    private final String hqlListTmpl;
    private final String hqlCountTmpl;

    public TariffAssignmentDataSetDaoImpl(SessionFactory sessions, AppResources resources, QueryBuilder queryBuilder){
        super(sessions);

        this.queryBuilder = queryBuilder;
        fieldsDescriptor = loadFieldDescriptor(resources, "TariffAssignmentDataSetDaoImpl/fieldSetDescriptor.json");
        hqlListTmpl = resources.getAsString(this.getClass(), "TariffAssignmentDataSetDaoImpl/tmpl_list.hql");
        hqlCountTmpl = resources.getAsString(this.getClass(), "TariffAssignmentDataSetDaoImpl/tmpl_count.hql");
    }

    public long save(TariffAssignmentDataSet assignment){
        return saveEntity(assignment);
    }

    public void delete(TariffAssignmentDataSet assignment){
        markAsDeleted(assignment);
    }

    public List<TariffAssignmentDataSet> getList(DataSetFilter filter, DataSetSort sort, Pagination pagination){
        return getListWithoutDeleted(TariffAssignmentDataSet.class,
                filter, sort, pagination,
                queryBuilder, fieldsDescriptor, hqlListTmpl);
    }

    public long getCount(DataSetFilter filter){
        return getCountWithoutDeleted(filter, queryBuilder, fieldsDescriptor, hqlCountTmpl);
    }

    public TariffAssignmentDataSet getById(long id){
        return getEntityById(TariffAssignmentDataSet.class, id);
    }
}
