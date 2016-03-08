import java.util.ArrayList;
import java.util.HashMap;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * Created by fre on 2/23/16.
 */
public class Queuer {

    public static Integer THRESHOLD_ACTIVATE = 80; // per cent
    public static Integer THRESHOLD_DEACTIVATE = 70; // per cent

    public static Integer PRIORITIES = 10; // per cent
    private int packetsForPriority[] = new int[10];
    private long currentPacketsInQueue = 0;

    private long memoryUsage = 0;

    enum PUSH_RESPONSE {
        INSERTED,
        INSERTED_WITH_ELIMINATION,
        NOT_INSERTED
    }

    ConcurrentHashMap<Integer, ConcurrentLinkedDeque<DataToProcess>> queues = new ConcurrentHashMap<>();

    private void freeMemory() {

    }

    public Queuer() {
        for (Integer i = 0; i < PRIORITIES; i++) {
            queues.put(i, new ConcurrentLinkedDeque<DataToProcess>());
        }
    }


    private boolean deleteALowerPriority(Integer priority) {
        for (Integer i = 0; i < PRIORITIES; i++) {
            ConcurrentLinkedDeque<DataToProcess> q = queues.get(i);
            if (!q.isEmpty()) {
                synchronized (q) {
                    q.removeFirst();
                }
                return true;
            }
        }
        return false;
    }

    private Boolean canInsert(DataToProcess dataToProcess) {
        if (MemoryInfo.freePercentage() > THRESHOLD_ACTIVATE) {
            // if we can't free memory to insert the current one, we can't insert it
            if (deleteALowerPriority(dataToProcess.priority) == false)
                return false;
            if (canInsert(dataToProcess)) {
                return true;
            } else {
                return false;
            }
        }

        // it needs to take into account the priority
        return true;
    }

    public void push(DataToProcess packet) {
        if (packet.priority >= PRIORITIES) {
            // you can't insert data with a priority higher than the highest allowed
            return;
        }
        if (canInsert(packet)) {
            ConcurrentLinkedDeque<DataToProcess> q = queues.get(packet.priority);
            synchronized (q) {
                // Add memory usage
                memoryUsage += ObjectSizeFetcher.getObjectSize(packet.data);

                // Increase the number of packets in the queue
                ++packetsForPriority[packet.priority];
                ++currentPacketsInQueue;

                q.addFirst(packet);
            }
        }
    }

    private DataToProcess getEldestInLine() {
        DataToProcess oldest = null;
        for (Integer i = 0; i < PRIORITIES; i++) {
            ConcurrentLinkedDeque<DataToProcess> q = queues.get(i);
            if (!q.isEmpty()) {
                if (q == null)
                    oldest = q.getLast();
                else if (oldest.timestamp.compareTo(q.getLast().timestamp) > 0) {
                    oldest = q.getLast();
                }

            }
        }
        return oldest;

    }

    public DataToProcess pop() {
        DataToProcess eldest = getEldestInLine();
        if (eldest == null) {
            return null;
        }
        ConcurrentLinkedDeque<DataToProcess> q = queues.get(eldest.priority);
        DataToProcess packet = q.removeLast();
        // Decrease memory usage
        memoryUsage -= ObjectSizeFetcher.getObjectSize(packet.data);

        // Decrease the number of packets in the queue
        --packetsForPriority[packet.priority];
        --currentPacketsInQueue;

        return packet;
    }

    // ################### GETTERS ###################

    /**
     * Getter packetsForPriority
     * returns an array with the count of the packets in each queue
     */
    public int[] getPacketsForPriority() {
        return packetsForPriority;
    }

    /**
     * Getter memory usage in bytes
     * returns how much memory it is currently using
     */
    public long getMemoryUsage() {
        return memoryUsage;
    }

    /**
     * Return the number of packets currently in the queue
     */
    public long getCurrentPacketsInQueue() {
        return currentPacketsInQueue;
    }
}
