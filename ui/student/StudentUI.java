package test7.ui.student;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StudentUI extends JFrame implements ActionListener {
    private JButton jb, jb1, jb2;
    private BorrowUI borrowUI;

    public StudentUI() {
        setTitle("学生面板");
        setLocation(500, 300);
        setSize(260, 220);
        setLayout(new GridLayout(1,3));
        setResizable(false);
        jb = new JButton("借阅");
        jb.addActionListener(this);
        add(jb);
        jb1 = new JButton("归还");
        jb1.addActionListener(this);
        add(jb1);
        jb2 = new JButton("退出");
        jb2.addActionListener(this);
        add(jb2);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("借阅")) {
            borrowUI = new BorrowUI();
        }else if(e.getActionCommand().equals("归还")) {
            //
        }else if(e.getActionCommand().equals("退出")) {
            dispose();
            borrowUI.dispose();
        }
    }
}
