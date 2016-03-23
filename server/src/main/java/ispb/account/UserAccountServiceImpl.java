package ispb.account;

import ispb.base.db.dao.UserDataSetDao;
import ispb.base.db.dataset.UserDataSet;
import ispb.base.db.utils.DaoFactory;
import ispb.base.service.account.UserAccountService;
import ispb.base.frontend.utils.AccessLevel;

import org.apache.commons.codec.digest.DigestUtils;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Objects;

public class UserAccountServiceImpl implements UserAccountService {

    private final DaoFactory daoFactory;

    public UserAccountServiceImpl(DaoFactory daoFactory){
        this.daoFactory = daoFactory;
    }

    public UserDataSet auth(String login, String password){

        UserDataSetDao userDao = daoFactory.getUserDao();
        UserDataSet user = userDao.getByLogin(login.toLowerCase());
        if (user == null)
            return null;

        String shaPassword = encryptPassword(password, user.getSalt());
        if (Objects.equals(user.getPassword(), shaPassword))
            return user;
        return null;
    }

    public boolean loginExist(String login){
        UserDataSetDao userDao = daoFactory.getUserDao();
        return userDao.getByLogin(login.toLowerCase()) != null;
    }

    private String getSalt(){
        SecureRandom random = new SecureRandom();
        return new BigInteger(130, random).toString(32);
    }

    public UserDataSet addUser(String login, String password, String name, String surname, int accessLevel){

        UserDataSet user = new UserDataSet();
        user.setLogin(login.toLowerCase());
        user.setAccessLevel(accessLevel);
        user.setName(name);
        user.setSurname(surname);

        String salt = getSalt();
        user.setSalt(salt);
        user.setPassword(encryptPassword(password, salt));

        daoFactory.getUserDao().save(user);
        return user;
    }

    public void resetAdmin(String login, String password){
        UserDataSet user = getByLogin(login);
        if (user == null)
            addUser(login, password, login, login, AccessLevel.MAX);
        else {
            String shaPassword = encryptPassword(password, user.getSalt());
            user.setPassword(shaPassword);
            user.setActive(true);
            user.setAccessLevel(AccessLevel.MAX);
            daoFactory.getUserDao().save(user);
        }
    }

    private UserDataSet getByLogin(String login){
        UserDataSetDao userDao = daoFactory.getUserDao();
        return userDao.getByLogin(login.toLowerCase());
    }

    private String encryptPassword(String password, String salt){
        return DigestUtils.sha1Hex(salt + password);
    }
}
