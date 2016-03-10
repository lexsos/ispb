package ispb.db.dao;


import ispb.base.db.dao.TariffAssignmentDataSetDao;
import ispb.base.db.dataset.TariffAssignmentDataSet;
import ispb.base.db.field.CmpOperator;
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

    private QueryBuilder queryBuilder;
    private FieldSetDescriptor fieldsDescriptor;
    private String hqlListTmpl;
    private String hqlCountTmpl;

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
        DataSetFilter newFilter;
        if (filter == null)
            newFilter = new  DataSetFilter();
        else
            newFilter = filter.getCopy();
        newFilter.add("deleteAt", CmpOperator.IS_NULL, null);

        return getQueryAsList(hqlListTmpl, queryBuilder, fieldsDescriptor, newFilter, sort, pagination);
    }

    public long getCount(DataSetFilter filter){
        DataSetFilter newFilter;
        if (filter == null)
            newFilter = new  DataSetFilter();
        else
            newFilter = filter.getCopy();
        newFilter.add("deleteAt", CmpOperator.IS_NULL, null);

        return getRowCount(hqlCountTmpl, queryBuilder, fieldsDescriptor, newFilter);
    }

    public TariffAssignmentDataSet getById(long id){
        return getEntityById(TariffAssignmentDataSet.class, id);
    }
}
