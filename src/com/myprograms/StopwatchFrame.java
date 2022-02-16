package com.myprograms;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.text.ParseException;

public class StopwatchFrame extends JFrame
{
    public final int DEFAULT_WIDTH = 640;
    public final int DEFAULT_HEIGHT = 480;
    private JFormattedTextField textField;
    private JButton startButton;
    private JButton resetButton;
    private JButton stopButton;
    private JMenuItem stopwatchMenu;
    private JMenuItem countdownMenu;
    private JMenuItem exitMenu;
    //mode: false - means stopwatch mode, true - means countdown mode
    private boolean mode;
    private final Timer timer;
    private long startStopwatchTime;
    private long endStopwatchTime;
    private long startCountdownTime;
    private long endCountdownTime;
    private long stopwatchCurrentValue;
    private long countdownCurrentValue;
    private long HH;
    private long MM;
    private long SS;
    private long sss;

    public StopwatchFrame() throws ParseException
    {
        //Setting constant window size at the screen center
        Toolkit kit = Toolkit.getDefaultToolkit();
        int screenWidth = kit.getScreenSize().width;
        int screenHeight = kit.getScreenSize().height;
        setBounds((screenWidth - DEFAULT_WIDTH) / 2, (screenHeight - DEFAULT_HEIGHT) / 2, DEFAULT_WIDTH, DEFAULT_HEIGHT);

        setResizable(false);
        setTitle("Stoper");

        //Default mode at startup is stopwatch
        mode = false;

        //Method for creating UI elements
        createUI();

        //Method for adding ActionListener to each element
        addActionListeners();

        //Timer responsible for refreshing the time at textField
        timer = new Timer(1, event ->
        {
            if (!mode)
            {
                endStopwatchTime = System.currentTimeMillis();
                stopwatchCurrentValue = endStopwatchTime - startStopwatchTime;
                var seconds = stopwatchCurrentValue / 1000;
                HH = seconds / 3600;
                MM = seconds / 60 % 60;
                SS = seconds % 60;
                sss = stopwatchCurrentValue % 1000;
                textField.setText(String.format("%02d:%02d:%02d.%03d", HH, MM, SS, sss));
            }
        });
    }
    //ParseException due to use of MaskFormatter
    public void createUI() throws ParseException
    {
        var bar = new JMenuBar();
        setJMenuBar(bar);

        var mainMenu = new JMenu("Menu");
        bar.add(mainMenu);

        stopwatchMenu = new JMenuItem("Stoper");
        mainMenu.add(stopwatchMenu);

        countdownMenu = new JMenuItem("Minutnik");
        mainMenu.add(countdownMenu);

        stopwatchMenu.setEnabled(false);
        exitMenu = new JMenuItem("Wyjście");
        mainMenu.add(exitMenu);

        var panel1 = new JPanel();
        var panel2 = new JPanel();
        panel1.setLayout(new BorderLayout());
        setLayout(new GridLayout(2, 1));

        //Setting HH:MM:SS:msmsms format of the textField
        MaskFormatter format = new MaskFormatter("##:##:##.###");
        textField = new JFormattedTextField(format);
        textField.setHorizontalAlignment(0);
        HH = 0;
        MM = 0;
        SS = 0;
        sss = 0;
        //Creating string to display correct format
        String time = String.format("%02d:%02d:%02d.%03d", HH, MM, SS, sss);
        textField.setText(time);
        textField.setFont(new Font("Lucida Sans Console", 1, 42));

        panel1.add(textField, "Center");
        //Creating display area by adding 2 JPanels with GridLayout
        add(panel1);
        panel2.setLayout(new GridLayout(1, 3));
        add(panel2);

        startButton = new JButton("START");
        startButton.setFont(new Font("Lucida Sans Console", 0, 28));
        panel2.add(startButton);

        stopButton = new JButton("STOP");
        stopButton.setFont(new Font("Lucida Sans Console", 0, 28));
        panel2.add(stopButton);

        resetButton = new JButton("RESET");
        resetButton.setFont(new Font("Lucida Sans Console", 0, 28));
        panel2.add(resetButton);

        textField.setEditable(false);
    }

    public void addActionListeners()
    {
        //Actions when Stopwatch is selected from main menu
        stopwatchMenu.addActionListener((event) ->
        {
            mode = false;
            stopwatchMenu.setEnabled(false);
            countdownMenu.setEnabled(true);
            textField.setEditable(false);
            setTitle("Stoper");
            var seconds = stopwatchCurrentValue / 1000;
            HH = seconds / 3600;
            MM = seconds / 60 % 60;
            SS = seconds % 60;
            sss = stopwatchCurrentValue % 1000;
            textField.setText(String.format("%02d:%02d:%02d.%03d", HH, MM, SS, sss));

        });
        //Actions when Countdown timer is selected from main menu
        countdownMenu.addActionListener((event) ->
        {
            mode = true;
            stopwatchMenu.setEnabled(true);
            countdownMenu.setEnabled(false);
            textField.setEditable(true);
            setTitle("Minutnik");
        });

        //Actions when Exit is selected from main menu
        exitMenu.addActionListener((event) ->
        {
            System.exit(0);
        });

        //Clicking start button assigns start and end times for each modes depending on whether
        //measuring was already started before, locks start button and starts the timer
        startButton.addActionListener((event) ->
        {
            if (!mode)
            {
                if (endStopwatchTime == 0)
                {
                    startStopwatchTime = System.currentTimeMillis();
                }
                else
                {
                    endStopwatchTime = System.currentTimeMillis();
                    startStopwatchTime = endStopwatchTime - stopwatchCurrentValue;
                }
                timer.start();
                startButton.setEnabled(false);
                stopButton.setEnabled(true);
                countdownMenu.setEnabled(false);
            }
        });
        //Clicking stop button stops the current mode timer and locks current value on textField
        stopButton.addActionListener((event) ->
        {
            if (!mode)
            {
                timer.stop();
                stopwatchCurrentValue = endStopwatchTime - startStopwatchTime;
                var seconds = stopwatchCurrentValue / 1000;
                HH = seconds / 3600;
                MM = seconds / 60 % 60;
                SS = seconds % 60;
                sss = stopwatchCurrentValue % 1000;
                textField.setText(String.format("%02d:%02d:%02d.%03d", HH, MM, SS, sss));
                startButton.setEnabled(true);
                stopButton.setEnabled(false);
                countdownMenu.setEnabled(true);
            }
        });
        //Clicking stop button stops the timer and resets all time values for current mode
        resetButton.addActionListener((event) ->
        {
            if (!mode)
            {
                timer.stop();
                stopwatchCurrentValue = 0;
                startStopwatchTime = 0;
                endStopwatchTime = 0;
                HH = 0;
                MM = 0;
                SS = 0;
                sss = 0;
                textField.setText(String.format("%02d:%02d:%02d.%03d", HH, MM, SS, sss));
            }
            startButton.setEnabled(true);
            stopButton.setEnabled(true);
        });
    }
}
