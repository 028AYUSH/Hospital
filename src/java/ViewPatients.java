import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import java.util.*;

public class ViewPatients extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter pw = response.getWriter();
        List<Patient> patients = new ArrayList<>();
        String doctorId = (String) request.getSession().getAttribute("doctorId");

        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "system", "manager");

            // Query to get patients assigned to the doctor based on the appointments
            String query = "SELECT p.p_id, p.name, p.email, p.dob FROM patient_info p " +
                           "JOIN appointments a ON p.p_id = a.patient_id WHERE a.doctor_id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, doctorId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                patients.add(new Patient(rs.getString("p_id"), rs.getString("name"), rs.getString("email"), rs.getDate("dob")));
            }
            conn.close();

            pw.println("<html><body>");
            pw.println("<h1>Assigned Patients</h1>");
            pw.println("<ul>");
            for (Patient patient : patients) {
                pw.println("<li>" + patient.getName() + " - Email: " + patient.getEmail() + " - DOB: " + patient.getDob() + "</li>");
            }
            pw.println("</ul>");
            pw.println("</body></html>");

        } catch (Exception e) {
            pw.println(e);
        }
    }
}
