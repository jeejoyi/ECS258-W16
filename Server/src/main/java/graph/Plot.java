package graph;

import remote_sensor.RemoteSensor;
import remote_sensor.RemoteSensorManager;
import utility.MemoryInfo;

import javax.swing.*;
import java.awt.*;
import java.util.Date;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class Plot extends JFrame    {
//    private JFrame frame;
    private RealTimeLinePlot[] charts;
    private JPanel[] plotsPanel;
    private int totalPlotInFrame = 0;

    private void createAllPlotsContainer(boolean requirePanel)  {
        //create all container for plots
        charts = new RealTimeLinePlot[this.totalPlotInFrame];
        if(requirePanel)    {
            plotsPanel = new JPanel[this.totalPlotInFrame];
        }
    }

    private void frameSetting(String windowTitles) {
        /*Frame configuration*/
        //set title for the frame
        setTitle(windowTitles);
        //frame setting
        setLayout(new BorderLayout());
        //make the frame non-resizeable
        setResizable(false);
        setVisible(true);
    }

    public Plot(final String windowTitles, final String[] plotTitles, final String[] XAxisTitles,
                final String[] YAxisTitles, final float[] maxYRange, final String[][] seriesTitles) {
        //set the number of plots in frame
        this.totalPlotInFrame = plotTitles.length;

        //create all container for plots
        createAllPlotsContainer(true);

        /*Frame configuration*/
        frameSetting(windowTitles);

        for(int i = 0; i < this.totalPlotInFrame; i++)  {
            //create a realtimeplot instance
            charts[i] = new RealTimeLinePlot(plotTitles[i], XAxisTitles[i], YAxisTitles[i], new Date(), maxYRange[i],
                                             seriesTitles[i].length, seriesTitles[i]);

            //add it into panel
            plotsPanel[i].add(charts[i]);

            //now add the panel to the frame
            add(plotsPanel[i]);
        }
        //pack w/e was added and changed
        pack();

        //display frame visible
        setVisible(true);

        //must call to ensure all changes
        revalidate();
        repaint();
    }

    public Plot(final String windowTitles, final String plotTitles, final String XAxisTitles,
                final String YAxisTitles, final float maxYRange, final String[] seriesTitles) {
        //set the number of plots in frame
        this.totalPlotInFrame = 1;

        //create all container for plots
        createAllPlotsContainer(false);

        /*Frame configuration*/
        frameSetting(windowTitles);

        charts[0] = new RealTimeLinePlot(plotTitles, XAxisTitles, YAxisTitles, new Date(), maxYRange,
                                         seriesTitles.length, seriesTitles);

        add(charts[0]);
        pack();

        //display frame

        //must call for after redraw
        revalidate();
        repaint();
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
    public RealTimeLinePlot getCharts(int index) {
        return charts[index];
    }

    //update all created graph. This function still need to be implemented
    public void updateGraph(RemoteSensor sensor) {
        if (sensor == null) {
            sensor = RemoteSensorManager.getInstance().getRemoteSensorUsingMostMemory();
            float data[] = {MemoryInfo.getUsedMemory(),
                            sensor == null ? 0 : sensor.getMemoryUsage()};
            charts[0].update(data);
        } else {
            float data[] = {sensor.getMemoryUsage()};
            for(int i = 0; i < this.totalPlotInFrame; i++)  {
                charts[i].update(data);
            }

        }


    }

    public void exit()  {
        setVisible(false);
        dispose();
    }
}
