package test7;

import java.sql.*;
import java.util.ArrayList;

public class DBOP {
    private static boolean studentFlag;
    private static String authkey;
    private static Connection connection;

    public static void init() {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\jijiao\\IdeaProjects\\test\\src\\test7\\db\\library.db");
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean toLogin(boolean isStudent, String userName, String passWord) {
        String sql = "SELECT * FROM `user` WHERE `username` = ? AND `password` = ? AND `type` = ? LIMIT 1;";
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setQueryTimeout(30);
            preparedStatement.setString(1, userName);
            preparedStatement.setString(2, passWord);
            preparedStatement.setInt(3, isStudent?0:1);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(!resultSet.next()) {
                return false;
            }else {
                authkey = resultSet.getString("authkey");
                studentFlag = isStudent;
                return true;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
    }

    public static Object[][] getAllBook(int start, int end) {
        String sql = "SELECT bid, isbn, name, author, publisher, publicationdate, stock, borrowed FROM `book` LIMIT ?,?;";
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setQueryTimeout(30);
            preparedStatement.setInt(1, start);
            preparedStatement.setInt(2, end);
            ResultSet resultSet = preparedStatement.executeQuery();
            ArrayList<Object[]> arrayList = new ArrayList<>();
            while(resultSet.next()) {
                Object[] obj = new Object[8];
                obj[0] = resultSet.getInt("bid");
                obj[1] = resultSet.getString("isbn");
                obj[2] = resultSet.getString("name");
                obj[3] = resultSet.getString("author");
                obj[4] = resultSet.getString("publisher");
                obj[5] = resultSet.getString("publicationdate");
                obj[6] = resultSet.getInt("stock");
                obj[7] = resultSet.getInt("borrowed");
                arrayList.add(obj);
            }
            return ConvertUtils.ArrayListToObject2D(arrayList);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }

    public static int getAllBookTotal() {
        String sql = "SELECT bid FROM `book`;";
        Statement statement = null;
        try {
            statement = connection.createStatement();
            statement.setQueryTimeout(30);
            ResultSet resultSet = statement.executeQuery(sql);
            int count = 0;
            while (resultSet.next()) {
                count++;
            }
            return count;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return 0;
        }
    }

    public static String getNickName() {
        if(authkey == null) {
            return "游客";
        }
        String sql = "SELECT * FROM `user` WHERE `authkey` = ? LIMIT 1;";
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setQueryTimeout(30);
            preparedStatement.setString(1, authkey);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(!resultSet.next()) {
                return "游客";
            }else {
                return resultSet.getString("nickname");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return "游客";
        }
    }

    private String generateKey(String userName, String passWord) {
        return wordCompletion(userName, 16, '0') + wordCompletion(passWord, 32, '1');
    }

    private String wordCompletion(String str, int length, char ch) {
        if (str.length() > length) {
            return str;
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < length - str.length(); i++) {
            stringBuilder.append(ch);
        }
        stringBuilder.append(str);
        return stringBuilder.toString();
    }

    public static String getAuthkey() {
        return authkey;
    }

    public static boolean isStudent() {
        return studentFlag;
    }
}
