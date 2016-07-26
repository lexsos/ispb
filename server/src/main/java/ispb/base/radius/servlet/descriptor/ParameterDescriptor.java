package ispb.base.radius.servlet.descriptor;


import java.util.List;

public class ParameterDescriptor {

    private String display;
    private String defaultValue;
    private List<ValueDescriptor> values;

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public List<ValueDescriptor> getValues() {
        return values;
    }

    public void setValues(List<ValueDescriptor> values) {
        this.values = values;
    }
}
