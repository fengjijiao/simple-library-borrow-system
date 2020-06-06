package test7.ui.teacher;

import test7.DBOP;
import test7.Main;
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
    private JPanel jp, jp1, jp2, jp3;
    private JLabel jl, jl1, jl2 ,jl3, jl4, jl5, jl6, jl7;
    private JTextField jt, jt1, jt2, jt3, jt4;
    private JButton jb, jb1, jb2, jb3, jb4;
    private JTableUnEdited jTableUnEdited;
    private DefaultTableModel defaultTableModel;
    private Object[] columTitle;
    private Object[][] borrowSet;
    private JScrollPane jScrollPane;
    private JComboBox<Integer> integerJComboBox;
    private int selectedId, selectedIndex;
    private int page = 1;
    private int perPageNum = 20;
    private int[] interval;
    private EditBorrowInfoUI editBorrowInfoUI;
    private String searchedName = "", searchedISBN = "",  searchedAuthor = "", searchedPublisher = "", searchedNickname = "";

    public BorrowUI() {
        setTitle("借阅管理");
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
        jl7 = new JLabel("借阅者");
        jp.add(jl7);
        jt4 = new JTextField(10);
        jp.add(jt4);
        jb = new JButton("查询");
        jb.addActionListener(this);
        jp.add(jb);
        jb1 = new JButton("重置");
        jb1.addActionListener(this);
        jp.add(jb1);
        add(jp);
        //
        jp1 = new JPanel();
        jp1.setBounds(0, (int) (wHeight*0.1), wWidth, (int) (wHeight*0.06));
        jp1.setLayout(new FlowLayout(FlowLayout.LEADING));
        jb2 = new JButton("修改");
        jb2.setEnabled(false);
        jb2.addActionListener(this);
        jp1.add(jb2);
        add(jp1);
        //
        jp3 = new JPanel();
        jp3.setBounds(0, (int) (wHeight*0.15), wWidth, (int) (wHeight*0.04));
        jp3.setLayout(new FlowLayout(FlowLayout.LEADING));
        jl4 = new JLabel("未选中");
        jp3.add(jl4);
        add(jp3);
        //
        jScrollPane = new JScrollPane();
        jScrollPane.setBounds(0, (int) (wHeight*0.2), wWidth, (int) (wHeight*0.6));
        interval = calcPageInterval();
        defaultTableModel = new DefaultTableModel();
        borrowSet = DBOP.getAllBorrow(interval[0], interval[1]);
        assert borrowSet != null;
        columTitle = new Object[]{"序号", "用户", "书名", "开始时间", "结束时间", "状态"};
        defaultTableModel.setDataVector(borrowSet, columTitle);
        jTableUnEdited = new JTableUnEdited(defaultTableModel);
        jTableUnEdited.setSelectionMode(0);//单选0,连续多选1,随意多选2
        jTableUnEdited.setBounds(0, (int) (wHeight*0.2), wWidth, (int) (wHeight*0.6));
        jTableUnEdited.addRowSelectListener(new JTableUnEdited.RowSelect() {
            @Override
            public void SelectChanged(int row) {
                selectedId = calcIndexSelectBorrow(row);
                selectedIndex = row;
                jl4.setText("已选中：" + borrowSet[row][2]);
                if(jb2 != null) jb2.setEnabled(true);//修改
            }
        });
        jScrollPane.setRowHeaderView(jTableUnEdited.getTableHeader());
        jScrollPane.setViewportView(jTableUnEdited);
        add(jScrollPane);
        //
        jp2 = new JPanel();
        jp2.setLayout(new FlowLayout(FlowLayout.LEADING));
        jp2.setBounds(0, (int) (wHeight*0.8), wWidth, (int) (wHeight*0.1));
        int total = DBOP.getAllBorrowTotal();
        jl5 = new JLabel("共"+total+"条, 跳转到 ");
        jp2.add(jl5);
        integerJComboBox = new JComboBox<>();
        integerJComboBox.setLayout(new FlowLayout());
        for(int i=1;i<=calcPageAmount(total);i++) {
            integerJComboBox.addItem(i);
        }
        integerJComboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                int newPage = (int) e.getItem();
                if(newPage != page) {
                    page = newPage;
                    refreshBorrowTable();
                }
            }
        });
        jp2.add(integerJComboBox);
        jl6 = new JLabel("页");
        jp2.add(jl6);
        add(jp2);
        setDefaultCloseOperation(HIDE_ON_CLOSE);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "查询":
                String author = jt.getText();
                String name = jt1.getText();
                String isbn = jt2.getText();
                String publisher = jt3.getText();
                String nickname = jt4.getText();
                searchedAuthor = author;
                searchedName = name;
                searchedISBN = isbn;
                searchedPublisher = publisher;
                searchedNickname = nickname;
                page = 1;
                interval = calcPageInterval();
                showNewBorrowSetForSearch(isbn, name, author, publisher, nickname);
                break;
            case "重置":
                jt.setText("");
                jt1.setText("");
                jt2.setText("");
                jt3.setText("");
                jt4.setText("");
                break;
            case "修改":
                if(editBorrowInfoUI == null || !editBorrowInfoUI.isVisible()) {
                    editBorrowInfoUI = new EditBorrowInfoUI(selectedId);
                    Main.addJFrameToHeap(editBorrowInfoUI);
                }else {
                    JOptionPane.showMessageDialog(this, "已经打开了一个修改窗口！", "提示", JOptionPane.PLAIN_MESSAGE);
                }
                break;
        }
    }

    private int calcIndexSelectBorrow(int row) {
        return (int) borrowSet[row][0];
    }

    private int calcPageAmount(int total) {
        return  (int) Math.ceil(total/(perPageNum*1.0));
    }

    private int[] calcPageInterval() {
        int[] interval = new int[2];
        interval[0] = (page>1)?(page-1)*perPageNum:0;
        interval[1] = perPageNum;
        return interval;
    }

    private void refreshBorrowTable() {
        interval = calcPageInterval();
        borrowSet = DBOP.getAllBorrow(interval[0], interval[1]);
        refreshBorrowTableDivBorrowSet(borrowSet);
    }

    private void refreshBorrowTableDivBorrowSet(Object[][] borrowSet) {
        assert borrowSet != null;
        defaultTableModel.setDataVector(borrowSet, columTitle);
    }

    private void refreshFooterForSearch(String isbn, String name, String author, String publisher, String nickname) {
        int total = DBOP.getAllBorrowSearchTotal(isbn, name, author, publisher, nickname);
        jl5.setText("共"+total+"条, 跳转到 ");
        integerJComboBox.removeAllItems();
        for(int i=1;i<=calcPageAmount(total);i++) {
            integerJComboBox.addItem(i);
        }
        integerJComboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                int newPage = (int) e.getItem();
                if(newPage != page) {
                    page = newPage;
                    interval = calcPageInterval();
                    borrowSet = DBOP.getAllBorrowSearch(isbn, name, author, publisher, nickname, interval[0], interval[1]);
                    refreshBorrowTableDivBorrowSet(borrowSet);
                }
            }
        });
        jl6.setText("页");
    }

    public void showNewBorrowSetForSearch(String isbn, String name, String author, String publisher, String nickname) {
        borrowSet = DBOP.getAllBorrowSearch(isbn, name, author, publisher, nickname, interval[0], interval[1]);
        assert borrowSet != null;
        refreshBorrowTableDivBorrowSet(borrowSet);
        refreshFooterForSearch(isbn, name, author, publisher, nickname);
    }

    public void showNewBorrowSetForSearch() {
        showNewBorrowSetForSearch(searchedISBN,searchedName,searchedAuthor,searchedPublisher,searchedNickname);
    }
}