package ispb.db.dao;

import ispb.base.db.dao.UserDataSetDao;
import ispb.base.db.dataset.UserDataSet;
import ispb.base.resources.AppResources;
import ispb.db.util.BaseDao;
import org.hibernate.SessionFactory;

import java.util.List;

public class UserDataSetDaoImpl extends BaseDao implements UserDataSetDao {

    private AppResources resources;

    public UserDataSetDaoImpl(SessionFactory sessions, AppResources resources){
        super(sessions);
        this.resources = resources;
    }

    public long save(UserDataSet user){
        return saveEntity(user);
    }

    public void delete(UserDataSet user){
        markAsDeleted(user);
    }

    public UserDataSet getById(long userId){
        return getEntityById(UserDataSet.class, userId);
    }

    public List<UserDataSet> getAll(){
        String hql = resources.getAsString(this.getClass(), "UserDataSetDaoImpl/getAll.hql");
        Object result = this.doTransaction(
                (session, transaction) ->
                        session.createQuery(hql).list()
        );
        return (List<UserDataSet>)result;
    }

    public UserDataSet getByLogin(String login){
        String hql = resources.getAsString(this.getClass(), "UserDataSetDaoImpl/getByLogin.hql");
        Object result = this.doTransaction(
                (session, transaction) ->
                        session.createQuery(hql).setParameter("login", login).list()
        );
        List<UserDataSet> list = (List<UserDataSet>)result;
        if (list.isEmpty())
            return null;
        return list.get(0);
    }
}
