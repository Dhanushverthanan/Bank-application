package bankdb;

import jakarta.servlet.ServletContext;
import java.sql.Connection;
import java.sql.DriverManager;

public class Dbconnections {

    public static Connection getConnections(ServletContext ctx) throws Exception {

        String url = ctx.getInitParameter("url");
        String user = ctx.getInitParameter("username");
        String pass = ctx.getInitParameter("password");

        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(url, user, pass);
    }
}
