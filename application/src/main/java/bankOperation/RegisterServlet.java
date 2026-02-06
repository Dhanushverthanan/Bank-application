package bankOperation;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.*;
import bankdb.Dbconnections;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws IOException {

        String name = req.getParameter("name");
        String email = req.getParameter("email");
        String password = req.getParameter("password");

        try (Connection con = Dbconnections.getConnections(getServletContext())) {

         
            PreparedStatement check = con.prepareStatement(
                "SELECT id FROM users WHERE email=?");
            check.setString(1, email);
            ResultSet rsCheck = check.executeQuery();

            if (rsCheck.next()) {
                res.getWriter().println("Email already registered. Please login.");
                return;
            }

           
            PreparedStatement ps1 = con.prepareStatement(
                "INSERT INTO users(name,email,password) VALUES(?,?,?)",
                Statement.RETURN_GENERATED_KEYS);
            ps1.setString(1, name);
            ps1.setString(2, email);
            ps1.setString(3, password);
            ps1.executeUpdate();

            ResultSet rs = ps1.getGeneratedKeys();
            rs.next();
            int userId = rs.getInt(1);

            // 3️⃣ Create account
            PreparedStatement ps2 = con.prepareStatement(
                "INSERT INTO accounts(user_id,balance) VALUES(?,0)");
            ps2.setInt(1, userId);
            ps2.executeUpdate();

            res.sendRedirect("login.html");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
