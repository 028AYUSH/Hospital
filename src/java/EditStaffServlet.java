import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

public class EditStaffServlet extends HttpServlet {

    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("text/html");
        PrintWriter out = res.getWriter();

        String staffId = req.getParameter("staff_id");
        String name = req.getParameter("name");
        String role = req.getParameter("role");
        String phone = req.getParameter("phone");
        String email = req.getParameter("email");
        String pass = req.getParameter("password");

        try {
            // Load Oracle JDBC Driver
            Class.forName("oracle.jdbc.driver.OracleDriver");
            // Establish connection
            Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "system", "manager");

            // SQL query to update staff details
            String query = "UPDATE staff SET name = ?, email = ?, password = ?, role = ?, phone = ? WHERE staff_id = ?";
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setString(1, name);
            pstmt.setString(2, email);
            pstmt.setString(3, pass);
            pstmt.setString(4, role);
            pstmt.setString(5, phone);
            pstmt.setString(6, staffId); // Set the staff_id parameter

            // Execute the update
            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                out.println("<h1>Staff details updated successfully!</h1>");
            } else {
                out.println("<h1>Staff not found. Update failed.</h1>");
            }

            // Close the connection
            con.close();
        } catch (ClassNotFoundException e) {
            out.println("<h1>Error: JDBC Driver not found!</h1>");
            e.printStackTrace();
        } catch (SQLException e) {
            out.println("<h1>Error: Database connection or SQL query issue!</h1>");
            e.printStackTrace();
        } catch (Exception e) {
            out.println("<h1>Unexpected Error: " + e.getMessage() + "</h1>");
            e.printStackTrace();
        }
    }
}
