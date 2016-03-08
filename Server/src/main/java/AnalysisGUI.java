import java.awt.*;
import java.awt.event.*;
/**
 * Created by CowCow on 3/8/16.
 */

public class AnalysisGUI extends Frame implements Runnable {
    private static Button plotAllButton;
    private static Checkbox[] sensorCheckBox ;
    private static Plot[] plots;

    public AnalysisGUI()    {
        System.out.println(RemoteSensorManager.getInstance().getRemoteSensorsNamesList().size());
//        sensorCheckBox = new Checkbox[RemoteSensorManager.getInstance().getRemoteSensorsNamesList().size()];
//        int i = 0;
//        for(String s: RemoteSensorManager.getInstance().getRemoteSensorsNamesList())    {
//            sensorCheckBox[i] = new Checkbox(s);
//            add(sensorCheckBox[i]);
//            i++;
//        }

        //plot button
        plotAllButton = new Button("Plot All");

        plotAllButton.setBounds(150,550,50,50);
        add(plotAllButton);

        //window setting
        setSize(200,600);
        setTitle("Statistical Analysis");
        setLayout(new FlowLayout());
        setResizable(false);
        setVisible(true);

        //listener for button
        plotAllButton.addActionListener(new ActionListener()    {
            public void actionPerformed(ActionEvent e)  {
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
    }

    public void run()   {
        System.out.println("GUI Started");
    }

}


