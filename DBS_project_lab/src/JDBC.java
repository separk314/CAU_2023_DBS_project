import com.mysql.jdbc.Driver;

import javax.swing.plaf.nimbus.State;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class JDBC {
    Connection con = null;

    public void connectMySQL() {
        String server = "localhost";
        String userName = "root";
        String password = "qlalfqjsgh314.";

        // JDBC 드라이버
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException error) {
            System.out.println(error);
            error.printStackTrace();
        }

        // MySQL 접속
        try {
            con = DriverManager.getConnection("jdbc:mysql://" + server + "/" + "?useSSL=false", userName, password);
            System.out.println("MySQL 연결 완료");
        } catch (SQLException error) {
            System.out.println(error);
            error.printStackTrace();
        }

        try {
            Statement stmt = con.createStatement();
            String SQL = "CREATE DATABASE IF NOT EXISTS CustomerDatabase";
            stmt.executeUpdate(SQL);
            System.out.println("CustomerDatabase 데이터베이스가 성공적으로 생성되었습니다.");

            // Customer 데이터베이스 선택
            String useDatabaseSQL = "USE Customer";
            stmt.executeUpdate(useDatabaseSQL);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void disconnectMySQL() {
        try {
            if (con != null)    con.close();
            System.out.println("MySQL 연결 종료");
        } catch (SQLException error) {
            error.printStackTrace();
        }
    }
}
