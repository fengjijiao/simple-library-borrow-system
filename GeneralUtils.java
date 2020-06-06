package test7;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GeneralUtils {
    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if( !isNum.matches() ){
            return false;
        }
        return true;
    }

    public static boolean isDateString(String str) {
        Pattern pattern = Pattern.compile("[0-9]{4}-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])");
        Matcher isNum = pattern.matcher(str);
        if( !isNum.matches() ){
            return false;
        }
        return true;
    }

    public static String getBorrowStatus(int status) {
        if(status == 0) {
            return "借阅中";
        }else if(status == 1) {
            return "已归还";
        }else {
            return "未知";
        }
    }
}
