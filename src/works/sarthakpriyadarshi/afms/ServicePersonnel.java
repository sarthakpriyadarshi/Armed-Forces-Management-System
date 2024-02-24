package works.sarthakpriyadarshi.afms;

import java.io.Serializable;

public class ServicePersonnel implements Serializable {
    private String name;
    private String serviceNumber;
    private String service;
    private String trade;

    public ServicePersonnel(String name, String serviceNumber, String service, String trade) {
        this.name = name;
        this.serviceNumber = serviceNumber;
        this.service = service;
        this.trade = trade;
    }

    public String getName() {
        return name;
    }

    public String getServiceNumber() {
        return serviceNumber;
    }

    public String getService() {
        return service;
    }

    public String getTrade() {
        return trade;
    }

    @Override
    public String toString() {
        // Format the output with fixed-width columns
        return String.format("%-20s %-20s %-20s %-20s",
                "Name: " + name + " ",
                "Service Number: " + serviceNumber + "\t",
                "Service: " + service +  "\t",
                "Trade: " + trade + "\t");
    }
}