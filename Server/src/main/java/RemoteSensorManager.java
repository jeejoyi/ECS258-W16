import com.google.common.collect.MinMaxPriorityQueue;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

import java.rmi.Remote;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;


public class RemoteSensorManager {

    private static RemoteSensorManager INSTANCE;
    private final ConcurrentHashMap<String, RemoteSensor> remoteToSensor = new ConcurrentHashMap<>();
    private final ConcurrentLinkedQueue<String> sensorsList = new ConcurrentLinkedQueue<>();

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
        sensorsList.add(channel.channel().id().asShortText());
        remoteToSensor.put(channel.channel().id().asShortText(), sensor);
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
    public RemoteSensor getRemoteSensorUsingMostMemory() {
        RemoteSensor highest = null;
        for (RemoteSensor remoteSensor : getRemoteSensorsList()) {
            if (highest == null || remoteSensor.getMemoryUsage() > highest.getMemoryUsage()) {
                highest = remoteSensor;
            }
        }
        return highest;
    }

}


