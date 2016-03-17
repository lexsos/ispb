package ispb.base.eventsys;


import java.util.Date;

public class EventMessage {

    private Date createAt;

    public EventMessage(){
        setCreateAt(new Date());
    }

    public Date getCreateAt() {
        return createAt;
    }

    protected void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }
}
