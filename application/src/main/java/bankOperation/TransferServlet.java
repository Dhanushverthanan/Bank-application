package bankOperation;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.*;
import bankdb.Dbconnections;

@WebServlet("/transfer")
public class TransferServlet extends HttpServlet {

    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws IOException {

        HttpSession session = req.getSession(false);
        if (session == null) {
            res.sendRedirect("login.html");
            return;
        }

        int senderId = (int) session.getAttribute("userId");
        String receiverEmail = req.getParameter("email");
        double amount = Double.parseDouble(req.getParameter("amount"));

        if (amount <= 0) {
            res.getWriter().println("Invalid transfer amount");
            return;
        }

        try (Connection con = Dbconnections.getConnections(getServletContext())) {
            con.setAutoCommit(false);

            
            PreparedStatement findUser = con.prepareStatement(
                "SELECT id FROM users WHERE email=?");
            findUser.setString(1, receiverEmail);
            ResultSet rsUser = findUser.executeQuery();

            if (!rsUser.next()) {
                res.getWriter().println("Receiver not found");
                con.rollback();
                return;
            }
            int receiverId = rsUser.getInt("id");

            
            PreparedStatement balStmt = con.prepareStatement(
                "SELECT balance FROM accounts WHERE user_id=?");
            balStmt.setInt(1, senderId);
            ResultSet rsBal = balStmt.executeQuery();

            if (!rsBal.next()) {
                res.getWriter().println("Account not found");
                con.rollback();
                return;
            }

            double senderBalance = rsBal.getDouble("balance");

            if (senderBalance < amount) {
                res.getWriter().println("Insufficient balance");
                con.rollback();
                return;
            }

          
            PreparedStatement deduct = con.prepareStatement(
                "UPDATE accounts SET balance = balance - ? WHERE user_id=?");
            deduct.setDouble(1, amount);
            deduct.setInt(2, senderId);
            deduct.executeUpdate();

           
            PreparedStatement credit = con.prepareStatement(
                "UPDATE accounts SET balance = balance + ? WHERE user_id=?");
            credit.setDouble(1, amount);
            credit.setInt(2, receiverId);
            credit.executeUpdate();

            con.commit();
            res.sendRedirect("dashboard");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
