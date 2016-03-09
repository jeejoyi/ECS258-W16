package processor;

import data_type.DataToProcess;

public class StupidProcessor implements IProcessor {

    private static StupidProcessor INSTANCE;

    /**
     * Get instance of Singleton
     *
     * @return instance
     */
    public static StupidProcessor getInstance() {
        if (INSTANCE != null) return INSTANCE;
        return INSTANCE = new StupidProcessor();
    }

    /**
     * Avoid new
     */
    private StupidProcessor() {

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
