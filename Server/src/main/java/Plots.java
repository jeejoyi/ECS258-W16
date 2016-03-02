//import javax.swing.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
import java.util.Date;
//import java.util.Random;
import javax.swing.JFrame;
import java.awt.Dimension;
import java.util.Random;

/**
 * Created by CowCow on 3/1/16.
 *
 * Class Plots creates and keep track of all graph that will create, update graph at 1Hz according to plotDataInstance
 */

public class Plots {
    private JFrame[] frames;
    private RealTimeLinePlot[] charts;
    private PlotData plotDataInstance = new PlotData();

    public Plots(final String[] windowTitles, final String[] plotTitles, final String[] XAxisTitles,
                final String[] YAxisTitles, final String[] seriesTitles, final float[] maxYRange)   {
        if(windowTitles.length != plotTitles.length && windowTitles.length != YAxisTitles.length &&
                windowTitles.length != XAxisTitles.length && windowTitles.length != maxYRange.length){
            return;
        }

        //create N frames
        frames = new JFrame[windowTitles.length];
        charts = new RealTimeLinePlot[windowTitles.length];

        for(int i = 0; i < windowTitles.length; i++)    {
            //set window titles
            frames[i] = new JFrame(windowTitles[i]);
            //set default frame closing action
            frames[i].setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            Dimension d = new Dimension(400,600);
            frames[i].setResizable(false);

            //create a realtimeplot
            charts[i] = new RealTimeLinePlot(plotTitles[i], XAxisTitles[i], YAxisTitles[i],
                                            new Date(), seriesTitles[i], maxYRange[i]);
            //add the chart to the frame
            frames[i].add(charts[i]);
            frames[i].pack();

            //display frame
            frames[i].setVisible(true);
        }
    }

    //get ith frame instance
    public JFrame getFrames(int i) {
        return frames[i];
    }

    //get ith chart instance
    public RealTimeLinePlot getCharts(int i) {
        return charts[i];
    }

    //update all created graph. This function still need to be implemented
    public void updateAllGraph()    {
        Random rand = new Random();
        for(int i = 0; i < charts.length; i++)  {
            charts[i].update(rand.nextInt(100));
        }
    }

}
