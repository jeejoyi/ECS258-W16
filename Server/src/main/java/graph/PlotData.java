package graph;

/**
 * Created by CowCow on 3/1/16.
 */
public class PlotData {
    private static PlotData classInstance = null;
    private float currentMemoryUsage = 0;
    private float currentCPUUsage = 0;
    private float[] currentQueueUsage = new float[10];

    protected PlotData() {
        // Exists only to defeat instantiation.
    }
    public static PlotData getInstance() {
        if(classInstance == null) {
            classInstance = new PlotData();
        }
        return classInstance;
    }

    public void setCurrentCPUUsage(float currentCPUUsage) {
        this.currentCPUUsage = currentCPUUsage;
    }

    public float getCurrentCPUUsage() {
        return currentCPUUsage;
    }

    public void setCurrentMemoryUsage(float currentMemoryUsage) {
        this.currentMemoryUsage = currentMemoryUsage;
    }

    public float getCurrentMemoryUsage() {
        return currentMemoryUsage;
    }

    public void setCurrentQueueUsage(int i, float currentQueueUsage) {
        this.currentQueueUsage[i] = currentQueueUsage;
    }

    public float getCurrentQueueUsage(int i) {
        return currentQueueUsage[i];
    }
}
