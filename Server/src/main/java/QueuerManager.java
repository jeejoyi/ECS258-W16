
import javafx.util.Pair;

import javax.swing.*;
import javax.swing.Timer;
import javax.xml.crypto.Data;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

public class QueuerManager {

    public static Integer THRESHOLD_ACTIVATE = 70; // per cent
    public static Integer THRESHOLD_FORCE_FREE = 80; // per cent
    public static Integer THRESHOLD_DEACTIVATE = 60; // per cent
    public static Integer PRIORITIES = 10; // per cent

    private volatile boolean activateIncreasingNeededPriority = false;

    private long lastTimeAdded = 0;
    private final PriorityQueue<Pair<Long, RemoteSensor>> sensorHeap = new PriorityQueue<>();

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
                    if (MemoryInfo.usedPercentage() < THRESHOLD_DEACTIVATE) {
                        activateIncreasingNeededPriority = false;
                    }
                }
            }
        });
        timer.start();
    }

    // ####################### MANAGING CLIENTS #######################


    public void addClient(RemoteSensor remoteSensor) {
        sensorHeap.add(new Pair<Long, RemoteSensor>(getExecutionTime(remoteSensor.getNextPacket()), remoteSensor));
    }

    public void removeClient(RemoteSensor remoteSensor) {
    }

    // ####################### MANAGING FREEING MEMORY IN CASE IN WHICH IT'S NEEDED #############

    /**
     * Decides who has to free data for the incoming process
     *
     * @return returns when there is enough memory freed
     */
    public void freeFor(DataToProcess dataToProcess) {
        RemoteSensorManager remoteSensorManager = RemoteSensorManager.getInstance();

        long objectSize = dataToProcess.getMemorySize();
        while (objectSize > 0) {
            RemoteSensor highestMemorySensor = remoteSensorManager.getRemoteSensorUsingMostMemory();
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

    // ################### MANAGING PUSH AND POP #######################

    private long getExecutionTime(DataToProcess packet) {
        long executionTime = packet == null ? lastTimeAdded : packet.timestamp.getTime();
        lastTimeAdded = Math.min(executionTime, lastTimeAdded);
        return lastTimeAdded;
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

        if (MemoryInfo.usedPercentage() > THRESHOLD_FORCE_FREE) {
            freeFor(packet);
            // heavily decrease the traffic
            System.out.println("FORCING decreasing traffic, and freeing memory for the new packet");
            softenIncomingTraffic(2);
        }

        if (!activateIncreasingNeededPriority && MemoryInfo.usedPercentage() > THRESHOLD_ACTIVATE) {
            System.out.println("SUGGESTING decreasing traffic");
            softenIncomingTraffic();
        }

        RemoteSensorManager.getInstance().getRemoteSensor(fromChannel).push(packet);
    }


    public DataToProcess popPacket() {
        if (sensorHeap.isEmpty()) return null;
        Pair<Long, RemoteSensor> next = sensorHeap.poll();
        RemoteSensor remoteSensor = next.getValue();
        DataToProcess packet = remoteSensor.pop();

        // if there is no more data, but it is still active, I will reinsert it in the queue
        if (packet != null || remoteSensor.isActive()) {
            long time = getExecutionTime(packet);
            sensorHeap.add(new Pair<>(lastTimeAdded, remoteSensor));
        }

        return packet;
    }

}
