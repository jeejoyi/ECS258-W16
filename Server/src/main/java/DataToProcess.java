import java.util.Date;

/**
 * Created by fre on 2/23/16.
 */
public class DataToProcess {
    Date timestamp;
    String operation;
    Object data;
    Integer priority;
    String sensor;

    DataToProcess() {
        timestamp = new Date();
    }

    void setSensor(String sensor) {
        this.sensor = sensor;
    }

    long getMemorySize() {
        return ObjectSizeFetcher.getObjectSize(this.data);
    }
}