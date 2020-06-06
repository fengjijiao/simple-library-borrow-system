package test7;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Date;

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

    public static Object[][] getAllBook(int start, int num) {
        String sql = "SELECT bid, isbn, name, author, publisher, publicationdate, stock, borrowed FROM `book` LIMIT ?,?;";
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setQueryTimeout(30);
            preparedStatement.setInt(1, start);
            preparedStatement.setInt(2, num);
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

    public static Object[][] getAllBookSearch(String isbn, String name, String author, String publisher, int start, int num) {
        String sql = "SELECT bid, isbn, name, author, publisher, publicationdate, stock, borrowed FROM `book`";
        int mul = 0;
        if(!isbn.equals("")) {
            sql+=" WHERE `isbn` LIKE ?";
            mul++;
        }
        if(!name.equals("")) {
            sql+=(mul==0?" WHERE ":" AND ")+"`name` LIKE ?";
            mul++;
        }
        if(!author.equals("")) {
            sql+=(mul==0?" WHERE ":" AND ")+"`author` LIKE ?";
            mul++;
        }
        if(!publisher.equals("")) {
            sql+=(mul==0?" WHERE ":" AND ")+"`publisher` LIKE ?";
            mul++;
        }
        sql+=" LIMIT ?,?;";
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setQueryTimeout(30);
            int mck = 0;
            if(mck < mul) {
                if(!isbn.equals("")) preparedStatement.setString(++mck, generateSQLLikeMatche(isbn,2));
                if(!name.equals("")) preparedStatement.setString(++mck, generateSQLLikeMatche(name,2));
                if(!author.equals("")) preparedStatement.setString(++mck, generateSQLLikeMatche(author,2));
                if(!publisher.equals("")) preparedStatement.setString(++mck, generateSQLLikeMatche(publisher, 2));
            }
            preparedStatement.setInt(++mck, start);
            preparedStatement.setInt(++mck, num);
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

    public static int getAllBookSearchTotal(String isbn, String name, String author, String publisher) {
        String sql = "SELECT bid FROM `book`";
        int mul = 0;
        if(!isbn.equals("")) {
            sql+=" WHERE `isbn` LIKE ?";
            mul++;
        }
        if(!name.equals("")) {
            sql+=(mul==0?" WHERE ":" AND ")+"`name` LIKE ?";
            mul++;
        }
        if(!author.equals("")) {
            sql+=(mul==0?" WHERE ":" AND ")+"`author` LIKE ?";
            mul++;
        }
        if(!publisher.equals("")) {
            sql+=(mul==0?" WHERE ":" AND ")+"`publisher` LIKE ?";
            mul++;
        }
        sql+=";";
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setQueryTimeout(30);
            int mck = 0;
            if(mck < mul) {
                if(!isbn.equals("")) preparedStatement.setString(++mck, generateSQLLikeMatche(isbn,2));
                if(!name.equals("")) preparedStatement.setString(++mck, generateSQLLikeMatche(name,2));
                if(!author.equals("")) preparedStatement.setString(++mck, generateSQLLikeMatche(author,2));
                if(!publisher.equals("")) preparedStatement.setString(++mck, generateSQLLikeMatche(publisher, 2));
            }
            ResultSet resultSet = preparedStatement.executeQuery();
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

    public static void deleteBook(int bid) {
        String sql = "DELETE FROM `book` WHERE `bid` = ?;";
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setQueryTimeout(30);
            preparedStatement.setInt(1, bid);
            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static void addBook(String isbn, String name, String author, String publisher, String publicationdate, int stock) {
        String sql = "INSERT INTO `book` (isbn, name, author, publisher, publicationdate, stock, borrowed) VALUES( ?, ?, ?, ?, ?, ?, 0);";
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setQueryTimeout(30);
            preparedStatement.setString(1, isbn);
            preparedStatement.setString(2, name);
            preparedStatement.setString(3, author);
            preparedStatement.setString(4, publisher);
            preparedStatement.setString(5, publicationdate);
            preparedStatement.setInt(6, stock);
            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static void updateBook(int bid, String isbn, String name, String author, String publisher, String publicationdate, int stock) {
        String sql = "UPDATE `book` SET `isbn` = ?, `name` = ?, `author` = ?, `publisher` = ?, `publicationdate` = ?, `stock` = ? WHERE `bid` = ?;";
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setQueryTimeout(30);
            preparedStatement.setString(1, isbn);
            preparedStatement.setString(2, name);
            preparedStatement.setString(3, author);
            preparedStatement.setString(4, publisher);
            preparedStatement.setString(5, publicationdate);
            preparedStatement.setInt(6, stock);
            preparedStatement.setInt(7, bid);
            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static boolean isExistsBook(String isbn) {
        String sql = "SELECT * FROM `book` WHERE `isbn` = ?;";
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setQueryTimeout(30);
            preparedStatement.setString(1, isbn);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                return true;
            }else {
                return false;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return true;
        }
    }

    public static HashMap<String, Object> getOneBook(int bid) {
        HashMap<String, Object> hashMap = new HashMap<>();
        String sql = "SELECT * FROM `book` WHERE `bid` = ? LIMIT 1;";
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setQueryTimeout(30);
            preparedStatement.setInt(1, bid);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                hashMap.put("isbn", resultSet.getString("isbn"));
                hashMap.put("name", resultSet.getString("name"));
                hashMap.put("author", resultSet.getString("author"));
                hashMap.put("publisher", resultSet.getString("publisher"));
                hashMap.put("publicationdate", resultSet.getString("publicationdate"));
                hashMap.put("stock", resultSet.getInt("stock"));
            }
            return hashMap;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }

    public static Object[][] getAllBorrow(int start, int num) {
        String sql = "SELECT brid,uid,bid,starttime,endtime,status FROM `borrow` LIMIT ?,?;";
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setQueryTimeout(30);
            preparedStatement.setInt(1, start);
            preparedStatement.setInt(2, num);
            ResultSet resultSet = preparedStatement.executeQuery();
            ArrayList<Object[]> arrayList = new ArrayList<>();
            while(resultSet.next()) {
                Object[] obj = new Object[6];
                obj[0] = resultSet.getInt("brid");
                obj[1] = resultSet.getInt("uid");
                obj[2] = resultSet.getInt("bid");
                obj[3] = resultSet.getString("starttime");
                obj[4] = resultSet.getString("endtime");
                obj[5] = GeneralUtils.getBorrowStatus(resultSet.getInt("status"));
                arrayList.add(obj);
            }
            return ConvertUtils.ArrayListToObject2D(arrayList);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }

    public static Object[][] getAllBorrowSearch(String isbn, String name, String author, String publisher, int start, int num) {
        String sql = "SELECT brid,uid,bid,starttime,endtime,status FROM `borrow`";
        int mul = 0;
        if(!isbn.equals("")) {
            sql+=" WHERE `isbn` LIKE ?";
            mul++;
        }
        if(!name.equals("")) {
            sql+=(mul==0?" WHERE ":" AND ")+"`name` LIKE ?";
            mul++;
        }
        if(!author.equals("")) {
            sql+=(mul==0?" WHERE ":" AND ")+"`author` LIKE ?";
            mul++;
        }
        if(!publisher.equals("")) {
            sql+=(mul==0?" WHERE ":" AND ")+"`publisher` LIKE ?";
            mul++;
        }
        sql+=" LIMIT ?,?;";
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setQueryTimeout(30);
            int mck = 0;
            if(mck < mul) {
                if(!isbn.equals("")) preparedStatement.setString(++mck, generateSQLLikeMatche(isbn,2));
                if(!name.equals("")) preparedStatement.setString(++mck, generateSQLLikeMatche(name,2));
                if(!author.equals("")) preparedStatement.setString(++mck, generateSQLLikeMatche(author,2));
                if(!publisher.equals("")) preparedStatement.setString(++mck, generateSQLLikeMatche(publisher, 2));
            }
            preparedStatement.setInt(++mck, start);
            preparedStatement.setInt(++mck, num);
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

    public static int getAllBorrowSearchTotal(String isbn, String name, String author, String publisher) {
        String sql = "SELECT brid FROM `borrow`";
        int mul = 0;
        if(!isbn.equals("")) {
            sql+=" WHERE `isbn` LIKE ?";
            mul++;
        }
        if(!name.equals("")) {
            sql+=(mul==0?" WHERE ":" AND ")+"`name` LIKE ?";
            mul++;
        }
        if(!author.equals("")) {
            sql+=(mul==0?" WHERE ":" AND ")+"`author` LIKE ?";
            mul++;
        }
        if(!publisher.equals("")) {
            sql+=(mul==0?" WHERE ":" AND ")+"`publisher` LIKE ?";
            mul++;
        }
        sql+=";";
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setQueryTimeout(30);
            int mck = 0;
            if(mck < mul) {
                if(!isbn.equals("")) preparedStatement.setString(++mck, generateSQLLikeMatche(isbn,2));
                if(!name.equals("")) preparedStatement.setString(++mck, generateSQLLikeMatche(name,2));
                if(!author.equals("")) preparedStatement.setString(++mck, generateSQLLikeMatche(author,2));
                if(!publisher.equals("")) preparedStatement.setString(++mck, generateSQLLikeMatche(publisher, 2));
            }
            ResultSet resultSet = preparedStatement.executeQuery();
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

    public static int getAllBorrowTotal() {
        String sql = "SELECT brid FROM `borrow`;";
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

    public static void deleteBorrow(int brid) {
        String sql = "DELETE FROM `borrow` WHERE `brid` = ?;";
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setQueryTimeout(30);
            preparedStatement.setInt(1, brid);
            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    //默认借阅中
    public static void addBorrow(int uid,int bid,String starttime,String endtime) {
        addBorrow(uid,bid,starttime,endtime,0);
    }

    public static void addBorrow(int uid,int bid,String starttime,String endtime, int status) {
        String sql = "INSERT INTO `borrow` (uid,bid,starttime,endtime,status) VALUES( ?, ?, ?, ?, ?);";
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setQueryTimeout(30);
            preparedStatement.setInt(1, uid);
            preparedStatement.setInt(2, bid);
            preparedStatement.setString(3, starttime);
            preparedStatement.setString(4, endtime);
            preparedStatement.setInt(5, status);
            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static void updateBorrowStatus(int status) {
        Date dNow = new Date();
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String sql = "UPDATE `borrow` SET `status` = ?, `endtime` = ? WHERE `brid` = ?;";
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setQueryTimeout(30);
            preparedStatement.setInt(1, status);
            preparedStatement.setString(2, ft.format(dNow));
            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static HashMap<String, Object> getOneBorrow(int brid) {
        HashMap<String, Object> hashMap = new HashMap<>();
        String sql = "SELECT * FROM `borrow` WHERE `brid` = ? LIMIT 1;";
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setQueryTimeout(30);
            preparedStatement.setInt(1, brid);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                hashMap.put("uid", resultSet.getInt("uid"));
                hashMap.put("bid", resultSet.getInt("bid"));
                hashMap.put("starttime", resultSet.getString("starttime"));
                hashMap.put("endtime", resultSet.getString("endtime"));
                hashMap.put("status", resultSet.getInt("status"));
            }
            return hashMap;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
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

    private static String generateSQLLikeMatche(String str, int type) {
        if(type == 0) {
            return '%'+str;
        }else if(type == 1) {
            return str+'%';
        }else if(type == 2) {
            return '%'+str+'%';
        }else {
            return str;
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
