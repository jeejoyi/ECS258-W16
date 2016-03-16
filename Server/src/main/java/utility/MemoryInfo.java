package utility;

import remote_sensor.Queuer;
import remote_sensor.RemoteSensorManager;
import remote_sensor.RemoteSensor;

import java.text.NumberFormat;


public class MemoryInfo {
    private enum MODE {STATIC, DYNAMIC_JVM}

    private final static MODE mode = MODE.STATIC;
    private final static long staticTotalMemory = 1024 * 1024 * 20; //20MB

    /**
     * Gets the free memory
     */
    public static long totalFreeMemory() {
        Runtime runtime = Runtime.getRuntime();

        NumberFormat format = NumberFormat.getInstance();

        StringBuilder sb = new StringBuilder();
        long maxMemory = runtime.maxMemory();
        long allocatedMemory = runtime.totalMemory();
        long freeMemory = runtime.freeMemory();

        sb.append("free memory: " + format.format(freeMemory / 1024) + "<br/>");
        sb.append("allocated memory: " + format.format(allocatedMemory / 1024) + "<br/>");
        sb.append("max memory: " + format.format(maxMemory / 1024) + "<br/>");
        sb.append("total free memory: " + format.format((freeMemory + (maxMemory - allocatedMemory)) / 1024) + "<br/>");
        return (freeMemory + (maxMemory - allocatedMemory) / 1024);
    }

    /**
     * Returns the max memory that the queues can consume
     *
     * @return
     */
    public static long getMaxMemory() {
        if (mode == MODE.DYNAMIC_JVM) {
            Runtime runtime = Runtime.getRuntime();
            return runtime.maxMemory();
        } else {
            return staticTotalMemory;
        }
    }

    /**
     * Returns how many bytes the queues are consuming
     *
     * @return
     */
    public static long getUsedMemory() {
        if (mode == MODE.DYNAMIC_JVM) {
            Runtime runtime = Runtime.getRuntime();
            //TODO WROOONG, it is indicationg the freemoroy
            return runtime.freeMemory();
        } else {
            return Queuer.getTotalMemoryUsed();
        }
    }

    /**
     * Returns total number of Packet dropped from queue
     *
     * @return long
     */
    public static long getTotalPacketDropped()  {
        return Queuer.getTotalPacketsDropped();
    }

    /**
     * Returns total number of Packet currently in queue
     *
     * @return long
     */
    public static long getTotalPacketAlive()  {
        return Queuer.getTotalPacketsInQueues();
    }

    /**
     * Returns total memory used by devices
     *
     * @return long
     */

    public static long getTotalMemoryUsed() {return Queuer.getTotalMemoryUsed();};

    /**
     * Returns most total memory used by devices
     *
     * @return long
     */

    public static long getMostMemoryUsed() {
        RemoteSensor sensor = RemoteSensorManager.getInstance().getRemoteSensorUsingMostMemory();

        return (sensor != null) ? sensor.getMemoryUsage() : 0;
    }

    /**
     * Gets the free memory in percentage
     */
    public static long freePercentage() {
        return 100 - (getUsedMemory() * 100 / getMaxMemory());
    }


    /**
     * Gets the used memory in percentage
     */
    public static long usedPercentage() {
        return 100 - freePercentage();
    }


}
