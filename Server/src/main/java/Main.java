import graph.AnalysisGUI;
import network.ServerInit;
import processor.SmartProcessor;

public class Main {

    static final int PORT = 5005;

    public static void main(String[] args) throws Exception {
        //start server thread
        (new Thread(new ServerInit())).start();

        //sleep for 1000
        try {
            Thread.sleep(1000);
        } catch (Exception e) {

        }

        //now start GUI
        (new Thread(AnalysisGUI.getInstance())).start();

        // starting the remote processor
        (new SmartProcessor()).start();
    }
}
