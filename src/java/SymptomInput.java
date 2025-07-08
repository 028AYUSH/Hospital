import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import java.util.*;

public class SymptomInput extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter pw = response.getWriter();
        String symptom = request.getParameter("symptom");
        List<Doctor> doctors = new ArrayList<>();

        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "system", "manager");

            String query = "SELECT d.doctor_id, d.name, d.specialization, d.phone " +
                           "FROM doctors d " +
                           "JOIN symptoms s ON d.specialization = s.specialization " +
                           "WHERE s.symptom_name = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, symptom);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                doctors.add(new Doctor(rs.getString("doctor_id"), rs.getString("name"),
                                       rs.getString("specialization"), rs.getString("phone")));
            }
            conn.close();

            pw.println("<html>");
            pw.println("<head><title>Doctor List</title></head>");
            pw.println("<body>");
            pw.println("<h1>Doctors for the symptom: " + symptom + "</h1>");
            pw.println("<ul>");
            for (Doctor doc : doctors) {
                pw.println("<li>" + doc.getName() + " - " + doc.getSpecialization() + " - " + doc.getPhone() + 
                           " <a href='BookAppointmentServlet?doctorId=" + doc.getDoctorId() + "'>Book Appointment</a></li>");
            }
            pw.println("</ul>");
            pw.println("</body>");
            pw.println("</html>");

        } catch (Exception e) {
            pw.println(e);
        }
    }
}

class Doctor {
    private String doctorId;
    private String name;
    private String specialization;
    private String phone;

    public Doctor(String doctorId, String name, String specialization, String phone) {
        this.doctorId = doctorId;
        this.name = name;
        this.specialization = specialization;
        this.phone = phone;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "Doctor [doctorId=" + doctorId + ", name=" + name + ", specialization=" + specialization + ", phone=" + phone + "]";
    }
}
