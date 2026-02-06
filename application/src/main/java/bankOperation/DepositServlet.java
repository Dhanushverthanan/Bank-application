package bankOperation;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.*;
import bankdb.Dbconnections;

@WebServlet("/deposit")
public class DepositServlet extends HttpServlet {

    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws IOException {

        HttpSession session = req.getSession(false);
        int userId = (int) session.getAttribute("userId");
        double amount = Double.parseDouble(req.getParameter("amount"));

        if (amount <= 0) {
            res.getWriter().println("Invalid deposit amount");
            return;
        }

        try (Connection con = Dbconnections.getConnections(getServletContext());
             PreparedStatement ps = con.prepareStatement(
                "UPDATE accounts SET balance = balance + ? WHERE user_id=?")) {

            ps.setDouble(1, amount);
            ps.setInt(2, userId);
            ps.executeUpdate();

            res.sendRedirect("dashboard");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
