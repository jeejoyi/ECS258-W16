package dataType;

import utility.ObjectSizeFetcher;

import java.util.Date;


public class DataToProcess {
    public Date timestamp;
    public String operation;
    public Object data;
    public Integer priority;
    public String sensor;

    DataToProcess() {
        timestamp = new Date();
    }

    public void setSensor(String sensor) {
        this.sensor = sensor;
    }

    public long getMemorySize() {
        return ObjectSizeFetcher.getObjectSize(this.data);
    }
}