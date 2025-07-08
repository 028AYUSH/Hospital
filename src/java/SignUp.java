import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import java.util.regex.Pattern;

public class SignUp extends HttpServlet {

    public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        res.setContentType("text/html");
        PrintWriter pw = res.getWriter();

        String name = req.getParameter("name");
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String dob = req.getParameter("dob");

        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "system", "manager");

            if (!isValidEmail(email)) {
                pw.println("<html><body><p>Invalid email format.</p></body></html>");
                return;
            }

            String patientId = generatePatientId(con);

            String query = "INSERT INTO patient_info (p_id, email, password, name, dob) VALUES (?, ?, ?, ?, TO_DATE(?, 'YYYY-MM-DD'))";
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setString(1, patientId);
            pstmt.setString(2, email);
            pstmt.setString(3, password);
            pstmt.setString(4, name);
            pstmt.setString(5, dob);

            int rowsInserted = pstmt.executeUpdate();

            if (rowsInserted > 0) {
                pw.println("<html><body><div class='form-container'><h1>Registration Successful</h1><p class='success-msg'>Your Patient ID is: <span class='patient-id'>" + patientId + "</span></p><div class='form-footer'>Continue to <a href='PatLogin.html'>Login</a></div></div></body></html>");
            } else {
                pw.println("<html><body><p>Registration failed. Please try again.</p></body></html>");
            }
            con.close();
        } catch (Exception e) {
            pw.println("<html><body><p>Error: " + e.getMessage() + "</p></body></html>");
        }

        // Adding CSS within the HTML response
        pw.println("<style>");
        pw.println("body {");
        pw.println("    font-family: Arial, sans-serif;");
        pw.println("    background: linear-gradient(135deg, #7bdfeb, #3aaf85);");
        pw.println("    display: flex;");
        pw.println("    justify-content: center;");
        pw.println("    align-items: center;");
        pw.println("    height: 100vh;");
        pw.println("    margin: 0;");
        pw.println("    padding: 0;");
        pw.println("}");
        pw.println(".form-container {");
        pw.println("    background: #ffffff;");
        pw.println("    padding: 30px;");
        pw.println("    border-radius: 12px;");
        pw.println("    box-shadow: 0 6px 12px rgba(0, 0, 0, 0.1);");
        pw.println("    width: 100%;");
        pw.println("    max-width: 450px;");
        pw.println("    text-align: center;");
        pw.println("}");
        pw.println("h1 {");
        pw.println("    font-size: 32px;");
        pw.println("    color: #3aaf85;");
        pw.println("    margin-bottom: 20px;");
        pw.println("    font-weight: bold;");
        pw.println("}");
        pw.println(".success-msg {");
        pw.println("    font-size: 20px;");
        pw.println("    margin-bottom: 20px;");
        pw.println("    font-weight: normal;");
        pw.println("    color: #333;");
        pw.println("}");
        pw.println(".patient-id {");
        pw.println("    font-size: 24px;");
        pw.println("    font-weight: bold;");
        pw.println("    color: #4CAF50;");
        pw.println("}");
        pw.println(".form-footer {");
        pw.println("    margin-top: 20px;");
        pw.println("}");
        pw.println(".form-footer a {");
        pw.println("    text-decoration: none;");
        pw.println("    color: #4CAF50;");
        pw.println("    font-weight: bold;");
        pw.println("    transition: color 0.3s ease;");
        pw.println("}");
        pw.println(".form-footer a:hover {");
        pw.println("    color: #2e7d32;");
        pw.println("}");
        pw.println("</style>");
    }

    // Method to generate a unique patient ID
    private String generatePatientId(Connection con) throws SQLException {
        String prefix = "PAT";
        String newId = prefix + "001";

        String query = "SELECT MAX(p_id) AS max_id FROM patient_info";
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

    // Method to validate email
    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@"
                + "(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        if (email == null) {
            return false;
        }
        return pattern.matcher(email).matches();
    }
}
