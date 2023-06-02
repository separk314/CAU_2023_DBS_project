import com.mysql.jdbc.Driver;

import javax.swing.plaf.nimbus.State;
import java.nio.charset.StandardCharsets;
import java.sql.*;

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

    }

    public void connectDatabase() {
        try {
            Statement stmt = con.createStatement();
            String SQL = "CREATE DATABASE IF NOT EXISTS CustomerDatabase";
            stmt.executeUpdate(SQL);

            // CustomerDatabase 데이터베이스 선택
            SQL = "USE CustomerDatabase";
            stmt.executeUpdate(SQL);

            // Customer 테이블이 없다면 생성
            SQL = "CREATE TABLE IF NOT EXISTS Customer (id INT AUTO_INCREMENT PRIMARY KEY, serialId INT, name VARCHAR(255), gender VARCHAR(10), country VARCHAR(255), grade INT)";
            stmt.executeUpdate(SQL);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void insertRecord(Customer customer) {
        try {
            Statement stmt = con.createStatement();
            String name = customer.getName();
            String gender = customer.getGender();
            String country = customer.getCountry();
            int grade = customer.getGrade();
            int serialId = customer.getSerialId();

            // 문자열(name)을 UTF-8로 인코딩
            byte[] nameBytes = name.getBytes(StandardCharsets.UTF_8);
            String encodedName = new String(nameBytes, StandardCharsets.UTF_8);

            // 쿼리 실행
            String SQL = "INSERT INTO Customer (serialId, name, gender, country, grade) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pstmt = con.prepareStatement(SQL);
            pstmt.setInt(1, serialId);
            pstmt.setString(2, encodedName);
            pstmt.setString(3, gender);
            pstmt.setString(4, country);
            pstmt.setInt(5, grade);
            pstmt.executeUpdate();

        } catch (SQLException error) {
            error.printStackTrace();
        }
    }

    public void createBPlusTreeIndex() {
        try {
            Statement stmt = con.createStatement();

            // idx_customer_gender 인덱스가 이미 존재하는지 확인
            ResultSet resultSet = stmt.executeQuery("SHOW INDEX FROM Customer WHERE Key_name = 'idx_customer_gender'");
            if (resultSet.next()) {
                // 인덱스가 이미 존재하면 삭제
                stmt.executeUpdate("DROP INDEX idx_customer_gender ON Customer");
            }

            // idx_customer_country 인덱스가 이미 존재하는지 확인
            resultSet = stmt.executeQuery("SHOW INDEX FROM Customer WHERE Key_name = 'idx_customer_country'");
            if (resultSet.next()) {
                // 인덱스가 이미 존재하면 삭제
                stmt.executeUpdate("DROP INDEX idx_customer_country ON Customer");
            }

            // idx_customer_grade 인덱스가 이미 존재하는지 확인
            resultSet = stmt.executeQuery("SHOW INDEX FROM Customer WHERE Key_name = 'idx_customer_grade'");
            if (resultSet.next()) {
                // 인덱스가 이미 존재하면 삭제
                stmt.executeUpdate("DROP INDEX idx_customer_grade ON Customer");
            }

            // 새로운 인덱스 생성
            stmt.executeUpdate("CREATE INDEX idx_customer_gender ON Customer (gender) USING BTREE");
            stmt.executeUpdate("CREATE INDEX idx_customer_country ON Customer (country) USING BTREE");
            stmt.executeUpdate("CREATE INDEX idx_customer_grade ON Customer (grade) USING BTREE");


            System.out.println("B+tree index 생성 완료.");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void executeGenderCountQueryWithBPlusIndex(String gender) {
        try {
            String sql = "SELECT COUNT(*) AS total_count FROM customer WHERE gender = ?";
            long startTime = System.currentTimeMillis();

            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, gender);
            ResultSet resultSet = pstmt.executeQuery();

            if (resultSet.next()) {
                int totalCount = resultSet.getInt("total_count");
                System.out.println("Total Count: " + totalCount);
            }

            long endTime = System.currentTimeMillis();
            long executionTime = endTime - startTime;

            System.out.println("B+tree index를 사용한 시간: " + executionTime + " ms");

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
