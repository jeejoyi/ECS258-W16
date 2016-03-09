import java.awt.*;
import java.awt.event.*;
/**
 * Created by CowCow on 3/8/16.
 */

public class AnalysisGUI extends Frame implements Runnable {
    private static AnalysisGUI INSTANCE;
    private static Button plotAllButton;
    private static Checkbox[] sensorCheckBox ;
    private static Plot[] plots;

    private static Plot totalMemoryUsage;
    private static String[] totalMemoryPlotSeries = {"total memory usage", "max memory usage"};
    private static float maxMemory = 999999;

    //singleton
    public static AnalysisGUI getInstance() {
        if (INSTANCE != null) return INSTANCE;
        return INSTANCE = new AnalysisGUI();
    }

    public AnalysisGUI()    {
        drawLayout();

        //window setting
        setSize(200, 600);
        setTitle("Statistical Analysis");
        setLayout(new FlowLayout());
        setResizable(false);
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
        removeAll();
        //add a title for window


        //delete object
        plotAllButton = null;

        //delete and adding all checkbox per client
        sensorCheckBox = null;
        sensorCheckBox = new Checkbox[RemoteSensorManager.getInstance().getRemoteSensorsNamesList().size()];
        int i = 0;
        for(String s: RemoteSensorManager.getInstance().getRemoteSensorsNamesList())    {
            //first delete checkbox
            sensorCheckBox[i] = new Checkbox(s);
            add(sensorCheckBox[i]);
            i++;
        }

        //plot button
        plotAllButton = new Button("Plot All");

        //button position
        plotAllButton.setBounds(150,550,50,50);
        //now add the button to the frame
        add(plotAllButton);

        //listener for button
        plotAllButton.addActionListener(new ActionListener()    {
            public void actionPerformed(ActionEvent e)  {
                System.out.println("clicked");
                //if theres no checkbox created
                if(sensorCheckBox == null)  {
                    return;
                }
                //check to see which checkbox is checked
                for(Checkbox cb: sensorCheckBox)    {
                    if(cb.getState() == true)  {
                        //plot?? ya
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
    }

}


