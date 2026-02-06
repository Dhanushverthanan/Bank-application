
package bankOperation;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.*;
import bankdb.Dbconnections;

@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {

    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws IOException {

        HttpSession session = req.getSession(false);
        if (session == null) {
            res.sendRedirect("login.html");
            return;
        }

        int userId = (int) session.getAttribute("userId");

        try (Connection con = Dbconnections.getConnections(getServletContext());
        		PreparedStatement ps =
        			    con.prepareStatement("SELECT balance FROM accounts WHERE user_id=?;"))
        {

        			ps.setInt(1, userId);
        			ResultSet rs = ps.executeQuery();

        			if (rs.next()) {
        			    req.getSession().setAttribute("balance", rs.getDouble("balance"));
        			}

        			req.getRequestDispatcher("dashboard.jsp").forward(req, res);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
