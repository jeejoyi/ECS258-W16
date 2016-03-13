package graph;

import javax.swing.*;
import java.awt.*;
import java.util.Date;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

interface updateFunction    {
    float getData();
}

public class Plot extends JFrame    {
//    private JFrame frame;
    private int window_width = 1200;
    private int window_height = 300;
    private RealTimeLinePlot[] charts;
//    private JPanel[] plotsPanel;
    private int totalPlotInFrame = 0;

    private void frameSetting(String windowTitles) {
        /*Frame configuration*/
        //set title for the frame
        setTitle(windowTitles);
        //set size
        setSize(this.window_width, this.window_height);
        //frame setting
        setLayout(new GridLayout(0, 3));
        //make the frame non-resizeable
        setResizable(false);
        setVisible(true);
    }

    public Plot(final String windowTitles, final String[] plotTitles, final String[] XAxisTitles,
                final String[] YAxisTitles, final String[][] seriesTitles) {
        //set the number of plots in frame
        this.totalPlotInFrame = plotTitles.length;

        /*Frame configuration*/
        frameSetting(windowTitles);

        //create all container for plots
        this.charts = new RealTimeLinePlot[this.totalPlotInFrame];

        for(int i = 0; i < this.totalPlotInFrame; i++)  {
//            panel configuration
            //create a realtimeplot instance
            this.charts[i] = new RealTimeLinePlot(plotTitles[i], XAxisTitles[i], YAxisTitles[i],
                                             new Date(), seriesTitles[i].length, seriesTitles[i],
                                             this.window_width/this.totalPlotInFrame, this.window_height);
//            this.plotsPanel[i] = new JPanel();
            //create a panel to add to the frame for multiple plot
            JPanel plotPanel = new JPanel();
            plotPanel.add(this.charts[i]);
            //add the panel to the frame
            add(plotPanel);
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
                final String YAxisTitles, final String[] seriesTitles) {
        //set the number of plots in frame
        this.totalPlotInFrame = 1;

        //create all container for plots
        this.charts = new RealTimeLinePlot[this.totalPlotInFrame];

        /*Frame configuration*/
        frameSetting(windowTitles);

        charts[0] = new RealTimeLinePlot(plotTitles, XAxisTitles, YAxisTitles, new Date(),
                                         seriesTitles.length, seriesTitles, this.window_width, this.window_height);

        add(charts[0]);
        pack();

        //display frame

        //must call for after redraw
        revalidate();
        repaint();
    }

    //initial timer for update after creating the graph
    public void initialTimer(final updateFunction[][] dataFunctions) {
        Timer timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updatePlot(dataFunctions);
            }
        });
        timer.start();
    }

    //if needed return the charts for the frame
    public RealTimeLinePlot getCharts(int index) {
        return charts[index];
    }

    //update all created graph. This function still need to be implemented
    private void updatePlot(updateFunction[][] dataFunctions) {
        if(dataFunctions.length != this.totalPlotInFrame)    {
            return;
        }
        //for every plot
        for(int i = 0; i < this.totalPlotInFrame; i++)  {
            float[] data = new float[dataFunctions[i].length];
            //for every series in the plot
            for(int j = 0; j < dataFunctions[i].length; j++) {
                data[j] = dataFunctions[i][j].getData();
            }
            charts[i].update(data);
        }
    }
//    public void updateGraph(RemoteSensor sensor) {
//        if (sensor == null) {
//            sensor = RemoteSensorManager.getInstance().getRemoteSensorUsingMostMemory();
//            float data[] = {MemoryInfo.getUsedMemory(),
//                            sensor == null ? 0 : sensor.getMemoryUsage()};
//            charts[0].update(data);
//        } else {
//            float data[] = {sensor.getMemoryUsage()};
//            for(int i = 0; i < this.totalPlotInFrame; i++)  {
//                charts[i].update(data);
//            }
//
//        }
//
//
//    }

    public void exit()  {
        setVisible(false);
        dispose();
    }
}

