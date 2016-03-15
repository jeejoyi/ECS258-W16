package graph;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Date;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

interface updateFunction    {
    float getData();
}

public class Plot extends JFrame    {
    private int window_width = 1200;
    private int window_height = 300;
    private RealTimeLinePlot[] charts;
    private int totalPlotInFrame = 0;

    private boolean isWindowOpen = false;

    private void frameSetting(String windowTitles) {
        /*Frame configuration*/
        //set title for the frame
        setTitle(windowTitles);
        //set size
        setSize(this.window_width, this.window_height);
        //frame setting
        setLayout(new GridLayout(0, this.totalPlotInFrame));
        //make the frame non-resizeable
        setResizable(false);
        setVisible(true);

        //listener for close
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                exit();
            }
        });
    }

    public Plot(final String windowTitles, final String[] plotTitles, final String[] XAxisTitles,
                final String[] YAxisTitles, final String[][] seriesTitles, boolean[][] plotTotal) {
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
                                             new Date(), seriesTitles[i].length, seriesTitles[i], plotTotal[i],
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

        //set window is open
        this.isWindowOpen = true;
    }

    public Plot(final String windowTitles, final String plotTitles, final String XAxisTitles,
                final String YAxisTitles, final String[] seriesTitles, boolean[] plotTotal) {
        //set the number of plots in frame
        this.totalPlotInFrame = 1;

        //create all container for plots
        this.charts = new RealTimeLinePlot[this.totalPlotInFrame];

        /*Frame configuration*/
        frameSetting(windowTitles);

        charts[0] = new RealTimeLinePlot(plotTitles, XAxisTitles, YAxisTitles, new Date(),
                                         seriesTitles.length, seriesTitles, plotTotal,
                                         this.window_width, this.window_height);

        add(charts[0]);
        pack();

        //display frame

        //must call for after redraw
        revalidate();
        repaint();

        this.isWindowOpen = true;
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

    public boolean isActive()   {
        return this.isWindowOpen;
    }

    public void exit()  {
        this.isWindowOpen = false;
        setVisible(false);
        dispose();
    }
}

