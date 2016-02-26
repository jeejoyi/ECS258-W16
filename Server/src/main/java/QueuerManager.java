
public class QueuerManager {

    public static QueuerManager INSTANCE;

    private Queuer queuer;

    public static QueuerManager getInstance() {
        if (INSTANCE != null) return INSTANCE;
        return INSTANCE = new QueuerManager();
    }

    private QueuerManager() {
        queuer = new Queuer();
    }


    public Queuer getQueuer() {
        return queuer;
    }
}
