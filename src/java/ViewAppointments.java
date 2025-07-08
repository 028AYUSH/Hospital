import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import java.util.*;

public class ViewAppointments extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("text/html");
        PrintWriter out = res.getWriter();

        List<Appointment> appointments = new ArrayList<>();
        
        // Database connection
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "system", "manager");
            
            String query = "SELECT * FROM appointments";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            // Fetching data from the database
            while (rs.next()) {
                int appointmentId = rs.getInt("appointment_id");
                int patientId = rs.getInt("patient_id");
                int doctorId = rs.getInt("doctor_id");
                String appointmentDate = rs.getString("appointment_date");
                String status = rs.getString("status");

                appointments.add(new Appointment(appointmentId, patientId, doctorId, appointmentDate, status));
            }

            con.close();
        } catch (Exception e) {
            e.printStackTrace();
            out.println("<h1>Error: " + e.getMessage() + "</h1>");
        }

        // Set the appointments list as a request attribute
        req.setAttribute("appointments", appointments);
        
        // Forward the request to the JSP page
        RequestDispatcher dispatcher = req.getRequestDispatcher("ViewAppointments.html");
        dispatcher.forward(req, res);
    }
}
class Appointment {
    private int appointmentId;
    private int patientId;
    private int doctorId;
    private String appointmentDate;
    private String status;

    public Appointment(int appointmentId, int patientId, int doctorId, String appointmentDate, String status) {
        this.appointmentId = appointmentId;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.appointmentDate = appointmentDate;
        this.status = status;
    }

    public int getAppointmentId() {
        return appointmentId;
    }

    public int getPatientId() {
        return patientId;
    }

    public int getDoctorId() {
        return doctorId;
    }

    public String getAppointmentDate() {
        return appointmentDate;
    }

    public String getStatus() {
        return status;
    }
}
