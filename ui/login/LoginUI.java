package test7.ui.login;

import test7.DBOP;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginUI extends JFrame implements ActionListener {
    private JLabel jl, jl1;
    private JTextField jf;
    private JPasswordField jf1;
    private ButtonGroup bg;
    private JRadioButton jr, jr1;
    private JButton jb;
    private CallBack mCallBack;

    public interface CallBack {
        void LoginSuccess();

        void LoginFailure(String msg);
    }

    public LoginUI(CallBack mCallBack) {
        this.mCallBack = mCallBack;
        setTitle("登录");
        setLocation(500, 300);
        setSize(260, 220);
        setLayout(null);
        setResizable(false);
        Container container = getContentPane();
        jl = new JLabel("用户名");
        jl.setBounds(10, 10, 100, 20);
        container.add(jl);
        jf = new JTextField();
        jf.setBounds(10, 40, 150, 30);
        container.add(jf);
        jl1 = new JLabel("密码");
        jl1.setBounds(10, 80, 100, 20);
        container.add(jl1);
        jf1 = new JPasswordField();
        jf1.setBounds(10, 110, 150, 30);
        container.add(jf1);
        bg = new ButtonGroup();
        jr = new JRadioButton("学生", true);
        jr.setBounds(10,150,50,20);
        bg.add(jr);
        container.add(jr);
        jr1 = new JRadioButton("教师");
        jr1.setBounds(70,150,50,20);
        bg.add(jr1);
        container.add(jr1);
        jb = new JButton("登录");
        jb.setBounds(170, 40, 60, 100);
        jb.addActionListener(this);
        container.add(jb);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("登录")) {
            String userName = jf.getText();
            if(userName.equals("")) {
                JOptionPane.showMessageDialog(this, "用户名不能为空！", "提示", JOptionPane.ERROR_MESSAGE);
                return;
            }
            String passWord = String.valueOf(jf1.getPassword());
            if(passWord.equals("")) {
                JOptionPane.showMessageDialog(this, "密码不能为空！", "提示", JOptionPane.ERROR_MESSAGE);
                return;
            }
            boolean isStudent = jr.isSelected();
            if (DBOP.toLogin(isStudent, userName, passWord)) {
                JOptionPane.showMessageDialog(this, "欢迎你，" + DBOP.getNickName() + "!", "提示", JOptionPane.PLAIN_MESSAGE);
                if (mCallBack != null) mCallBack.LoginSuccess();
            } else {
                JOptionPane.showMessageDialog(this, "密码错误！", "提示", JOptionPane.ERROR_MESSAGE);
                if (mCallBack != null) mCallBack.LoginFailure("密码错误！");
            }
        }
    }
}