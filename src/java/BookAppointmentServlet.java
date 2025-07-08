import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import java.sql.Timestamp;

public class BookAppointmentServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter pw = response.getWriter();
        String doctorId = request.getParameter("doctorId");

        pw.println("<html>");
        pw.println("<head><title>Book Appointment</title></head>");
        pw.println("<body>");
        pw.println("<h1>Book Appointment with Doctor</h1>");
        pw.println("<form action='BookAppointmentServlet' method='post'>");
        pw.println("<input type='hidden' name='doctorId' value='" + doctorId + "'>");
        pw.println("<label for='patientId'>Patient ID:</label>");
        pw.println("<input type='text' id='patientId' name='patientId' required><br>");
        pw.println("<label for='date'>Date:</label>");
        pw.println("<input type='datetime-local' id='date' name='date' required><br>");
        pw.println("<button type='submit'>Book Appointment</button>");
        pw.println("</form>");
        pw.println("</body>");
        pw.println("</html>");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter pw = response.getWriter();
        String doctorId = request.getParameter("doctorId");
        String patientId = request.getParameter("patientId");
        Timestamp appointmentDate = Timestamp.valueOf(request.getParameter("date").replace("T", " ") + ":00");

        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "system", "manager");

            String query = "INSERT INTO appointments (id, patient_id, doctor_id, appointment_date, status) VALUES (appointments_seq.NEXTVAL, ?, ?, ?, 'Scheduled')";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, patientId);
            stmt.setString(2, doctorId);
            stmt.setTimestamp(3, appointmentDate);

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                pw.println("<html><body><h1>Appointment booked successfully!</h1></body></html>");
            } else {
                pw.println("<html><body><h1>Failed to book appointment. Please try again.</h1></body></html>");
            }
            conn.close();
        } catch (Exception e) {
            pw.println(e);
        }
    }
}
