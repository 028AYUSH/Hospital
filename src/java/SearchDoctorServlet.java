import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

public class SearchDoctorServlet extends HttpServlet {
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("text/html");
        PrintWriter out = res.getWriter();

        String doctorId = req.getParameter("doctor_id");

        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "system", "manager");

            String query = "SELECT * FROM doctors WHERE doctor_id = ? OR email = ?";
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setString(1, doctorId);
            pstmt.setString(2, doctorId);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                req.setAttribute("doctor_id", rs.getString("doctor_id"));
                req.setAttribute("name", rs.getString("name"));
                req.setAttribute("specialization", rs.getString("specialization"));
                req.setAttribute("phone", rs.getString("phone"));
                req.setAttribute("email", rs.getString("email"));
                req.setAttribute("password", rs.getString("password"));

                RequestDispatcher rd = req.getRequestDispatcher("EditDoctorForm.jsp");
                rd.forward(req, res);
            } else {
                out.println("<h1>No doctor found with that ID or email.</h1>");
            }

            con.close();
        } catch (Exception e) {
            e.printStackTrace();
            out.println("<h1>Error: " + e.getMessage() + "</h1>");
        }
    }
}
