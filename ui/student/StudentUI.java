package test7.ui.student;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class StudentUI extends JFrame implements ActionListener {
    private JButton jb, jb1, jb2;
    private LibraryUI libraryUI;
    private ReMandUI reMandUI;
    private static ArrayList<JFrame> jFrameHeap = new ArrayList<>();

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
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("借阅")) {
            libraryUI = new LibraryUI();
        }else if(e.getActionCommand().equals("归还")) {
            reMandUI = new ReMandUI();
        }else if(e.getActionCommand().equals("退出")) {
            dispose();
            if(libraryUI != null) libraryUI.dispose();
            if(reMandUI != null) reMandUI.dispose();
            for(JFrame jf: jFrameHeap) {
                if(jf != null) jf.dispose();
            }
        }
    }

    public void refreshLibraryUIBookSet() {
        if(libraryUI != null && libraryUI.isVisible()) libraryUI.showNewBookSetForSearch();
    }

    public static void addJFrameToHeap(JFrame jFrame) {
        jFrameHeap.add(jFrame);
    }
}
