import com.google.common.collect.MinMaxPriorityQueue;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

import java.rmi.Remote;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by fre on 3/7/16.
 */

public class RemoteSensorManager {

    private static RemoteSensorManager INSTANCE;
    private final ConcurrentHashMap<String, RemoteSensor> remoteToSensor = new ConcurrentHashMap<>();
    private final ConcurrentLinkedQueue<String> sensorsList = new ConcurrentLinkedQueue<>();
    private final PriorityQueue<RemoteSensor> sensorHeap = new PriorityQueue<>(new Comparator<RemoteSensor>() {
        @Override
        public int compare(RemoteSensor o1, RemoteSensor o2) {
            return (int) (o1.getMemoryUsage() - o2.getMemoryUsage());
        }
    });

    /**
     * Get instance of Singleton
     *
     * @return instance
     */
    public static RemoteSensorManager getInstance() {
        if (INSTANCE != null) return INSTANCE;
        return INSTANCE = new RemoteSensorManager();
    }

    /**
     * Adding a new sensor client conneciton
     *
     * @param channel
     */
    public void addSensorChannel(ChannelHandlerContext channel) {

        RemoteSensor sensor = new RemoteSensor(channel);
        sensorHeap.add(sensor);
        sensorsList.add(channel.channel().id().asLongText());
        remoteToSensor.put(channel.channel().id().asLongText(), sensor);
    }


    // ################### GETTERS ###################
    public ConcurrentLinkedQueue<String> getRemoteSensorsNamesList() {
        return sensorsList;
    }

    /**
     * Returns the remote sensor class given the name (it's the network channel name).
     * It can be retrieved throught the function @getRemoteSensorsNamesList()
     *
     * @param name
     */
    public RemoteSensor getRemoteSensor(String name) {
        return remoteToSensor.get(name);
    }

    /**
     * Returns the list of Remote Sensors
     */
    public List<RemoteSensor> getRemoteSensorsList() {
        LinkedList<RemoteSensor> remoteSensors = new LinkedList<>();
        for (String name : getRemoteSensorsNamesList()) {
            remoteSensors.add(getRemoteSensor(name));
        }
        return remoteSensors;
    }

    // ################### ADVANCED MEMORY FREER ###################

    /**
     * Returns Remote Sensor with the highest usage of memory
     */
    public RemoteSensor getRemoteSensorsTopMemoryUsage() {
        RemoteSensor highest = null;
        for (RemoteSensor remoteSensor : getRemoteSensorsList()) {
            if (highest == null || remoteSensor.getMemoryUsage() > highest.getMemoryUsage()) {
                highest = remoteSensor;
            }
        }
        return highest;
    }


    // ################### HEAP SECTION TO MOVE ###################
    public PriorityQueue<RemoteSensor> getRemoteSensorHeap() {
        return sensorHeap;
    }

    public RemoteSensor getNextToProcess() {
        // logic to determine which sensor to process next
        RemoteSensor rSensor = sensorHeap.poll();
        return rSensor;
    }

    // ################### PROCESSOR TO MOVE ####################
    // Infinite loop to process next packet
    public void ProcessRemoteSensor() {
        while (true) {
            RemoteSensor remoteSensor = getNextToProcess();
            DataToProcess packet = remoteSensor.pop();

            // update heap after process
            sensorHeap.add(sensorHeap.peek());
            //  System.wait
        }
    }


}


