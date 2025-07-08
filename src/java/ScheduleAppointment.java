import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import java.sql.Timestamp;

public class ScheduleAppointment extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter pw = response.getWriter();
        String patientId = request.getParameter("patientId");
        String doctorId = request.getParameter("doctorId");
        String appointmentDate = request.getParameter("appointmentDate");
        String appointmentTime = request.getParameter("appointmentTime");
        Timestamp requestedTimestamp = Timestamp.valueOf(appointmentDate + " " + appointmentTime + ":00");

        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "system", "manager");

           
            String availabilityQuery = "SELECT status FROM doctor_availability WHERE doctor_id = ? AND available_date = ?";
            PreparedStatement availabilityStmt = conn.prepareStatement(availabilityQuery);
            availabilityStmt.setString(1, doctorId);
            availabilityStmt.setDate(2, java.sql.Date.valueOf(appointmentDate));
            ResultSet availabilityRs = availabilityStmt.executeQuery();

            boolean isAvailable = false;
            if (availabilityRs.next()) {
                String availabilityStatus = availabilityRs.getString("availability_status");
                if ("Available".equals(availabilityStatus)) {
                    isAvailable = true;
                }
            }

            if (isAvailable) {
                // Schedule appointment
                String insertQuery = "INSERT INTO appointments (id, patient_id, doctor_id, appointment_date, status) VALUES (appointments_seq.NEXTVAL, ?, ?, ?, 'Scheduled')";
                PreparedStatement insertStmt = conn.prepareStatement(insertQuery);
                insertStmt.setLong(1, Long.parseLong(patientId));
                insertStmt.setLong(2, Long.parseLong(doctorId));
                insertStmt.setTimestamp(3, requestedTimestamp);

                int rowsInserted = insertStmt.executeUpdate();
                if (rowsInserted > 0) {
                    pw.println("<html><body><h1>Appointment scheduled successfully!</h1></body></html>");
                } else {
                    pw.println("<html><body><h1>Failed to schedule appointment. Please try again.</h1></body></html>");
                }
            } else {
                pw.println("<html><body><h1>Doctor is not available at the requested time. Please choose another date or time.</h1></body></html>");
            }

            conn.close();
        } catch (Exception e) {
            pw.println(e);
        }
    }
}
