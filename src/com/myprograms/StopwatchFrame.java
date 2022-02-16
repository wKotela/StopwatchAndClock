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
    private JMenuItem stopperMenu;
    private JMenuItem counterMenu;
    private JMenuItem exitMenu;
    private boolean mode;
    private final Timer timer;
    private long startStopwatchTime;
    private long endStopwatchTime;
    private long startCounterTime;
    private long endCounterTime;
    private long stopwatchCurrentValue;
    private long counterCurrentValue;
    private long HH;
    private long MM;
    private long SS;
    private long sss;

    public StopwatchFrame() throws ParseException
    {
        Toolkit kit = Toolkit.getDefaultToolkit();
        int screenWidth = kit.getScreenSize().width;
        int screenHeight = kit.getScreenSize().height;
        setBounds((screenWidth - 640) / 2, (screenHeight - 480) / 2, 640, 480);

        setResizable(false);
        setTitle("Stoper");
        mode = false;

        createUI();

        addActionListeners();

        timer = new Timer(1, event ->
        {
            if (!mode) {
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

    public void createUI() throws ParseException
    {
        var bar = new JMenuBar();
        setJMenuBar(bar);

        var mainMenu = new JMenu("Menu");
        bar.add(mainMenu);

        stopperMenu = new JMenuItem("Stoper");
        mainMenu.add(stopperMenu);

        counterMenu = new JMenuItem("Minutnik");
        mainMenu.add(counterMenu);

        stopperMenu.setEnabled(false);
        exitMenu = new JMenuItem("Wyjście");
        mainMenu.add(exitMenu);

        var panel1 = new JPanel();
        var panel2 = new JPanel();
        panel1.setLayout(new BorderLayout());
        setLayout(new GridLayout(2, 1));

        MaskFormatter format = new MaskFormatter("##:##:##.###");
        textField = new JFormattedTextField(format);
        textField.setHorizontalAlignment(0);
        HH = 0;
        MM = 0;
        SS = 0;
        sss = 0;
        String time = String.format("%02d:%02d:%02d.%03d", HH, MM, SS, sss);
        textField.setText(time);
        textField.setFont(new Font("Lucida Sans Console", 1, 42));

        panel1.add(textField, "Center");
        add(panel1);
        panel2.setLayout(new GridLayout(1, 3));

        startButton = new JButton("START");
        startButton.setFont(new Font("Lucida Sans Console", 0, 28));
        panel2.add(startButton);

        stopButton = new JButton("STOP");
        stopButton.setFont(new Font("Lucida Sans Console", 0, 28));
        panel2.add(stopButton);

        resetButton = new JButton("RESET");
        resetButton.setFont(new Font("Lucida Sans Console", 0, 28));
        panel2.add(resetButton);

        add(panel2);

        textField.setEditable(false);
    }

    public void addActionListeners()
    {
        stopperMenu.addActionListener((event) ->
        {
            mode = false;
            stopperMenu.setEnabled(false);
            counterMenu.setEnabled(true);
            textField.setEditable(false);
            setTitle("Stoper");
            var seconds = stopwatchCurrentValue / 1000;
            HH = seconds / 3600;
            MM = seconds / 60 % 60;
            SS = seconds % 60;
            sss = stopwatchCurrentValue % 1000;
            textField.setText(String.format("%02d:%02d:%02d.%03d", HH, MM, SS, sss));
        });

        counterMenu.addActionListener((event) ->
        {
            mode = true;
            stopperMenu.setEnabled(true);
            counterMenu.setEnabled(false);
            textField.setEditable(true);
            setTitle("Minutnik");
        });

        exitMenu.addActionListener((event) ->
        {
            System.exit(0);
        });

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
                counterMenu.setEnabled(false);
            }
        });

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
                counterMenu.setEnabled(true);
            }
        });

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
