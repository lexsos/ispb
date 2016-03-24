package ispb.base.service.account;

import ispb.base.db.container.UserContainer;
import ispb.base.db.dataset.UserDataSet;
import ispb.base.service.exception.AlreadyExistException;
import ispb.base.service.exception.NotFoundException;

import java.util.List;

public interface UserAccountService {
    UserDataSet auth(String login, String password);
    boolean loginExist(String login);
    List<UserDataSet> getList();
    void resetAdmin(String login, String password);
    UserDataSet create(UserContainer container)  throws AlreadyExistException;
    UserDataSet update(UserContainer container) throws AlreadyExistException, NotFoundException;
    void delete(long userId) throws NotFoundException;
    boolean changePassword(UserContainer container) throws NotFoundException;
}
