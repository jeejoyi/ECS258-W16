package graph;

import remote_sensor.Queuer;
import remote_sensor.RemoteSensor;
import remote_sensor.RemoteSensorManager;
import utility.MemoryInfo;

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

//    private static Plot totalMemoryUsage;
//    private static String[] totalMemoryPlotSeries = {"total memory usage", "max memory usage"};
//    private static float maxMemory = 999999;

    //singleton
    public static AnalysisGUI getInstance() {
        if (INSTANCE != null) return INSTANCE;
        return INSTANCE = new AnalysisGUI();
    }

    //easy allocate for updateFunctions
    private updateFunction[][] allocateUpdateFunction(String[] plots, String[][] series) {
        updateFunction temp[][] = new updateFunction[plots.length][];
        for (int i = 0; i < series.length; i++) {
            temp[i] = new updateFunction[series[i].length];
        }
        return temp;
    }

    public AnalysisGUI()    {
        //window setting
        setSize(200, 700);
        setTitle("Statistical Analysis");
        setLayout(new BorderLayout());
        setResizable(false);
        drawLayout();
        setVisible(true);


        //plot for server over all usage
        String windowTitle = "Over All Server Usage";
        String[] plotTitles = {"Memory Usage (Byte)", "Memory Usage (%)", "Packets In System"};
        String[] XAxisTitles = {"Time", "Time", "Time"};
        String[] YAxisTitles = {"Memory (Byte)", "Precentage (%)", "Packets"};
        String[][] seriesTitles = {{"Used Most Memory", "Total Memory"},
                                   {"Free", "Used"},
                                   {"Dropped", "Alive"}};
        //construct the plot
        Plot serverUsage = new Plot(windowTitle, plotTitles, XAxisTitles, YAxisTitles, seriesTitles);
        //construct updat function for each plot
        updateFunction seriesUpdateFunction[][] = allocateUpdateFunction(plotTitles, seriesTitles); //new updateFunction[plotTitles.length][seriesTitles];
        //Memory Usage (Byte)
        seriesUpdateFunction[0][0] = new updateFunction() {
            @Override
            public float getData() {
                return MemoryInfo.getMostMemoryUsed();
            }
        };
        seriesUpdateFunction[0][1] = new updateFunction() {
            @Override
            public float getData() {
                return MemoryInfo.getTotalMemoryUsed();
            }
        };

        //Memory Usage (%)
        seriesUpdateFunction[1][0] = new updateFunction() {
            @Override
            public float getData() {
                return MemoryInfo.freePercentage();
            }
        };
        seriesUpdateFunction[1][1] = new updateFunction() {
            @Override
            public float getData() {
                return MemoryInfo.usedPercentage();
            }
        };

        //Packets In System
        seriesUpdateFunction[2][0] = new updateFunction() {
            @Override
            public float getData() {
                return MemoryInfo.getTotalPacketDropped();
            }
        };
        seriesUpdateFunction[2][1] = new updateFunction() {
            @Override
            public float getData() {
                return MemoryInfo.getTotalPacketAlive();
            }
        };


        //initial timer
        serverUsage.initialTimer(seriesUpdateFunction);

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
                for(String sensorName: RemoteSensorManager.getInstance().getRemoteSensorsNamesList())    {
                    //get the sensor instance
                    RemoteSensor sensor = RemoteSensorManager.getInstance().getRemoteSensor(sensorName);
                    //if the check box checked, and have not already opened the window
                    //plot it
                    if((sensorCheckBoxes.get(sensorName).isSelected()) && (sensorPlots.get(sensorName) == null))    {
                        //plot the Usage for the device
                        String windowTitle = "Device: " + sensorName;
                        String[] plotTitles = {"Memory Usage (Byte)", "Packets In System"};
                        String[] XAxisTitles = {"Time", "Time"};
                        String[] YAxisTitles = {"Memory (Byte)", "Packets"};
                        String[][] seriesTitles = {{"Total Memory Used"},
                                                   {"Dropped", "Alive", "Priority0", "Priority1", "Priority2",
                                                    "Priority3", "Priority4", "Priority5", "Priority6", "Priority7",
                                                    "Priority8", "Priority9"}};
                        sensorPlots.put(sensorName, new Plot(windowTitle, plotTitles, XAxisTitles, YAxisTitles,
                                                             seriesTitles));
                        //construct series update function
                        //construct updat function for each plot
                        updateFunction seriesUpdateFunction[][] = allocateUpdateFunction(plotTitles, seriesTitles);

                        //Memory Usage (Byte)
                        seriesUpdateFunction[0][0] = new updateFunction() {
                            @Override
                            public float getData() {
                                return sensor.getMemoryUsage();
                            }
                        };
                        //packet in system
                        seriesUpdateFunction[1][0] = new updateFunction() {
                            @Override
                            public float getData() {
                                return sensor.getPacketsDropped();
                            }
                        };
                        seriesUpdateFunction[1][1] = new updateFunction() {
                            @Override
                            public float getData() {
                                return sensor.getCurrentPacketsInQueue();
                            }
                        };
                        for(int i = 0; i < 10; i++) {
                            final int index = i;
                            seriesUpdateFunction[1][i + 2] = new updateFunction() {
                                @Override
                                public float getData() {
                                    return sensor.getPacketsForPriority()[index];
                                }
                            };
                        }
                        //initial timer
                        sensorPlots.get(sensorName).initialTimer(seriesUpdateFunction);
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


