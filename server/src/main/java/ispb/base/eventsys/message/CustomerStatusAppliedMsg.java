package ispb.base.eventsys.message;

import ispb.base.db.dataset.CustomerDataSet;
import ispb.base.eventsys.EventMessage;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


public class CustomerStatusAppliedMsg extends EventMessage implements Iterable<Long> {

    private final Set<Long> customerIdSet = new HashSet<>();

    public void addCustomerId(CustomerDataSet customer){
        customerIdSet.add(customer.getId());
    }

    public Set<Long> getCustomerIdSet(){
        return customerIdSet;
    }

    public Iterator<Long> iterator(){
        return customerIdSet.iterator();
    }
}
