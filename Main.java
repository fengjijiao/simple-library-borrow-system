package test7;

import test7.ui.login.LoginUI;
import test7.ui.student.StudentUI;
import test7.ui.teacher.TeacherUI;

public class Main {
    private static LoginUI loginUI;
    private static StudentUI studentUI;
    private static TeacherUI teacherUI;
    public static void main(String[] args) {
        DBOP.init();
        studentUI = new StudentUI();
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
}
