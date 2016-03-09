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

        totalMemoryUsage = new Plot("Total Memory", "Over All System Usage", "Time", "Memory Usage", maxMemory,
                totalMemoryPlotSeries);
        totalMemoryUsage.initialTimer(null);
    }

    public void run()   {
        System.out.println("GUI Started");
    }

    public void drawLayout()   {
        //remove everything from the frame
        getContentPane().removeAll();
        //add a title for window
        add(new JLabel("    Select Devices to Plot"), BorderLayout.NORTH);

        //add checkbox panel
        checkBoxPanel = new JPanel();
        checkBoxPanel.setLayout(new GridLayout(0, 2));
//        //create N checkbox object for N sensors
//        sensorCheckBoxes = new JCheckBox[RemoteSensorManager.getInstance().getRemoteSensorsNamesList().size()];
        //add sensor checkbox into checkbox panel
        int i = 0;
        for(String s: RemoteSensorManager.getInstance().getRemoteSensorsNamesList())    {
            //first delete checkbox
            sensorCheckBoxes.put(s, new JCheckBox("Sensor "+ (i + 1)));
            checkBoxPanel.add(sensorCheckBoxes.get(s));
            checkBoxPanel.revalidate();
            checkBoxPanel.repaint();
            i++;
        }
        checkboxScrollPanel = new JScrollPane(checkBoxPanel);
        add(checkboxScrollPanel, BorderLayout.CENTER);


        //add a plot all button
        plotAllButton = new JButton("Plot All");
        add(plotAllButton, BorderLayout.SOUTH);

//        //listener for button
        plotAllButton.addActionListener(new ActionListener()    {
            public void actionPerformed(ActionEvent e)  {
                System.out.println("clicked");
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
                        String[] series = {"memory"};
                        System.out.println(s + " is checked");
                        sensorPlots.put(s, new Plot(s, "Usage", "Time", "Memory Usage", maxMemory, series));
                        sensorPlots.get(s).initialTimer(sensor);
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

}


