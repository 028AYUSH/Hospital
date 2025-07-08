import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import java.util.regex.*;

public class AddStaffServlet extends HttpServlet {

    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("text/html");
        PrintWriter out = res.getWriter();

        String name = req.getParameter("name");
        String role = req.getParameter("role");
        String phone = req.getParameter("phone");
        String email = req.getParameter("email");
        String password = req.getParameter("password");

        // Validate email format
        if (!isValidEmail(email)) {
            out.println("<h1>Error: Invalid email format. Please provide a valid email address.</h1>");
            return;
        }

        try {
            // Load database driver
            Class.forName("oracle.jdbc.driver.OracleDriver");
            // Establish connection to database
            Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "system", "manager");

            // Generate unique staff ID
            String staffId = generateStaffId(con);

            // Insert staff details into database
            String query = "INSERT INTO staff (staff_id, name, email, password, role, phone) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setString(1, staffId);
            pstmt.setString(2, name);
            pstmt.setString(3, email);
            pstmt.setString(4, password);
            pstmt.setString(5, role);
            pstmt.setString(6, phone);
            

            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                out.println("<h1>Staff added successfully! Staff ID: " + staffId + "</h1>");
            } else {
                out.println("<h1>Failed to add staff.</h1>");
            }

            // Close the connection
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
            out.println("<h1>Error: " + e.getMessage() + "</h1>");
        }
    }

    private String generateStaffId(Connection con) throws SQLException {
        String prefix = "STF";
        String newId = prefix + "001";

        String query = "SELECT MAX(staff_id) AS max_id FROM staff";
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(query);

        if (rs.next() && rs.getString("max_id") != null) {
            String lastId = rs.getString("max_id");
            int num = Integer.parseInt(lastId.substring(3));
            num++;
            newId = prefix + String.format("%03d", num);
        }

        return newId;
    }

    private boolean isValidEmail(String email) {
        // Define a regex for email validation
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@"
                + "(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        if (email == null) {
            return false;
        }
        return pattern.matcher(email).matches();
    }
}
