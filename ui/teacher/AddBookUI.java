package test7.ui.teacher;

import test7.DBOP;
import test7.Main;
import test7.GeneralUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddBookUI extends JFrame implements ActionListener {
    private int wWidth = 400, wHeight = 400;
    private JPanel jp;
    private JLabel jl, jl1, jl2, jl3, jl4, jl5, jl6;
    private JTextField jt, jt1, jt2, jt3, jt4, jt5, jt6;
    private JButton jb, jb1;
    private int textfieldColumns = 19;

    public AddBookUI() {
        setTitle("添加");
        setLocation(500, 130);
        setSize(wWidth, wHeight);
        setLayout(null);
        setResizable(false);
        setDefaultCloseOperation(HIDE_ON_CLOSE);
        jp = new JPanel();
        jp.setLayout(null);
        jp.setBounds(10,0, (int) (wWidth*0.9), (int) (wHeight*0.9));
        jl = new JLabel("书名");
        jl.setBounds(10,10, (int) (wWidth*0.9), 20);
        jp.add(jl);
        jt = new JTextField(textfieldColumns);
        jt.setBounds(10,10 + 20, (int) (wWidth*0.9), (int) (wHeight*0.05));
        jp.add(jt);
        jl1 = new JLabel("ISBN");
        jl1.setBounds(10, 10 + 20 +(int) (wHeight*0.05), (int) (wWidth*0.9), 20);
        jp.add(jl1);
        jt1 = new JTextField(textfieldColumns);
        jt1.setBounds(10,10 + 20*2 +(int) (wHeight*0.05), (int) (wWidth*0.9), (int) (wHeight*0.05));
        jp.add(jt1);
        jl2 = new JLabel("作者");
        jl2.setBounds(10,10 + 20*2 +(int) (wHeight*0.05)*2, (int) (wWidth*0.9), 20);
        jp.add(jl2);
        jt2 = new JTextField(textfieldColumns);
        jt2.setBounds(10,10 + 20*3 +(int) (wHeight*0.05)*2, (int) (wWidth*0.9), (int) (wHeight*0.05));
        jp.add(jt2);
        jl3 = new JLabel("出版者");
        jl3.setBounds(10,10 + 20*3 +(int) (wHeight*0.05)*3, (int) (wWidth*0.9), 20);
        jp.add(jl3);
        jt3 = new JTextField(textfieldColumns);
        jt3.setBounds(10,10 + 20*4 +(int) (wHeight*0.05)*3, (int) (wWidth*0.9), (int) (wHeight*0.05));
        jp.add(jt3);
        jl4 = new JLabel("出版日期(格式：2019-05-01)");
        jl4.setBounds(10,10 + 20*4 +(int) (wHeight*0.05)*4, (int) (wWidth*0.9), 20);
        jp.add(jl4);
        jt4 = new JTextField(textfieldColumns);
        jt4.setBounds(10,10 + 20*5 +(int) (wHeight*0.05)*4, (int) (wWidth*0.9), (int) (wHeight*0.05));
        jp.add(jt4);
        jl5 = new JLabel("库存");
        jl5.setBounds(10,10 + 20*5 +(int) (wHeight*0.05)*5, (int) (wWidth*0.9), 20);
        jp.add(jl5);
        jt5 = new JTextField(textfieldColumns);
        jt5.setBounds(10,10 + 20*6 +(int) (wHeight*0.05)*5, (int) (wWidth*0.9), (int) (wHeight*0.05));
        jp.add(jt5);
        jb = new JButton("添加");
        jb.setBounds(10,10 + 140+(int)(wHeight*0.3),60,30);
        jb.addActionListener(this);
        jp.add(jb);
        jb1 = new JButton("重置");
        jb1.setBounds(80,10 + 140+(int)(wHeight*0.3),60,30);
        jb1.addActionListener(this);
        jp.add(jb1);
        add(jp);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("添加")) {
            String name = jt.getText();
            String isbn = jt1.getText();
            String author = jt2.getText();
            String publisher = jt3.getText();
            String publicationdate = jt4.getText();
            String stockString = jt5.getText();
            if(name.equals("")) {
                JOptionPane.showConfirmDialog(this, "书名不能为空！", "提示", JOptionPane.DEFAULT_OPTION);
            }else if(isbn.equals("")) {
                JOptionPane.showConfirmDialog(this, "ISBN不能为空！", "提示", JOptionPane.DEFAULT_OPTION);
            }else if(author.equals("")) {
                JOptionPane.showConfirmDialog(this, "作者不能为空！", "提示", JOptionPane.DEFAULT_OPTION);
            }else if(publisher.equals("")) {
                JOptionPane.showConfirmDialog(this, "出版者不能为空！", "提示", JOptionPane.DEFAULT_OPTION);
            }else if(publicationdate.equals("")) {
                JOptionPane.showConfirmDialog(this, "出版日期不能为空！", "提示", JOptionPane.DEFAULT_OPTION);
            }else if(stockString.equals("")) {
                JOptionPane.showConfirmDialog(this, "库存不能为空！", "提示", JOptionPane.DEFAULT_OPTION);
            }else if(!GeneralUtils.isNumeric(stockString)) {
                JOptionPane.showConfirmDialog(this, "库存必须为数字！", "提示", JOptionPane.DEFAULT_OPTION);
            }else if(!GeneralUtils.isDateString(publicationdate)) {
                JOptionPane.showConfirmDialog(this, "出版日期格式不正确！", "提示", JOptionPane.DEFAULT_OPTION);
            }else if(DBOP.isExistsBook(isbn)) {
                JOptionPane.showConfirmDialog(this, "这本书已经存在了！", "提示", JOptionPane.DEFAULT_OPTION);
            }else {
                int stock = Integer.parseInt(stockString);
                DBOP.addBook(isbn,name,author,publisher,publicationdate,stock);
                int confirm = JOptionPane.showConfirmDialog(this, "添加成功！", "提示", JOptionPane.DEFAULT_OPTION);
                if(confirm == 0) {
                    dispose();
                    Main.refreshLibraryUIBookSet();
                }
            }
        }else if(e.getActionCommand().equals("重置")) {
            jt.setText("");
            jt1.setText("");
            jt2.setText("");
            jt3.setText("");
            jt4.setText("");
            jt5.setText("");
        }
    }
}
