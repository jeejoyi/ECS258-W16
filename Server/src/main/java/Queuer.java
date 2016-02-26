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

    enum PUSH_RESPONSE {
        INSERTED,
        INSERTED_WITH_ELIMINATION,
        NOT_INSERTED
    }

    ConcurrentHashMap<Integer, ConcurrentLinkedDeque<DataToProcess>> queues;

    private void freeMemory() {

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

    public void push(DataToProcess dataToProcess) {
        if (dataToProcess.priority >= PRIORITIES) {
            // you can't insert data with a priority higher than the highest allowed
            return;
        }
        if (canInsert(dataToProcess)) {
            ConcurrentLinkedDeque<DataToProcess> q = queues.get(dataToProcess.priority);
            synchronized (q) {
                q.addFirst(dataToProcess);
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
        return q.removeLast();
    }
}
