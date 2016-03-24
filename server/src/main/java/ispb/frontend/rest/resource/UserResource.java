package ispb.frontend.rest.resource;


import ispb.base.Application;
import ispb.base.db.container.UserContainer;
import ispb.base.db.dataset.UserDataSet;
import ispb.base.frontend.rest.*;
import ispb.base.frontend.utils.AccessLevel;
import ispb.base.service.account.UserAccountService;
import ispb.base.service.exception.AlreadyExistException;
import ispb.base.service.exception.NotFoundException;

import java.util.LinkedList;
import java.util.List;

public class UserResource extends RestResource {

    private static class UserEntity extends RestEntity implements UserContainer {

        private String login;
        private String name;
        private String surname;
        private int accessLevel;
        private boolean active;
        private String password;

        public UserEntity(){

        }

        public UserEntity(UserDataSet user){
            setId(user.getId());
            login = user.getLogin();
            name = user.getName();
            surname = user.getSurname();
            accessLevel = user.getAccessLevel();
            active = user.isActive();
        }

        public boolean verify(){
            return getLogin() != null && getName() != null && getSurname() != null;
        }

        @Override
        public String getLogin() {
            return login;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public String getSurname() {
            return surname;
        }

        @Override
        public int getAccessLevel() {
            return accessLevel;
        }

        @Override
        public boolean isActive() {
            return active;
        }

        @Override
        public String getPassword() {
            return password;
        }
    }

    private static class UserListRestResponse extends RestResponse {

        private final List<UserEntity> userList = new LinkedList<>();

        public UserListRestResponse(List<UserDataSet> userDataSetList){
            for (UserDataSet user: userDataSetList)
                userList.add(new UserEntity(user));
        }

        public UserListRestResponse(UserDataSet userDataSet){
                userList.add(new UserEntity(userDataSet));
        }
    }

    public int getReadAccessLevel(){
        return AccessLevel.ADMIN;
    }

    public int getWriteAccessLevel(){
        return AccessLevel.ADMIN;
    }

    public Class<? extends RestEntity> getEntityType(){
        return UserEntity.class;
    }

    public RestResponse getEntityList(RestContext restContext){
        UserAccountService service = getUserService(restContext.getApplication());
        return new UserListRestResponse(service.getList());
    }

    public RestResponse createEntity(RestContext restContext){
        UserAccountService service = getUserService(restContext.getApplication());
        UserEntity entity = restContext.getEntityByType(UserEntity.class);
        try {
            UserDataSet user = service.create(entity);
            return new UserListRestResponse(user);
        }
        catch (AlreadyExistException e){
            return ErrorRestResponse.alreadyExist();
        }
    }

    public RestResponse deleteEntity(RestContext restContext){
        UserAccountService service = getUserService(restContext.getApplication());
        try {
            service.delete(restContext.getId());
            return new RestResponse();
        }
        catch (NotFoundException e){
            return ErrorRestResponse.notFound();
        }
    }

    public RestResponse updateEntity(RestContext restContext){
        UserAccountService service = getUserService(restContext.getApplication());
        UserEntity entity = restContext.getEntityByType(UserEntity.class);
        try {
            UserDataSet user = service.update(entity);
            String newPassword = entity.getPassword();
            if (newPassword != null && newPassword.length() > 0)
                service.changePassword(entity);
            return new UserListRestResponse(user);
        }
        catch (AlreadyExistException e){
            return ErrorRestResponse.alreadyExist();
        }
        catch (NotFoundException e){
            return ErrorRestResponse.notFound();
        }
    }

    private UserAccountService getUserService(Application application){
        return application.getByType(UserAccountService.class);
    }
}
