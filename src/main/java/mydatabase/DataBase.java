package mydatabase;
import exceptions.ServiceException;
import java.sql.*;

public class DataBase {
    public static boolean connectDB(String un, String p) {
        Connection connection = null;
        try {
            String url = "jdbc:sqlserver://localhost:1433;databaseName=DentistDatabase;integratedSecurity=true";

            connection = DriverManager.getConnection(url,"root","");

            Statement statement = connection.createStatement();
            int log = statement.executeUpdate("dbo.LoginCredi("+un+","+p+")");

            return log == 1;

        } catch (SQLException except) {
            throw new ServiceException("Database error " + except);
        }
        finally {
            try {
                if (connection != null && !connection.isClosed()) {
                    connection.close();
                }
            } catch (SQLException except) {
                throw new ServiceException("Database error " + except);
            }
        }
    }
}
