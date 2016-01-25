package ispb.base.db.view;


import ispb.base.db.dataset.CustomerDataSet;

public class CustomerSummeryView {

    private CustomerDataSet customer;
    private double balance;

    public CustomerSummeryView(CustomerDataSet customer, double balance){
        this.setCustomer(customer);
        this.setBalance(balance);
    }

    public CustomerDataSet getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerDataSet customer) {
        this.customer = customer;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}
