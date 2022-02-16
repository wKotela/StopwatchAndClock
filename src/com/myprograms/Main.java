//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.myprograms;

import javax.swing.*;
import java.awt.EventQueue;
import java.text.ParseException;

public class Main
{
    public static void main(String[] args) throws ParseException
    {
        EventQueue.invokeLater(() ->
        {
            try
            {
                var frame = new StopwatchFrame();
                frame.setVisible(true);
                frame.setEnabled(true);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            }
            catch (ParseException e)
            {
                e.printStackTrace();
            }
        });
    }
}
