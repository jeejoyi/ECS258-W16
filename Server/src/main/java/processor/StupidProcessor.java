package processor;

import dataType.DataToProcess;

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

    private StupidProcessor() {

    }


    @Override
    public void process(DataToProcess packet) {

    }
}
