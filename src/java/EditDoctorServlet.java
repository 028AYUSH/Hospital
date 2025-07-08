import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

public class EditDoctorServlet extends HttpServlet {
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("text/html");
        PrintWriter out = res.getWriter();

        String doctorId = req.getParameter("doctor_id");
        String name = req.getParameter("name");
        String specialization = req.getParameter("specialization");
        String phone = req.getParameter("phone");
        String email = req.getParameter("email");
        String password = req.getParameter("password");

        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "system", "manager");

            String query = "UPDATE doctors SET name = ?, specialization = ?, phone = ?, email = ?, password = ? WHERE doctor_id = ?";
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setString(1, name);
            pstmt.setString(2, specialization);
            pstmt.setString(3, phone);
            pstmt.setString(4, email);
            pstmt.setString(5, password);
            pstmt.setString(6, doctorId);

            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                out.println("<h1>Doctor information updated successfully!</h1>");
            } else {
                out.println("<h1>Doctor not found.</h1>");
            }

            con.close();
        } catch (Exception e) {
            e.printStackTrace();
            out.println("<h1>Error: " + e.getMessage() + "</h1>");
        }
    }
}
