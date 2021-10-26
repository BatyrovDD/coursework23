package su.usatu.project23.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLJDBCUtil {

	public static Connection getConnection() throws SQLException {
		Connection conn = null;

		String url = "jdbc:mysql://mysql:3306/project23";
		String user = "db_user_23";
		String password = "db_password_23";

		conn = DriverManager.getConnection(url, user, password);

		return conn;
	}

}