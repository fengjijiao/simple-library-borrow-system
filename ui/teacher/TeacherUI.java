package test7.ui.teacher;

import test7.ui.student.LibraryUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class TeacherUI extends JFrame implements ActionListener {
    private JButton jb, jb1, jb2;
    private LibraryUI libraryUI;
    private BorrowUI borrowUI;
    private static ArrayList<JFrame> jFrameHeap = new ArrayList<>();

    public TeacherUI() {
        setTitle("教师面板");
        setLocation(500, 300);
        setSize(260, 220);
        setLayout(new GridLayout(1,3));
        setResizable(false);
        jb = new JButton("图书管理");
        jb.addActionListener(this);
        add(jb);
        jb1 = new JButton("借阅管理");
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
        if(e.getActionCommand().equals("图书管理")) {
            libraryUI = new LibraryUI();
        }else if(e.getActionCommand().equals("借阅管理")) {
            borrowUI = new BorrowUI();
        }else if(e.getActionCommand().equals("退出")) {
            dispose();
            if(libraryUI != null) libraryUI.dispose();
            if(borrowUI != null) borrowUI.dispose();
            for(JFrame jf : jFrameHeap) {
                if(jf != null) jf.dispose();
            }
        }
    }

    public void refreshLibraryUIBookSet() {
        if(libraryUI != null && libraryUI.isVisible()) libraryUI.showNewBookSetForSearch();
    }

    public void refreshBorrowUIBookSet() {
        if(borrowUI != null && borrowUI.isVisible()) borrowUI.showNewBorrowSetForSearch();
    }

    public static void addJFrameToHeap(JFrame jFrame) {
        jFrameHeap.add(jFrame);
    }
}
