package ispb.base.frontend.rest;


import java.util.Objects;

public class RestFilterItem {

    private String property;
    private String value;

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean propertyEquals(String property){
        return Objects.equals(this.property, property);
    }

    public int asInt(){
        return Integer.parseInt(value);
    }

    public long asLong(){
        return Long.parseLong(value);
    }

    public float asFloat(){
        return Float.parseFloat(value);
    }

    public double asDouble(){
        return Double.parseDouble(value);
    }
}
