package ispb.base.radius.servlet.descriptor;


import java.util.Map;

public class ServletDescriptor {
    private Map<String, ParameterDescriptor> parameters;


    public Map<String, ParameterDescriptor> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, ParameterDescriptor> parameters) {
        this.parameters = parameters;
    }

    public ParameterDescriptor getParameter(String parameterName){
        if (parameters != null)
            return parameters.get(parameterName);
        return null;
    }
}
