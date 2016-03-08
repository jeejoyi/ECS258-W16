import javax.swing.*;
import java.awt.*;
import java.util.Date;
import java.util.Random;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by CowCow on 3/8/16.
 */
public class Plot {
    private JFrame frames;
    private RealTimeLinePlot charts;

    public Plot(final String windowTitles, final String plotTitles, final String XAxisTitles,
                 final String YAxisTitles, final float maxYRange, final String[] seriesTitles)   {
        //set window titles
        frames = new JFrame(windowTitles);
        //set default frame closing action
//        frames.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dimension d = new Dimension(400,600);
        frames.setResizable(false);

        //create a realtimeplot
        charts = new RealTimeLinePlot(plotTitles, XAxisTitles, YAxisTitles, new Date(), maxYRange, seriesTitles.length,
                                        seriesTitles);
            //add the chart to the frame
        frames.add(charts);
        frames.pack();

        //display frame
        frames.setVisible(true);

        Timer timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateGraph();
            }
        });
        timer.start();
    }
    //update all created graph. This function still need to be implemented
    public void updateGraph()    {
        Random rand = new Random();
        float[] data = {rand.nextInt(100)};
        charts.update(data);
    }
}
