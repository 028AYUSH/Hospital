import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import java.util.*;

public class ViewDoctor extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter pw = response.getWriter();
        List<Doctor> doctors = new ArrayList<>();

        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "system", "manager");

            String query = "SELECT * FROM doctors";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                doctors.add(new Doctor(rs.getString("doctor_id"), rs.getString("name"),
                                       rs.getString("specialization"), rs.getString("phone")));
            }
            conn.close();

            pw.println("<html><body>");
            pw.println("<h1>Doctor List</h1>");
            pw.println("<ul>");
            for (Doctor doc : doctors) {
                pw.println("<li>" + doc.getName() + " - " + doc.getSpecialization() + " - " + doc.getPhone() + "</li>");
            }
            pw.println("</ul>");
            pw.println("</body></html>");

        } catch (Exception e) {
            pw.println(e);
        }
    }
}
