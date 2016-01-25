package ispb.base.db.filter;


import java.util.LinkedHashMap;
import java.util.Map;

public class WhereStatement {

    private String where = "";
    private Map<String, Object> parameters = new LinkedHashMap<>();

    public String getWhere() {
        return where;
    }

    public void setWhere(String where) {
        this.where = where;
    }

    public Map<String, Object> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, Object> parameters) {
        this.parameters = parameters;
    }
}
