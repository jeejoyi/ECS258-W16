import io.netty.channel.*;

import java.util.Calendar;

public class RemoteSensor {

    //channel to set the priority etc..
    private final ChannelHandlerContext channel;


    // Data contained
    private final Queuer queue = new Queuer();

    private volatile int priority = 0;

    private volatile boolean isActive = true;
    // #### STATS SECTION ####

    // TODO can be the execution time etcs
    private long time = 0;
    private Integer packetsIncomePastSecond = 0;
    private Integer priorityMagnitude = 0;

    private long packetsDropped = 0;
    private long packetSizeDropped = 0;

    // ################### QUEUE ###################
    //

    public int getScore() {
        return priorityMagnitude;
    }

    /**
     * Push some data in its queue
     */
    public void push(DataToProcess packet) {
        // TODO add statistics
        // for example: n of packets, total size of queue etc.. and add in general stats

        packet.timestamp = Calendar.getInstance().getTime();

        // increase the cumulative priority
        priorityMagnitude += packet.priority;

        queue.push(packet);
    }

    /**
     * Pop some data from its queue
     */
    public DataToProcess pop() {
        DataToProcess packet = queue.pop();
        return packet;
    }

    /**
     * Get next to execute without popping
     */
    public DataToProcess getNextPacket() {
        DataToProcess packet = queue.getNextToProcess();
        return packet;
    }

    /**
     * Pop least important
     *
     * @return freed memory
     */
    public long popLeastImportant() {
        long freedMemory = queue.deletedALowerPriority();

        //calls garbage collector
        System.gc();
        return freedMemory;
    }

    // ################### PRIORITY SETTERS ###################

    /**
     * Tells to the client that can send messages with an lower priority
     */
    public void decreaseWorkingPriority() {
        decreaseWorkingPriority(1);
    }

    /**
     * Tells to the client that can send messages with an lower priority
     */
    public void decreaseWorkingPriority(int levels) {
        if (priority > 0) {
            priority -= levels;
            sendSetPriority();
        }
    }

    public void increaseWorkingPriority() {
        increaseWorkingPriority(1);
    }

    /**
     * Tells to the client that can send messages with an higher priority
     */
    public void increaseWorkingPriority(int levels) {
        if (priority < Queuer.PRIORITIES) {
            priority += levels;
            sendSetPriority();
        }
    }

    // ################### NETWORK ###################

    /**
     * Constructor
     * It needs the channel to send to the sensor client messages
     *
     * @param channel
     */
    public RemoteSensor(final ChannelHandlerContext channel) {
        this.channel = channel;
    }

    /**
     * Order to the client to update the priority
     */
    private void sendSetPriority() {
        channel.writeAndFlush("{type:'set_priority', priority:'" + priority + "'}\n");
    }

    // ################### GETTERS ###################

    /**
     * Getter packetsForPriority
     * returns an array with the count of the packets in each queue
     */
    public int[] getPacketsForPriority() {
        return queue.getPacketsForPriority();
    }

    /**
     * Getter memory usage in bytes
     * returns how much memory it is currently using
     */
    public long getMemoryUsage() {
        return queue.getMemoryUsage();
    }

    /**
     * Return the number of packets currently in the queue
     */
    public long getCurrentPacketsInQueue() {
        return queue.getCurrentPacketsInQueue();
    }

    /**
     * Return true if the connection is still active
     */
    public boolean isActive() {
        return isActive;
    }
}
