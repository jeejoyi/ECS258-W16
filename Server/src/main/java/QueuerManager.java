public class QueuerManager {

    public static Integer THRESHOLD_ACTIVATE = 80; // per cent
    public static Integer THRESHOLD_DEACTIVATE = 70; // per cent
    public static Integer PRIORITIES = 10; // per cent

    /**
     * Singleton
     */
    public static QueuerManager INSTANCE;

    /**
     * Get Instance of QueuerManager
     */
    public static QueuerManager getInstance() {
        if (INSTANCE != null) return INSTANCE;
        return INSTANCE = new QueuerManager();
    }

    /**
     * Decides who has to free data for the incoming process
     *
     * @return returns when there is enough memory freed
     */
    public void freeFor(DataToProcess dataToProcess) {
        RemoteSensorManager remoteSensorManager = RemoteSensorManager.getInstance();

        long objectSize = dataToProcess.getMemorySize();
        while (objectSize > 0) {
            RemoteSensor highestMemorySensor = remoteSensorManager.getRemoteSensorsTopMemoryUsage();
            if (highestMemorySensor.equals(dataToProcess)) {
                return;
            }

            // pop object from other lists
            objectSize -= highestMemorySensor.popLeastImportant();
        }

    }


    /**
     * Push a packet in the queue if possible
     *
     * @param fromChannel
     * @param dataToProcess
     */
    public void pushPacket(String fromChannel, DataToProcess dataToProcess) {
        // implement the policy of a new insertion
        if (dataToProcess.priority >= PRIORITIES) {
            // you can't insert data with a priority higher than the highest allowed
            return;
        }
        dataToProcess.sensor = fromChannel;

        //1) check if memory over the limit
        //2) if yes, delete the one with the highest memory usage
        //2.1) take action and send to the sensor the to backoff
        //3) insert new packet

        if (MemoryInfo.freePercentage() > THRESHOLD_ACTIVATE) {

        }

        RemoteSensorManager.getInstance().getRemoteSensor(fromChannel).push(dataToProcess);

    }


}
