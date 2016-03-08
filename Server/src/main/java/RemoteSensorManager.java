import com.google.common.collect.MinMaxPriorityQueue;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

import java.rmi.Remote;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by fre on 3/7/16.
 */

public class RemoteSensorManager {

    private static RemoteSensorManager INSTANCE;
    private final ConcurrentHashMap<String, RemoteSensor> remoteToSensor = new ConcurrentHashMap<>();
    private final ConcurrentLinkedQueue<String> sensorsList = new ConcurrentLinkedQueue<>();
    private final PriorityQueue<RemoteSensor> sensorHeap = new PriorityQueue<>(0, new Comparator<RemoteSensor>() {
        @Override
        public int compare(RemoteSensor o1, RemoteSensor o2) {
            return (int) (o1.getMemoryUsage() - o2.getMemoryUsage());
        }
    });

    public static RemoteSensorManager getInstance() {
        if (INSTANCE != null) return INSTANCE;
        return INSTANCE = new RemoteSensorManager();
    }

    public void addSensorChannel(ChannelHandlerContext channel) {

        RemoteSensor sensor = new RemoteSensor(channel);
        sensorHeap.add(sensor);
        remoteToSensor.put(channel.name(), sensor);
    }

    public PriorityQueue<RemoteSensor> getRemoteSensorHeap() {
        return sensorHeap;
    }


    public ConcurrentLinkedQueue<String> getRemoteSensorsNamesList() {
        return sensorsList;
    }

    public RemoteSensor getRemoteSensor(String name) {
        return remoteToSensor.get(name);
    }

    public RemoteSensor getNextToProcess() {
        // logic to determine which sensor to process next
        RemoteSensor rSensor = sensorHeap.poll();
        return rSensor;
    }

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


