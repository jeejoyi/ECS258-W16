package graph; /**
 * Created by CowCow on 3/1/16.
 * the following class is modified source code from trashgod from stackoverflow
 * url: http://stackoverflow.com/questions/21299096/jfreechart-how-to-show-real-time-on-the-x-axis-of-a-timeseries-chart/21307289#21307289
 *
 * Class RealTimeLinePlot build on top of JFreeChart's TimeSeries Chart
 * Plot is being update in a rate of 1Hz
 */

import java.text.SimpleDateFormat;
import javax.swing.JPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.DynamicTimeSeriesCollection;
import org.jfree.data.time.Second;
import java.util.Date;


public class RealTimeLinePlot extends JPanel {
    //private class variable
    private final DynamicTimeSeriesCollection dataset;
    private final JFreeChart chart;
    private final int numSeries;

    public RealTimeLinePlot(final String chartTitle, final String XAxisTitle, final String YAxisTitle,
                            final Date current_Date, final int numSeries, final String[] seriesTitle,
                            int width, int height) {
        this.numSeries = numSeries;

        //init dataset as a dynamicTimeSeriesCollision class with nSeries = 1, nMoments = 1000, time sample = second
        dataset = new DynamicTimeSeriesCollection(numSeries, 1000, new Second());
        //X axis is based on current time in seconds
        dataset.setTimeBase(new Second(current_Date));  //somehow the min is +16 of whats the current minutes
        //add all Series into the dataset
        for(int i = 0; i < numSeries; i++)  {
            dataset.addSeries(new float[1], i, seriesTitle[i]);
        }

        //create the chart, ChartFactory is the super class of chart
        chart = ChartFactory.createTimeSeriesChart(chartTitle, XAxisTitle, YAxisTitle, dataset, true, true, false);
        //get the XYplot from chart
        final XYPlot plot = chart.getXYPlot();
        //get the domain Axis from XYPlot
        DateAxis axis = (DateAxis) plot.getDomainAxis();
        //Sets the fixed auto range for the axis
        axis.setFixedAutoRange(100000);
        //set X axis display format, since we set it to base on time in seconds.
        axis.setDateFormatOverride(new SimpleDateFormat("mm.ss"));
        //create chart panel
        final ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(width, height));
//        chartPanel.setPreferredSize(new java.awt.Dimension(400, 300));
        chartPanel.setMouseZoomable(true, true);
//        dont know where it add it to
        add(chartPanel);
    }

    //update the graph by appending new values
    public void update(float[] value) {
        int currentIndex = dataset.getNewestIndex();
        dataset.advanceTime();
        for(int i = 0; i < this.numSeries; i++) {
            dataset.addValue(i, currentIndex, value[i]);
        }
    }
}
