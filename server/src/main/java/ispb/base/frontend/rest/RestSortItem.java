package ispb.base.frontend.rest;

import java.util.Objects;

public class RestSortItem {

    private String property;
    private String direction;

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public boolean isAsc() {
        return direction == null || Objects.equals(direction.toLowerCase(), "asc");
    }
}
