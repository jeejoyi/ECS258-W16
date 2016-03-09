package graph;

import remote_sensor.RemoteSensor;
import remote_sensor.RemoteSensorManager;

import javax.swing.*;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.event.*;
import javax.swing.JScrollPane;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.HashMap;
import java.util.Map;

public class AnalysisGUI extends JFrame implements Runnable {
    private static AnalysisGUI INSTANCE;
    private static JPanel checkBoxPanel;
    private static JScrollPane checkboxScrollPanel;
    private static JButton plotAllButton;
    private static Map<String, JCheckBox> sensorCheckBoxes = new HashMap<>();
    private static Map<String, Plot> sensorPlots = new HashMap<>();

    private static Plot totalMemoryUsage;
    private static String[] totalMemoryPlotSeries = {"total memory usage", "max memory usage"};
    private static float maxMemory = 999999;

    //singleton
    public static AnalysisGUI getInstance() {
        if (INSTANCE != null) return INSTANCE;
        return INSTANCE = new AnalysisGUI();
    }

    public AnalysisGUI()    {
        //window setting
        setSize(200, 700);
        setTitle("Statistical Analysis");
        setLayout(new BorderLayout());
        setResizable(false);

        drawLayout();

        setVisible(true);

        totalMemoryUsage = new Plot("Total Memory", "Over All System Usage", "Time", "Memory Usage (bytes)", maxMemory,
                                    totalMemoryPlotSeries);
        totalMemoryUsage.initialTimer(null);
    }

    public void run()   {
        System.out.println("GUI Started");
    }

    public void drawLayout()   {
        //remove everything from the frame
        getContentPane().removeAll();
        //add instruction for the GUI
        JLabel instruction = new JLabel("Select Devices to Plot");
        //set the word to align to the center
        instruction.setHorizontalAlignment(JLabel.CENTER);
        //the label stick to the top
        add(instruction, BorderLayout.NORTH);


        //add checkbox panel
        checkBoxPanel = new JPanel();
        checkBoxPanel.setLayout(new GridLayout(0, 2));

        //add sensor checkbox into checkbox panel
        int i = 0;
        for(String s: RemoteSensorManager.getInstance().getRemoteSensorsNamesList())    {
            //create a new checkbox object and put it into the map by device name
            if(sensorCheckBoxes.get(s) == null){
                sensorCheckBoxes.put(s, new JCheckBox("Sensor "+ (i + 1)));
            }
            //add the checkbox to the panel
            checkBoxPanel.add(sensorCheckBoxes.get(s));
            //re configure the panel
            checkBoxPanel.revalidate();
            checkBoxPanel.repaint();
            i++;
        }
        //add a scrollable panel with checkBox Panel
        checkboxScrollPanel = new JScrollPane(checkBoxPanel);
        add(checkboxScrollPanel, BorderLayout.CENTER);


        //add a plot all button
        plotAllButton = new JButton("Plot All");
        add(plotAllButton, BorderLayout.SOUTH);

//        //listener for button
        plotAllButton.addActionListener(new ActionListener()    {
            public void actionPerformed(ActionEvent e)  {
                //if theres no checkbox created
                if(sensorCheckBoxes == null)  {
                    return;
                }
                //check to see which checkbox is checked
                for(String s: RemoteSensorManager.getInstance().getRemoteSensorsNamesList())    {
                    //get the sensor instance
                    RemoteSensor sensor = RemoteSensorManager.getInstance().getRemoteSensor(s);
                    if(sensorCheckBoxes.get(s).isSelected())    {
                        //graph it
                        if(sensorPlots.get(s) == null)  {
                            String[] series = {"memory"};
                            System.out.println(s + " is checked");
                            sensorPlots.put(s, new Plot(s, "Usage", "Time", "Memory Usage (bytes)", maxMemory, series));
                            sensorPlots.get(s).initialTimer(sensor);
                        }
                    }
                }
            }
        });

        //listener for close
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                dispose();
                System.exit(0);
            }
        });

        //must call for after redraw
        revalidate();
        repaint();
    }

    //close the graph if it was display
    public void closeGraph(String name) {
        sensorCheckBoxes.remove(name);
        Plot usagePlot = sensorPlots.get(name);
        //if there is a graph opened
        if(usagePlot != null)   {
            //close the graph
            usagePlot.exit();
        }
    }

    public JCheckBox getCheckBox(String name)   {
        return sensorCheckBoxes.get(name);
    }

    public Plot getPlot(String name)    {
        return sensorPlots.get(name);
    }

}


