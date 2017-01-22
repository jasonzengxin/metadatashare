package oracle.dicloud.odi.metadatashare;

import java.sql.SQLException;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;


public class DBConnection {

	public static Connection getConnection() throws SQLException, ClassNotFoundException, NamingException
	{
		Context ctx = new InitialContext();
		DataSource ds = (DataSource)ctx.lookup("java:comp/env/jdbc/odiMasterRepository");
		Connection connection = ds.getConnection();
                System.out.println(connection);

		return connection;
	}
	
	public static void main(String[]  args){
		try {
			getConnection();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
