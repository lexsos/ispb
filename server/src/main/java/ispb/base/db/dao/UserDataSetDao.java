package ispb.base.db.dao;

import ispb.base.db.dataset.UserDataSet;
import java.util.List;

public interface UserDataSetDao {
    long save(UserDataSet user);
    void delete(UserDataSet user);
    List<UserDataSet> getAll();
    UserDataSet getByLogin(String login);
}
