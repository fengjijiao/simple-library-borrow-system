package test7.ui.teacher;

import test7.DBOP;
import test7.GeneralUtils;
import test7.Main;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

public class EditBorrowInfoUI extends JFrame implements ActionListener {
    private int wWidth = 400, wHeight = 400;
    private JPanel jp;
    private JLabel jl;
    private JButton jb;
    private ButtonGroup bg;
    private JRadioButton jrb, jrb1, jrb2;
    private final int brid;
    private HashMap<String, Object> hashMap;

    public EditBorrowInfoUI(int brid) {
        setTitle("修改");
        setLocation(500, 130);
        setSize(wWidth, wHeight);
        setLayout(null);
        setResizable(false);
        setDefaultCloseOperation(HIDE_ON_CLOSE);
        this.brid = brid;
        hashMap = DBOP.getOneBorrow(brid);
        jp = new JPanel();
        jp.setLayout(null);
        jp.setBounds(10,0, (int) (wWidth*0.9), (int) (wHeight*0.9));
        jl = new JLabel("状态");
        jl.setBounds(10,10, (int) (wWidth*0.9), 30);
        jp.add(jl);
        bg = new ButtonGroup();
        jrb = new JRadioButton("借阅中");
        jrb.setBounds(10,40, (int) (wWidth*0.3), 30);
        jp.add(jrb);
        bg.add(jrb);
        jrb1 = new JRadioButton("已归还");
        jrb1.setBounds(10 + (int) (wWidth*0.3),40, (int) (wWidth*0.3), 30);
        jp.add(jrb1);
        bg.add(jrb1);
        jrb2 = new JRadioButton("未知");
        jrb2.setBounds(10 + (int) (wWidth*0.3)*2,40,(int) (wWidth*0.3), 30);
        jp.add(jrb2);
        bg.add(jrb2);
        jb = new JButton("保存");
        jb.setBounds(10,10 + 140+(int)(wHeight*0.3),60,30);
        jb.addActionListener(this);
        jp.add(jb);
        add(jp);
        refreshSelectedStatus();
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("保存")) {
            int newStatus = getNewStatus();
            if(newStatus == -1) {
                JOptionPane.showConfirmDialog(this, "状态未选择！", "提示", JOptionPane.DEFAULT_OPTION);
            }else {
                DBOP.updateBorrowStatus(brid, newStatus);
                int confirm = JOptionPane.showConfirmDialog(this, "修改成功！", "提示", JOptionPane.DEFAULT_OPTION);
                if(confirm == 0) {
                    dispose();
                    Main.refreshBorrowUIBookSet();
                }
            }
        }else if(e.getActionCommand().equals("重置")) {
            refreshSelectedStatus();
        }
    }

    private int getNewStatus() {
        if(jrb.isSelected()) return 0;
        else if(jrb1.isSelected()) return 1;
        else if(jrb2.isSelected()) return 2;
        else return -1;
    }

    private void refreshSelectedStatus() {
        int status = (int) hashMap.get("status");
        if(status == 0) jrb.setSelected(true);
        else if(status == 1) jrb1.setSelected(true);
        else if(status == 2) jrb2.setSelected(true);
    }
}
