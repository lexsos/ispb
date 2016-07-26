package ispb.base.radius.servlet;


import ispb.base.db.fieldtype.RadiusClientType;

public interface RadiusClientRepositoryBuilder {

    RadiusClientRepository buildRepository();
    void addServletType(RadiusClientType type, Class<? extends RadiusServlet> clazz);
}
