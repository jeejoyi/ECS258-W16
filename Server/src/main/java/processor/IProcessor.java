package processor;

import data_type.DataToProcess;

/**
 * Created by fre on 3/8/16.
 */
public interface IProcessor {
    /**
     * Function that must be implemented to handle the Data with a Custom Processor
     *
     * @param packet
     */
    void process(DataToProcess packet);
}
