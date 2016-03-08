import io.netty.channel.*;

import java.util.Calendar;
import java.util.Date;

public class RemoteSensor {

    //channel to set the priority etc..
    private final ChannelHandlerContext channel;


    // Data contained
    private Queuer queue = new Queuer();

    private Integer priority;

    // #### STATS SECTION ####

    // TODO can be the execution time etc
    private long time = 0;
    private Integer packetsIncomePastSecond = 0;
    private Integer priorityMagnitude;

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
        // TODO decrease the stats that has been increased in pop
        DataToProcess packet = queue.pop();

        return packet;
    }


    /**
     * Pop least important
     */
    public DataToProcess popLeastImportant() {
        // TODO decrease the stats that has been increased in pop
        DataToProcess packet = queue.pop();

        return packet;
    }


    // ################### PRIORITY SETTERS ###################

    /**
     * Tells to the client that can send messages with an lower priority
     */
    public void decreasePriority() {
        --priority;
        sendSetPriority();
    }


    /**
     * Tells to the client that can send messages with an higher priority
     */
    public void increasePriority() {
        ++priority;
        sendSetPriority();
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
}
