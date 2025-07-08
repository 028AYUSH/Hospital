
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import java.util.regex.*;

public class AddDoctorServlet extends HttpServlet {

    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("text/html");
        PrintWriter out = res.getWriter();

        String name = req.getParameter("name");
        String specialization = req.getParameter("specialization");
        String phone = req.getParameter("phone");
        String email = req.getParameter("email");
        String password = req.getParameter("password");

        if (!isValidEmail(email)) {
            out.println("<h1>Error: Invalid email format. Please provide a valid email address.</h1>");
            return;
        }

        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "system", "manager");

            // Generate unique doctor ID
            String doctorId = generateDoctorId(con);

            String query = "INSERT INTO doctors (doctor_id, name, specialization, phone, email_id, password) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setString(1, doctorId);
            pstmt.setString(2, name);
            pstmt.setString(3, specialization);
            pstmt.setString(4, phone);
            pstmt.setString(5, email);
            pstmt.setString(6, password);

            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                out.println("<h1>Doctor added successfully! Doctor ID: " + doctorId + "</h1>");
            } else {
                out.println("<h1>Failed to add doctor.</h1>");
            }

            con.close();
        } catch (Exception e) {
            e.printStackTrace();
            out.println("<h1>Error: " + e.getMessage() + "</h1>");
        }
    }

    private String generateDoctorId(Connection con) throws SQLException {
        String prefix = "DOC";
        String newId = prefix + "001";

        String query = "SELECT MAX(doctor_id) AS max_id FROM doctors";
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
