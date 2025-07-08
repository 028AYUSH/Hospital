import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

public class UpdateAvailabilityServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter pw = response.getWriter();
        String doctorId = (String) request.getSession().getAttribute("doctorId");
        String availableDate = request.getParameter("availableDate");
        String availabilityStatus = request.getParameter("availabilityStatus");

        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "system", "manager");

            String checkQuery = "SELECT COUNT(*) FROM doctor_availability WHERE doctor_id = ? AND available_date = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
            checkStmt.setString(1, doctorId);
            checkStmt.setDate(2, java.sql.Date.valueOf(availableDate));
            ResultSet rs = checkStmt.executeQuery();
            rs.next();
            int count = rs.getInt(1);

            String query;
            if (count > 0) {
                
                query = "UPDATE doctor_availability SET availability_status = ? WHERE doctor_id = ? AND available_date = ?";
            } else {
                
                query = "INSERT INTO doctor_availability (doctor_id, available_date, availability_status) VALUES (?, ?, ?)";
            }

            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, doctorId);
            stmt.setDate(2, java.sql.Date.valueOf(availableDate));
            stmt.setString(3, availabilityStatus);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                pw.println("<html><body><h1>Availability updated successfully!</h1></body></html>");
            } else {
                pw.println("<html><body><h1>Failed to update availability. Please try again.</h1></body></html>");
            }
            conn.close();
        } catch (Exception e) {
            pw.println(e);
        }
    }
}
