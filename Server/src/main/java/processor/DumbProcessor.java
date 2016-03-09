package processor;

import data_type.DataToProcess;

public class DumbProcessor implements IProcessor {

    private static DumbProcessor INSTANCE;

    /**
     * Get instance of Singleton
     *
     * @return instance
     */
    public static DumbProcessor getInstance() {
        if (INSTANCE != null) return INSTANCE;
        return INSTANCE = new DumbProcessor();
    }

    /**
     * Avoid new
     */
    private DumbProcessor() {

    }

    @Override
    public void process(DataToProcess packet) {
        try {
            // this is a random function for the execution of the queue
            Thread.sleep(packet.getMemorySize() / 500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
