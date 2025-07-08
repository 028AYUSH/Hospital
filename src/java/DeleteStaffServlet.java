import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

public class DeleteStaffServlet extends HttpServlet {
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("text/html");
        PrintWriter out = res.getWriter();

        String staffId = req.getParameter("staff_id");

        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "system", "manager");

            String query = "DELETE FROM staff WHERE staff_id = ?";
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setString(1, staffId);

            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                out.println("<h1>Staff deleted successfully!</h1>");
            } else {
                out.println("<h1>Staff not found.</h1>");
            }

            con.close();
        } catch (Exception e) {
            e.printStackTrace();
            out.println("<h1>Error: " + e.getMessage() + "</h1>");
        }
    }
}
