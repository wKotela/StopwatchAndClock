package com.myprograms;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.text.ParseException;

public class StopwatchFrame extends JFrame
{
    public final int DEFAULT_WIDTH = 640;
    public final int DEFAULT_HEIGHT = 480;
    //countdownValueField for later use
    private JFormattedTextField timeField, countdownValueField;
    private JButton startButton;
    private JButton resetButton;
    private JButton stopButton;
    private JMenuItem stopwatchMenu;
    private JMenuItem countdownMenu;
    private JMenuItem exitMenu;
    private JPanel panel1;
    //mode: false - means stopwatch mode, true - means countdown mode
    private boolean mode;
    private boolean isTimeFieldFocused;
    private final Timer timer;
    private long startStopwatchTime;
    private long endStopwatchTime;
    private long endCountdownTime;
    private long stopwatchCurrentValue;
    private long countdownCurrentValue;
    private long countdownSetValue;
    private long stopwatchHourDisplay, stopwatchMinuteDisplay, stopwatchSecondDisplay, stopwatchMillisecondDisplay;
    private long countdownHourDisplay, countdownMinuteDisplay, countdownSecondDisplay, countdownMillisecondDisplay;

    public StopwatchFrame() throws ParseException, InterruptedException
    {
        //Setting constant size window at the screen center
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

        //Setting initial countdown time to 5 minutes
        countdownSetValue = 300000;
        countdownCurrentValue = countdownSetValue;

        //Timer responsible for refreshing the time at the main timeField
        timer = new Timer(1, event ->
        {
            if (!mode)
            {
                endStopwatchTime = System.currentTimeMillis();
                stopwatchCurrentValue = endStopwatchTime - startStopwatchTime;
                var seconds = stopwatchCurrentValue / 1000;
                stopwatchHourDisplay = seconds / 3600;
                stopwatchMinuteDisplay = seconds / 60 % 60;
                stopwatchSecondDisplay = seconds % 60;
                stopwatchMillisecondDisplay = stopwatchCurrentValue % 1000;
                timeField.setText(String.format("%02d:%02d:%02d.%03d", stopwatchHourDisplay, stopwatchMinuteDisplay, stopwatchSecondDisplay, stopwatchMillisecondDisplay));
            }
            else if(mode)
            {
                countdownCurrentValue = endCountdownTime - System.currentTimeMillis();
                var seconds = countdownCurrentValue / 1000;
                countdownHourDisplay = seconds / 3600;
                countdownMinuteDisplay = seconds / 60 % 60;
                countdownSecondDisplay = seconds % 60;
                countdownMillisecondDisplay = countdownCurrentValue % 1000;
                timeField.setText(String.format("%02d:%02d:%02d.%03d", countdownHourDisplay, countdownMinuteDisplay, countdownSecondDisplay, countdownMillisecondDisplay));
                if(countdownCurrentValue <= 0)
                {
                    stopTimer();
                    stopwatchMenu.setEnabled(true);
                    startButton.setEnabled(true);
                    startButton.setText("RESTART");
                    countdownHourDisplay = 0;
                    countdownMinuteDisplay = 0;
                    countdownSecondDisplay = 0;
                    countdownMillisecondDisplay = 0;
                    timeField.setText(String.format("%02d:%02d:%02d.%03d", countdownHourDisplay, countdownMinuteDisplay, countdownSecondDisplay, countdownMillisecondDisplay));
                }
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

        panel1 = new JPanel();
        panel1.setLayout(new BorderLayout());

        var panel2 = new JPanel();
        setLayout(new GridLayout(2, 1));

        //Setting HH:MM:SS:msmsms format of the textField
        MaskFormatter format = new MaskFormatter("##:##:##.###");
        timeField = new JFormattedTextField(format);
        timeField.setHorizontalAlignment(0);
        stopwatchHourDisplay = 0;
        stopwatchMinuteDisplay = 0;
        stopwatchSecondDisplay = 0;
        stopwatchMillisecondDisplay = 0;

        //Creating string to display correct format
        String time = String.format("%02d:%02d:%02d.%03d", stopwatchHourDisplay, stopwatchMinuteDisplay, stopwatchSecondDisplay, stopwatchMillisecondDisplay);
        timeField.setText(time);
        timeField.setFont(new Font("Lucida Sans Console", Font.PLAIN, 42));
        panel1.add(timeField, BorderLayout.CENTER);

        //Additional text field to display fixed measure time for countdown mode
        format = new MaskFormatter("(##:##:##.###)");
        countdownValueField = new JFormattedTextField(format);
        countdownValueField.setHorizontalAlignment(0);
        countdownValueField.setFont(new Font("Lucida Sans Console", Font.PLAIN, 28));
        panel1.add(countdownValueField,BorderLayout.AFTER_LAST_LINE);
        countdownValueField.setVisible(false);

        //Creating display area by adding 2 JPanels with GridLayout
        add(panel1);
        panel2.setLayout(new GridLayout(1, 3));
        add(panel2);

        startButton = new JButton("START");
        startButton.setFont(new Font("Lucida Sans Console", Font.PLAIN, 28));
        panel2.add(startButton);

        stopButton = new JButton("STOP");
        stopButton.setFont(new Font("Lucida Sans Console", Font.PLAIN, 28));
        panel2.add(stopButton);
        stopButton.setEnabled(false);

        resetButton = new JButton("RESET");
        resetButton.setFont(new Font("Lucida Sans Console", Font.PLAIN, 28));
        panel2.add(resetButton);

        timeField.setEditable(false);
    }

    public void addActionListeners()
    {
        //Actions when Stopwatch is selected from main menu
        stopwatchMenu.addActionListener((event) ->
        {
            mode = false;
            stopButton.setEnabled(false);
            stopwatchMenu.setEnabled(false);
            countdownMenu.setEnabled(true);
            timeField.setEditable(false);
            setTitle("Stoper");
            startButton.setText("START");
            var seconds = stopwatchCurrentValue / 1000;
            stopwatchHourDisplay = seconds / 3600;
            stopwatchMinuteDisplay = seconds / 60 % 60;
            stopwatchSecondDisplay = seconds % 60;
            stopwatchMillisecondDisplay = stopwatchCurrentValue % 1000;
            timeField.setText(String.format("%02d:%02d:%02d.%03d", stopwatchHourDisplay, stopwatchMinuteDisplay, stopwatchSecondDisplay, stopwatchMillisecondDisplay));

        });
        //Actions when Countdown timer is selected from main menu
        countdownMenu.addActionListener((event) ->
        {
            mode = true;
            stopwatchMenu.setEnabled(true);
            countdownMenu.setEnabled(false);
            stopButton.setEnabled(false);
            if(countdownSetValue == countdownCurrentValue)
                timeField.setEditable(true);
            setTitle("Minutnik");
            if(countdownCurrentValue > 0)
            {
                var seconds = countdownCurrentValue / 1000;
                countdownHourDisplay = seconds / 3600;
                countdownMinuteDisplay = seconds / 60 % 60;
                countdownSecondDisplay = seconds % 60;
                countdownMillisecondDisplay = countdownCurrentValue % 1000;
            }
            else
            {
                var seconds = countdownSetValue / 1000;
                countdownHourDisplay = seconds / 3600;
                countdownMinuteDisplay = seconds / 60 % 60;
                countdownSecondDisplay = seconds % 60;
                countdownMillisecondDisplay = countdownSetValue % 1000;
            }
                timeField.setText(String.format("%02d:%02d:%02d.%03d", countdownHourDisplay, countdownMinuteDisplay, countdownSecondDisplay, countdownMillisecondDisplay));
        });

        timeField.addPropertyChangeListener(event->
        {
            if(mode && isTimeFieldFocused)
            {
                var temp = timeField.getText();
                if (!temp.equals("") && temp != null)
                {
                    var check = temp.toCharArray();
                    if (check[3] == '6' || check[3] == '7' || check[3] == '8' || check[3] == '9')
                        check[3] = '5';
                    if (check[6] == '6' || check[6] == '7' || check[6] == '8' || check[6] == '9')
                        check[6] = '5';
                    temp = "";
                    for (int i = 0; i < check.length; i++)
                        temp += check[i];
                    timeField.setText(temp);
                    var countdownHours = Integer.valueOf(temp.substring(0, 2));
                    var countdownMinutes = Integer.valueOf(temp.substring(3, 5));
                    var countdownSeconds = Integer.valueOf(temp.substring(6, 8));
                    var countdownMilliseconds = Integer.valueOf(temp.substring(9, 12));
                    countdownSetValue = countdownHours * 3600000 + countdownMinutes * 60000 + countdownSeconds * 1000 + countdownMilliseconds;
                    countdownCurrentValue = countdownSetValue;
                }
            }
        });

        timeField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e)
            {
                isTimeFieldFocused = true;
            }

            @Override
            public void focusLost(FocusEvent e)
            {
                isTimeFieldFocused = false;
            }
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
            else if(mode && countdownSetValue > 0)
            {
                if(countdownCurrentValue > 0)
                {
                    endCountdownTime = System.currentTimeMillis() + countdownCurrentValue;
                }
                else
                {
                    countdownCurrentValue = countdownSetValue;
                    endCountdownTime = System.currentTimeMillis() + countdownSetValue;
                    startButton.setText("START");
                }
                timer.start();
                timeField.setEditable(false);
                startButton.setEnabled(false);
                stopButton.setEnabled(true);
                stopwatchMenu.setEnabled(false);
            }
        });
        //Clicking stop button stops the current mode timer and locks current value on textField
        stopButton.addActionListener((event) ->
        {
            if (!mode && stopwatchCurrentValue != 0)
            {
                timer.stop();
                stopwatchCurrentValue = endStopwatchTime - startStopwatchTime;
                var seconds = stopwatchCurrentValue / 1000;
                stopwatchHourDisplay = seconds / 3600;
                stopwatchMinuteDisplay = seconds / 60 % 60;
                stopwatchSecondDisplay = seconds % 60;
                stopwatchMillisecondDisplay = stopwatchCurrentValue % 1000;
                timeField.setText(String.format("%02d:%02d:%02d.%03d", stopwatchHourDisplay, stopwatchMinuteDisplay, stopwatchSecondDisplay, stopwatchMillisecondDisplay));
                startButton.setEnabled(true);
                stopButton.setEnabled(false);
                countdownMenu.setEnabled(true);
            }
            if(mode && countdownCurrentValue != 0)
            {
                timer.stop();
                countdownCurrentValue = endCountdownTime - System.currentTimeMillis();
                var seconds = countdownCurrentValue / 1000;
                countdownHourDisplay = seconds / 3600;
                countdownMinuteDisplay = seconds / 60 % 60;
                countdownSecondDisplay = seconds % 60;
                countdownMillisecondDisplay = countdownCurrentValue % 1000;
                timeField.setText(String.format("%02d:%02d:%02d.%03d", countdownHourDisplay, countdownMinuteDisplay, countdownSecondDisplay, countdownMillisecondDisplay));
                startButton.setEnabled(true);
                stopButton.setEnabled(false);
                stopwatchMenu.setEnabled(true);
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
                stopwatchHourDisplay = 0;
                stopwatchMinuteDisplay = 0;
                stopwatchSecondDisplay = 0;
                stopwatchMillisecondDisplay = 0;
                timeField.setText(String.format("%02d:%02d:%02d.%03d", stopwatchHourDisplay, stopwatchMinuteDisplay, stopwatchSecondDisplay, stopwatchMillisecondDisplay));
                countdownMenu.setEnabled(true);
            }
            else if(mode)
            {
                timer.stop();
                countdownCurrentValue = countdownSetValue;
                endCountdownTime = 0;
                var seconds = countdownSetValue / 1000;
                countdownHourDisplay = seconds / 3600;
                countdownMinuteDisplay = seconds / 60 % 60;
                countdownSecondDisplay = seconds % 60;
                countdownMillisecondDisplay = countdownSetValue % 1000;
                timeField.setText(String.format("%02d:%02d:%02d.%03d", countdownHourDisplay, countdownMinuteDisplay, countdownSecondDisplay, countdownMillisecondDisplay));
                timeField.setEditable(true);
                stopwatchMenu.setEnabled(true);
            }
            startButton.setEnabled(true);
            startButton.setText("START");
            stopButton.setEnabled(false);
        });
    }
    public void stopTimer()
    {
        timer.stop();
    }
}
