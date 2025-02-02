package test7.ui.student;

import test7.DBOP;
import test7.Main;
import test7.ui.teacher.AddBookUI;
import test7.ui.teacher.EditBookInfoUI;
import test7.view.JTableUnEdited;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class LibraryUI extends JFrame implements ActionListener {
    private int wWidth = 606, wHeight = 600;
    private JPanel jp, jp1, jp2, jp3;
    private JLabel jl, jl1, jl2 ,jl3, jl4, jl5, jl6;
    private JTextField jt, jt1, jt2, jt3, jt4;
    private JButton jb, jb1, jb2, jb3, jb4;
    private JTableUnEdited jTableUnEdited;
    private DefaultTableModel defaultTableModel;
    private Object[] columTitle;
    private Object[][] bookSet;
    private JScrollPane jScrollPane;
    private JComboBox<Integer> integerJComboBox;
    private int selectedId, selectedIndex;
    private int page = 1;
    private int perPageNum = 20;
    private int[] interval;
    private EditBookInfoUI editBookInfoUI;
    private AddBookUI addBookUI;
    private String searchedName = "", searchedISBN = "",  searchedAuthor = "", searchedPublisher = "";

    public LibraryUI() {
        if(DBOP.isStudent()) {
            setTitle("借阅");
        }else {
            setTitle("图书管理");
        }
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
        jp1.setBounds(0, (int) (wHeight*0.1), wWidth, (int) (wHeight*0.06));
        //jp1.setBackground(Color.CYAN);
        jp1.setLayout(new FlowLayout(FlowLayout.LEADING));
        if(DBOP.isStudent()) {
            jb2 = new JButton("借阅");
            jb2.setEnabled(false);
            jb2.addActionListener(this);
            jp1.add(jb2);
        }else {
            jb2 = new JButton("删除");
            jb2.setEnabled(false);
            jb2.addActionListener(this);
            jp1.add(jb2);
            jb3 = new JButton("添加");
            jb3.addActionListener(this);
            jp1.add(jb3);
            jb4 = new JButton("修改");
            jb4.setEnabled(false);
            jb4.addActionListener(this);
            jp1.add(jb4);
        }
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
        bookSet = DBOP.getAllBook(interval[0], interval[1]);
        assert bookSet != null;
        columTitle = new Object[]{"序号", "ISBN", "书名", "作者", "出版者", "出版日期", "库存", "已借"};
        defaultTableModel.setDataVector(bookSet, columTitle);
        jTableUnEdited = new JTableUnEdited(defaultTableModel);
        jTableUnEdited.setSelectionMode(0);//单选0,连续多选1,随意多选2
        jTableUnEdited.setBounds(0, (int) (wHeight*0.2), wWidth, (int) (wHeight*0.6));
        jTableUnEdited.addRowSelectListener(new JTableUnEdited.RowSelect() {
            @Override
            public void SelectChanged(int row) {
                selectedId = calcIndexSelectBook(row);
                selectedIndex = row;
                jl4.setText("已选中：" + bookSet[row][2]);
                jb2.setEnabled(true);//借阅 or 删除
                if(jb4 != null) jb4.setEnabled(true);//修改
            }
        });
        jScrollPane.setRowHeaderView(jTableUnEdited.getTableHeader());
        jScrollPane.setViewportView(jTableUnEdited);
        add(jScrollPane);
        //
        jp2 = new JPanel();
        jp2.setLayout(new FlowLayout(FlowLayout.LEADING));
        jp2.setBounds(0, (int) (wHeight*0.8), wWidth, (int) (wHeight*0.1));
        //jp2.setBackground(Color.RED);
        int total = DBOP.getAllBookTotal();
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
                    refreshBookTable();
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
                searchedAuthor = author;
                searchedName = name;
                searchedISBN = isbn;
                searchedPublisher = publisher;
                page = 1;
                interval = calcPageInterval();
                showNewBookSetForSearch(isbn,name,author,publisher);
                break;
            case "重置":
                jt.setText("");
                jt1.setText("");
                jt2.setText("");
                jt3.setText("");
                break;
            case "借阅":
                DBOP.addBorrow(DBOP.getUID(), selectedId);
                DBOP.updateBookBorrowed(selectedId, 1);
                JOptionPane.showMessageDialog(this, "已成功借阅\"" + bookSet[selectedIndex][2] + "\"！", "提示", JOptionPane.PLAIN_MESSAGE);
                showNewBookSetForSearch();
                break;
            case "删除":
                int confirm = JOptionPane.showConfirmDialog(this, "确认删除\""+ bookSet[selectedIndex][2] +"\"？", "提示", JOptionPane.DEFAULT_OPTION);
                if(confirm == 0) {//确认
                    DBOP.deleteBook(selectedId);
                    //refreshBookTable();
                    showNewBookSetForSearch();
                }
                break;
            case "添加":
                if(addBookUI == null || !addBookUI.isVisible()) {
                    addBookUI = new AddBookUI();
                    Main.addJFrameToHeap(addBookUI);
                }else {
                    JOptionPane.showMessageDialog(this, "已经打开了一个添加窗口！", "提示", JOptionPane.PLAIN_MESSAGE);
                }
                break;
            case "修改":
                if(editBookInfoUI == null || !editBookInfoUI.isVisible()) {
                    editBookInfoUI = new EditBookInfoUI(selectedId);
                    Main.addJFrameToHeap(editBookInfoUI);
                }else {
                    JOptionPane.showMessageDialog(this, "已经打开了一个修改窗口！", "提示", JOptionPane.PLAIN_MESSAGE);
                }
                break;
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
        interval[1] = perPageNum;
        return interval;
    }

    private void refreshBookTable() {
        interval = calcPageInterval();
        bookSet = DBOP.getAllBook(interval[0], interval[1]);
        refreshBookTableDivBookSet(bookSet);
    }

    private void refreshBookTableDivBookSet(Object[][] bookSet) {
        assert bookSet != null;
        defaultTableModel.setDataVector(bookSet, columTitle);
    }

    private void refreshFooterForSearch(String isbn, String name, String author, String publisher) {
        int total = DBOP.getAllBookSearchTotal(isbn, name, author, publisher);
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
                    bookSet = DBOP.getAllBookSearch(isbn, name, author, publisher, interval[0], interval[1]);
                    refreshBookTableDivBookSet(bookSet);
                }
            }
        });
        jl6.setText("页");
    }

    public void showNewBookSetForSearch(String isbn, String name, String author, String publisher) {
        bookSet = DBOP.getAllBookSearch(isbn, name, author, publisher, interval[0], interval[1]);
        assert bookSet != null;
        refreshBookTableDivBookSet(bookSet);
        refreshFooterForSearch(isbn, name, author, publisher);
    }

    public void showNewBookSetForSearch() {
        showNewBookSetForSearch(searchedISBN,searchedName,searchedAuthor,searchedPublisher);
    }
}
