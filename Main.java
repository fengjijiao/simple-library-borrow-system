package test7;

import test7.ui.login.LoginUI;
import test7.ui.student.StudentUI;
import test7.ui.teacher.TeacherUI;

import javax.swing.*;

public class Main {
    private static LoginUI loginUI;
    private static StudentUI studentUI;
    private static TeacherUI teacherUI;
    public static void main(String[] args) {
        DBOP.init();
        teacherUI = new TeacherUI();
        /*loginUI = new LoginUI(new LoginUI.CallBack() {
            @Override
            public void LoginSuccess() {
                System.out.println("登录成功!");
                loginUI.dispose();
                if(DBOP.isStudent()) {
                    studentUI = new StudentUI();
                }else {
                    teacherUI = new TeacherUI();
                }
            }

            @Override
            public void LoginFailure(String msg) {
                System.out.println("登录失败!");
            }
        });*/
    }

    public static void addJFrameToHeap(JFrame jFrame) {
        if(DBOP.isStudent()) {
            StudentUI.addJFrameToHeap(jFrame);
        }else {
            TeacherUI.addJFrameToHeap(jFrame);
        }
    }

    public static void refreshLibraryUIBookSet() {
        if(DBOP.isStudent()) {
            studentUI.refreshLibraryUIBookSet();
        }else {
            teacherUI.refreshLibraryUIBookSet();
        }
    }
}
