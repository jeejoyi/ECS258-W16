package graph;

import remote_sensor.RemoteSensor;
import remote_sensor.RemoteSensorManager;
import utility.MemoryInfo;

import javax.swing.*;
import java.awt.*;
import java.util.Date;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class Plot {
    private JFrame frame;
    private RealTimeLinePlot charts;

    public Plot(final String windowTitles, final String plotTitles, final String XAxisTitles,
                final String YAxisTitles, final float maxYRange, final String[] seriesTitles) {
        //set window titles
        frame = new JFrame(windowTitles);
        //set default frame closing action
//        frames.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dimension d = new Dimension(400, 600);
        frame.setResizable(false);

        //create a realtimeplot
        charts = new RealTimeLinePlot(plotTitles, XAxisTitles, YAxisTitles, new Date(), maxYRange, seriesTitles.length,
                                        seriesTitles);
        //add the chart to the frame
        frame.add(charts);
        frame.pack();

        //display frame
        frame.setVisible(true);
    }

    //initial timer for update after creating the graph
    public void initialTimer(final RemoteSensor sensor) {
        Timer timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateGraph(sensor);
            }
        });
        timer.start();
    }

    //if needed return the charts for the frame
    public RealTimeLinePlot getCharts() {
        return charts;
    }

    //update all created graph. This function still need to be implemented
    public void updateGraph(RemoteSensor sensor) {
        if (sensor == null) {
            sensor = RemoteSensorManager.getInstance().getRemoteSensorUsingMostMemory();
            float data[] = {MemoryInfo.getUsedMemory(),
                            sensor == null ? 0 : sensor.getMemoryUsage()};
            charts.update(data);
        } else {
            float data[] = {sensor.getMemoryUsage()};
            charts.update(data);
        }


    }

    public void exit()  {
        frame.setVisible(false);
        frame.dispose();
    }
}
