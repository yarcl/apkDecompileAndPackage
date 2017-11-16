package com;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by Administrator on 2017/11/16.
 */
public class PropertiesPanelBak extends JFrame{
    final String[] strs = new String[6];

    public PropertiesPanelBak(){
        this.setTitle("配置相关参数");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setBounds(100, 100, 500, 450);

        JPanel contentPane=new JPanel();
        contentPane.setBorder(new EmptyBorder(5,5,5,5));
        this.setContentPane(contentPane);
        contentPane.setLayout(new GridLayout(7,1,5,5));

        JPanel pane1=new JPanel();
        contentPane.add(pane1);

        JPanel pane2=new JPanel();
        contentPane.add(pane2);

        JPanel pane3=new JPanel();
        contentPane.add(pane3);

        JPanel pane4=new JPanel();
        contentPane.add(pane4);

        JPanel pane5=new JPanel();
        contentPane.add(pane5);

        JPanel pane6=new JPanel();
        contentPane.add(pane6);

        JPanel pane7=new JPanel();
        contentPane.add(pane7);

        JLabel label1=new JLabel("apkPath：");

        final JTextField textField1=new JTextField();
        textField1.setColumns(10);
        pane1.add(label1);
        pane1.add(textField1);

        JLabel label2=new JLabel("apkName：");
        final JTextField textField2=new JTextField();
        textField2.setColumns(10);
        pane2.add(label2);
        pane2.add(textField2);

        JLabel label3=new JLabel("packageSdk：");

        final JTextField textField3=new JTextField();
        textField3.setColumns(10);
        pane3.add(label3);
        pane3.add(textField3);

        JLabel label4=new JLabel("copyPath：");
        final JTextField textField4=new JTextField();
        textField4.setColumns(10);
        pane4.add(label4);
        pane4.add(textField4);

        JLabel label5=new JLabel("cmdFile：");
        final JTextField textField5=new JTextField();
        textField5.setColumns(10);
        pane5.add(label5);
        pane5.add(textField5);

        JLabel label6=new JLabel("insertStr：");
        final JTextField textField6=new JTextField();
        textField6.setColumns(10);
        pane6.add(label6);
        pane6.add(textField6);

        JButton button1 = new JButton("获取配置");
        pane7.add(button1);
        button1.addMouseListener(new MouseAdapter()
        {
            public void mouseClicked(MouseEvent e)
            {
                strs[0] = textField1.getText();
                strs[1] = textField2.getText();
                strs[2] = textField3.getText();
                strs[3] = textField4.getText();
                strs[4] = textField5.getText();
                strs[5] = textField6.getText();

                for(String str : strs){
                    System.out.println("输入的内容为："+str);
                }

            }
        });

        contentPane.setVisible(true);
    }
    public static void main(String[]args){
        PropertiesPanel example=new PropertiesPanel();

        System.out.println("hello");
    }


}
