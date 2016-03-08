import java.lang.instrument.Instrumentation;

public class ObjectSizeFetcher {
    private static Instrumentation instrumentation;

    public static void premain(String args, Instrumentation inst) {
        instrumentation = inst;
    }

    /**
     * Gets the size of an object
     *
     * @param o
     * @return
     */
    public static long getObjectSize(Object o) {
        return instrumentation.getObjectSize(o);
    }
}