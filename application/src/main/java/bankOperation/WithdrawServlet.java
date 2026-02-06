package bankOperation;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.*;
import bankdb.Dbconnections;

@WebServlet("/withdraw")
public class WithdrawServlet extends HttpServlet {

    private static final double LIMIT = 10000;

    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws IOException {

        HttpSession session = req.getSession(false);
        int userId = (int) session.getAttribute("userId");
        double amount = Double.parseDouble(req.getParameter("amount"));

        if (amount <= 0 || amount > LIMIT) {
            res.getWriter().println("Invalid withdrawal amount");
            return;
        }

        try (Connection con = Dbconnections.getConnections(getServletContext())) {

            PreparedStatement check = con.prepareStatement(
                "SELECT balance FROM accounts WHERE user_id=?");
            check.setInt(1, userId);
            ResultSet rs = check.executeQuery();

            if (rs.next() && rs.getDouble("balance") >= amount) {
                PreparedStatement ps = con.prepareStatement(
                    "UPDATE accounts SET balance = balance - ? WHERE user_id=?");
                ps.setDouble(1, amount);
                ps.setInt(2, userId);
                ps.executeUpdate();
            }

            res.sendRedirect("dashboard");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
