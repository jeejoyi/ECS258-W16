
public class SmartProcessor extends Thread {
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
        try {
            this.sleep(packet.getMemorySize());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
