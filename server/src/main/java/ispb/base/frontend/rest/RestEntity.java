package ispb.base.frontend.rest;

import ispb.base.frontend.utils.Verifiable;

public abstract class RestEntity implements Verifiable {

    private long id = -1;

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
}
