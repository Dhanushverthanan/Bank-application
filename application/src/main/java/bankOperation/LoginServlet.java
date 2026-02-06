package bankOperation;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.*;
import bankdb.Dbconnections;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws IOException {

        String email = req.getParameter("email");
        String password = req.getParameter("password");

        try (Connection con = Dbconnections.getConnections(getServletContext());
             PreparedStatement ps = con.prepareStatement(
                "SELECT id,name FROM users WHERE email=? AND password=?")) {

            ps.setString(1, email);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                HttpSession session = req.getSession();
                session.setAttribute("userId", rs.getInt("id"));
                session.setAttribute("name", rs.getString("name"));
                res.sendRedirect("dashboard");
            } else {
                res.getWriter().println("Invalid Login");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
