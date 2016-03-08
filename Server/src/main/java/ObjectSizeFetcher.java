import org.github.jamm.MemoryMeter;

import java.lang.instrument.Instrumentation;

public class ObjectSizeFetcher {
    private static MemoryMeter instrumentation = new MemoryMeter();

    /**
     * Gets the size of an object
     *
     * @param o
     * @return
     */
    public static long getObjectSize(Object o) {
        if (o instanceof String) {
            return ((String) o).length() * 4;
        }
        return instrumentation.measure(o);
    }
}