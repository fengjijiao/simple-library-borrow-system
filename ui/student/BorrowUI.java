package test7.ui.student;

import test7.DBOP;
import test7.view.JTableUnEdited;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class BorrowUI extends JFrame implements ActionListener {
    private int wWidth = 606, wHeight = 600;
    private JPanel jp, jp1, jp2;
    private JLabel jl, jl1, jl2 ,jl3, jl4, jl5, jl6;
    private JTextField jt, jt1, jt2, jt3, jt4;
    private JButton jb, jb1, jb2, jb3;
    private JTableUnEdited jTableUnEdited;
    private DefaultTableModel defaultTableModel;
    private Object[] columTitle;
    private Object[][] bookSet;
    private JScrollPane jScrollPane;
    private JComboBox jc;
    private int selectedId, selectedIndex;
    private int page = 1;
    private int perPageNum = 20;
    private int[] interval;

    public BorrowUI() {
        setTitle("借阅");
        setLocation(400, 100);
        setSize(wWidth, wHeight);
        setLayout(null);
        setResizable(false);
        //
        jp = new JPanel();
        jp.setBounds(0,0, wWidth, (int) (wHeight*0.1));
        jp.setLayout(new FlowLayout());
        jl = new JLabel("作者");
        jp.add(jl);
        jt = new JTextField(10);
        jp.add(jt);
        jl1 = new JLabel("书名");
        jp.add(jl1);
        jt1 = new JTextField(10);
        jp.add(jt1);
        jl2 = new JLabel("ISBN");
        jp.add(jl2);
        jt2 = new JTextField(10);
        jp.add(jt2);
        jl3 = new JLabel("出版者");
        jp.add(jl3);
        jt3 = new JTextField(10);
        jp.add(jt3);
        jb = new JButton("查询");
        jb.addActionListener(this);
        jp.add(jb);
        jb1 = new JButton("重置");
        jb1.addActionListener(this);
        jp.add(jb1);
        add(jp);
        //
        jp1 = new JPanel();
        jp1.setBounds(0, (int) (wHeight*0.1), wWidth, (int) (wHeight*0.1));
        jp1.setBackground(Color.CYAN);
        jp1.setLayout(new FlowLayout());
        jl4 = new JLabel("未选中");
        jp1.add(jl4);
        jb2 = new JButton("借阅");
        jp1.add(jb2);
        add(jp1);
        //
        jScrollPane = new JScrollPane();
        jScrollPane.setBounds(0, (int) (wHeight*0.2), wWidth, (int) (wHeight*0.6));
        interval = calcPageInterval();
        defaultTableModel = new DefaultTableModel();
        bookSet = DBOP.getAllBook(interval[0], interval[1]);
        assert bookSet != null;
        columTitle = new Object[]{"序号", "ISBN", "书名", "作者", "出版者", "出版日期", "库存", "已借"};
        defaultTableModel.setDataVector(bookSet, columTitle);
        jTableUnEdited = new JTableUnEdited(defaultTableModel);
        jTableUnEdited.setBounds(0, (int) (wHeight*0.2), wWidth, (int) (wHeight*0.6));
        jTableUnEdited.addRowSelectListener(new JTableUnEdited.RowSelect() {
            @Override
            public void SelectChanged(int row) {
                selectedId = calcIndexSelectBook(row);
                selectedIndex = row;
                jl4.setText("已选中：" + bookSet[row][2]);
            }
        });
        jScrollPane.setRowHeaderView(jTableUnEdited.getTableHeader());
        jScrollPane.setViewportView(jTableUnEdited);
        add(jScrollPane);
        //
        jp2 = new JPanel();
        jp2.setBounds(0, (int) (wHeight*0.8), wWidth, (int) (wHeight*0.1));
        jp2.setBackground(Color.RED);
        int total = DBOP.getAllBookTotal();
        jl5 = new JLabel("共"+total+"条, 跳转到 ");
        jp2.add(jl5);
        jc = new JComboBox();
        jc.setLayout(new FlowLayout());
        for(int i=0;i<calcPageAmount(total);i++) {
            jc.addItem(i+1);
        }
        jc.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                int newPage = (int) e.getItem();
                if(newPage != page) {
                    page = newPage;
                    interval = calcPageInterval();
                    bookSet = DBOP.getAllBook(interval[0], interval[1]);
                    assert bookSet != null;
                    defaultTableModel.setDataVector(bookSet, columTitle);
                }
            }
        });
        jp2.add(jc);
        jl6 = new JLabel("页");
        jp2.add(jl6);
        add(jp2);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("查询")) {
            //
        }else if(e.getActionCommand().equals("重置")) {
            jt.setText("");
            jt1.setText("");
            jt2.setText("");
            jt3.setText("");
        }
    }

    private int calcIndexSelectBook(int row) {
        return (int) bookSet[row][0];
    }

    private int calcPageAmount(int total) {
        return  (int) Math.ceil(total/(perPageNum*1.0));
    }

    private int[] calcPageInterval() {
        int[] interval = new int[2];
        interval[0] = (page>1)?(page-1)*perPageNum:0;
        interval[1] = (page>1)?page*perPageNum:perPageNum;
        return interval;
    }
}
