
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class QueuerManager {

    public static Integer THRESHOLD_ACTIVATE = 70; // per cent
    public static Integer THRESHOLD_FORCE_FREE = 80; // per cent
    public static Integer THRESHOLD_DEACTIVATE = 60; // per cent
    public static Integer PRIORITIES = 10; // per cent

    private boolean activateIncreasingNeededPriority = false;

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

    private QueuerManager() {
        // create a timer that every second increase the priority
        Timer timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!activateIncreasingNeededPriority)
                    increaseIncomingTraffic();
                else {
                    // if activateIncreasingNeededPriority== true, and it is below threshold
                    // deactivate the increasing priority policy
                    if (MemoryInfo.freePercentage() < THRESHOLD_DEACTIVATE) {
                        activateIncreasingNeededPriority = false;
                    }
                }
            }
        });
        timer.start();
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

    public void softenIncomingTraffic(int levels) {
        RemoteSensorManager remoteSensorManager = RemoteSensorManager.getInstance();
        for (RemoteSensor remoteSensor : remoteSensorManager.getRemoteSensorsList()) {
            remoteSensor.increaseWorkingPriority();
        }
    }

    public void softenIncomingTraffic() {
        softenIncomingTraffic(1);
    }

    public void increaseIncomingTraffic() {
        RemoteSensorManager remoteSensorManager = RemoteSensorManager.getInstance();
        for (RemoteSensor remoteSensor : remoteSensorManager.getRemoteSensorsList()) {
            remoteSensor.decreaseWorkingPriority();
        }
    }

    /**
     * Push a packet in the queue if possible
     *
     * @param fromChannel
     * @param packet
     */
    public void pushPacket(String fromChannel, DataToProcess packet) {
        // implement the policy of a new insertion
        if (packet.priority >= PRIORITIES) {
            // you can't insert data with a priority higher than the highest allowed
            return;
        }
        packet.sensor = fromChannel;

        //1) check if memory over the limit
        //2) if yes, delete the one with the highest memory usage
        //2.1) take action and send to the sensor the to backoff
        //3) insert new packet

        if (MemoryInfo.freePercentage() > THRESHOLD_FORCE_FREE) {
            freeFor(packet);
            // heavily decrease the traffic
            softenIncomingTraffic(2);
        }

        if (!activateIncreasingNeededPriority && MemoryInfo.freePercentage() > THRESHOLD_ACTIVATE) {
            softenIncomingTraffic();
        }

        RemoteSensorManager.getInstance().getRemoteSensor(fromChannel).push(packet);
    }


}
