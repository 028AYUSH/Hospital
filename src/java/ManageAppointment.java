import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

public class ManageAppointment extends HttpServlet {
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("text/html");
        PrintWriter out = res.getWriter();

        int appointmentId = Integer.parseInt(req.getParameter("appointmentId"));
        String action = req.getParameter("action"); // Action could be "cancel" or "reschedule"
        String newDate = req.getParameter("newDate");
        int newDoctorId = Integer.parseInt(req.getParameter("newDoctorId"));

        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "system", "manager");

            if (action.equals("cancel")) {
                // Cancel the appointment
                String cancelQuery = "UPDATE appointments SET status = 'Canceled' WHERE appointment_id = ?";
                PreparedStatement pstmt = con.prepareStatement(cancelQuery);
                pstmt.setInt(1, appointmentId);
                pstmt.executeUpdate();
                out.println("<h1>Appointment canceled successfully!</h1>");
                res.sendRedirect("appointmentCanceled.html"); // Redirect to canceled appointment page
            } else if (action.equals("reschedule")) {
                // Check if the new doctor is available on the new date
                String checkAvailabilityQuery = "SELECT * FROM doctor_availability WHERE doctor_id = ? AND available_date = TO_DATE(?, 'YYYY-MM-DD')";
                PreparedStatement pstmt = con.prepareStatement(checkAvailabilityQuery);
                pstmt.setInt(1, newDoctorId);
                pstmt.setString(2, newDate);

                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    // Reschedule the appointment
                    String rescheduleQuery = "UPDATE appointments SET doctor_id = ?, appointment_date = TO_DATE(?, 'YYYY-MM-DD'), status = 'Rescheduled' WHERE appointment_id = ?";
                    pstmt = con.prepareStatement(rescheduleQuery);
                    pstmt.setInt(1, newDoctorId);
                    pstmt.setString(2, newDate);
                    pstmt.setInt(3, appointmentId);

                    pstmt.executeUpdate();
                    out.println("<h1>Appointment rescheduled successfully!</h1>");
                    res.sendRedirect("appointmentRescheduled.html"); // Redirect to rescheduled appointment page
                } else {
                    out.println("<h1>New doctor is not available at the selected time.</h1>");
                }
            }

            con.close();
        } catch (Exception e) {
            e.printStackTrace();
            out.println("<h1>Error: " + e.getMessage() + "</h1>");
        }
    }
}
