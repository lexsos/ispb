package ispb.base.db.container;


public interface UserContainer {
    long getId();
    String getName();
    String getSurname();
    String getLogin();
    String getPassword();
    int getAccessLevel();
    boolean isActive();
}
