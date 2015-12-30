package ispb.base.service;

import ispb.base.db.dataset.UserDataSet;

public interface UserAccountService {
    UserDataSet auth(String login, String password);
    boolean loginExist(String login);
    UserDataSet addUser(String login, String password, String name, String surname, int accessLevel);
}
