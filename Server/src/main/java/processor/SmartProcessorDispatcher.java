package processor;

import data_type.DataToProcess;
import remote_sensor.QueuerManager;

public class SmartProcessorDispatcher extends Thread {
    public enum MODE {STUPID}

    public static final MODE mode = MODE.STUPID;

    @Override
    public void run() {
        // Infinite loop to process next packet
        while (true) {
            DataToProcess packet = QueuerManager.getInstance().popPacket();
            if (packet == null) {
                try {
                    Thread.sleep(1000L);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                continue;
            }
            processingFunction(packet);
        }

    }

    private void processingFunction(DataToProcess packet) {
        switch (mode) {
            case STUPID:
                StupidProcessor.getInstance().process(packet);
                break;
            // in the future there will be other cases, and maybe dynamic loading of the policies
            // probably throught a pattern observer
        }

    }

}
