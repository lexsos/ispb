package ispb.users;

import ispb.base.Application;
import ispb.base.db.dao.UserDataSetDao;
import ispb.base.db.dataset.UserDataSet;
import ispb.base.service.UserAccountService;
import org.apache.commons.codec.digest.DigestUtils;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Objects;

public class UserAccountServiceImpl implements UserAccountService {

    private Application application;

    public UserAccountServiceImpl(Application application){
        this.application = application;
    }

    public UserDataSet auth(String login, String password){

        UserDataSetDao userDao = application.getDaoFactory().getUserDao();
        UserDataSet user = userDao.getByLogin(login.toLowerCase());
        if (user == null)
            return null;

        String shaPasswd = DigestUtils.sha1Hex(user.getSalt() + password);
        if (Objects.equals(user.getPassword(), shaPasswd))
            return user;
        return null;
    }

    public boolean loginExist(String login){
        UserDataSetDao userDao = application.getDaoFactory().getUserDao();
        return userDao.getByLogin(login.toLowerCase()) != null;
    }

    private String getSalt(){
        SecureRandom random = new SecureRandom();
        return new BigInteger(130, random).toString(32);
    }

    public UserDataSet addUser(String login, String password, String name, String surname, int accessLevel){

        UserDataSet user = new UserDataSet();
        user.setLogin(login);
        user.setAccessLevel(accessLevel);
        user.setName(name);
        user.setSurname(surname);

        String salt = getSalt();
        user.setSalt(salt);
        user.setPassword(DigestUtils.sha1Hex(salt + password));

        application.getDaoFactory().getUserDao().save(user);
        return user;
    }
}
