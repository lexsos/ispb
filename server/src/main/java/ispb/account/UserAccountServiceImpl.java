package ispb.account;

import ispb.base.db.container.UserContainer;
import ispb.base.db.dao.UserDataSetDao;
import ispb.base.db.dataset.UserDataSet;
import ispb.base.db.utils.DaoFactory;
import ispb.base.service.LogService;
import ispb.base.service.account.UserAccountService;
import ispb.base.frontend.utils.AccessLevel;

import ispb.base.service.exception.AlreadyExistException;
import ispb.base.service.exception.NotFoundException;
import ispb.base.service.exception.ServiceException;
import org.apache.commons.codec.digest.DigestUtils;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.List;
import java.util.Objects;

public class UserAccountServiceImpl implements UserAccountService {

    private final DaoFactory daoFactory;
    private final LogService logService;

    public UserAccountServiceImpl(DaoFactory daoFactory, LogService logService){
        this.daoFactory = daoFactory;
        this.logService = logService;
    }

    public UserDataSet auth(String login, String password){

        UserDataSetDao userDao = daoFactory.getUserDao();
        UserDataSet user = userDao.getByLogin(login.toLowerCase());
        if (user == null || !user.isActive())
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

    public List<UserDataSet> getList(){
        UserDataSetDao userDao = daoFactory.getUserDao();
        return userDao.getAll();
    }

    public UserDataSet create(UserContainer container)  throws AlreadyExistException{

        if (loginExist(container.getLogin()))
            throw new  AlreadyExistException();

        UserDataSet user = new UserDataSet();
        user.setLogin(container.getLogin().toLowerCase());
        user.setAccessLevel(container.getAccessLevel());
        user.setName(container.getName());
        user.setSurname(container.getSurname());
        user.setActive(container.isActive());

        String salt = getSalt();
        user.setSalt(salt);
        user.setPassword(encryptPassword(container.getPassword(), salt));

        daoFactory.getUserDao().save(user);
        return user;
    }

    public UserDataSet update(UserContainer container) throws AlreadyExistException, NotFoundException{
        UserDataSetDao dao = daoFactory.getUserDao();

        UserDataSet user = dao.getById(container.getId());
        if (user == null)
            throw new NotFoundException();

        UserDataSet otherUser = getByLogin(container.getLogin());
        if (otherUser != null && otherUser.getId() != user.getId())
            throw new AlreadyExistException();

        user.setLogin(container.getLogin().toLowerCase());
        user.setAccessLevel(container.getAccessLevel());
        user.setName(container.getName());
        user.setSurname(container.getSurname());
        user.setActive(container.isActive());

        dao.save(user);
        return user;
    }

    public void delete(long userId) throws NotFoundException{
        UserDataSetDao dao = daoFactory.getUserDao();

        UserDataSet user = dao.getById(userId);
        if (user == null)
            throw new NotFoundException();

        dao.delete(user);
    }

    public boolean changePassword(UserContainer container) throws NotFoundException{
        UserDataSetDao dao = daoFactory.getUserDao();

        UserDataSet user = dao.getById(container.getId());
        if (user == null)
            throw new NotFoundException();

        String newPassword = container.getPassword();
        if (newPassword == null || newPassword.length() < 1)
            return false;

        String salt = getSalt();
        user.setSalt(salt);
        user.setPassword(encryptPassword(newPassword, salt));
        dao.save(user);
        return true;
    }

    public void resetAdmin(String login, String password){
        UserDataSet user = getByLogin(login);

        try {
            if (user == null){
                user = new UserDataSet();
                user.setName(login);
                user.setSurname(login);
                user.setLogin(login.toLowerCase());
                user.setActive(true);
                user.setPassword(password);
                user.setAccessLevel(AccessLevel.MAX);
                create(user);
            }
            else {
                user.setAccessLevel(AccessLevel.MAX);
                user = update(user);
                user.setPassword(password);
                changePassword(user);
            }
        }
        catch (ServiceException e){
            logService.warn("Error while reset admin password", e);
        }
    }

    private UserDataSet getByLogin(String login){
        UserDataSetDao userDao = daoFactory.getUserDao();
        return userDao.getByLogin(login.toLowerCase());
    }

    private String encryptPassword(String password, String salt){
        return DigestUtils.sha1Hex(salt + password);
    }

    private String getSalt(){
        SecureRandom random = new SecureRandom();
        return new BigInteger(130, random).toString(32);
    }
}
