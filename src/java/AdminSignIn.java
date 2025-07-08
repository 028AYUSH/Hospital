
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

public class AdminSignIn extends HttpServlet {

    public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        res.setContentType("text/html");
        PrintWriter pw1 = res.getWriter();
        String eid = req.getParameter("n1");
        String pass = req.getParameter("n2");
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "system", "manager");
            Statement stmt = con.createStatement();
            String q1 = "SELECT * FROM admin_info WHERE U_ID='"+eid+"' AND PASSWORD='"+pass+"'";
           
            ResultSet rs = stmt.executeQuery(q1);
            if (rs.next()) {
                 pw1.println("<html>");
                pw1.println("<head><title>Admin Options</title></head>");
                pw1.println("<body>");
                pw1.println("<h1>Welcome, " + rs.getString(1) + "!</h1>");
                pw1.println("<h2>Select an option:</h2>");
                pw1.println("<ul>");
                pw1.println("<li><a href='doctor_management.html'>DOCTOR</a></li>");
                pw1.println("<li><a href='staff_management.html'>STAFF</a></li>");
                pw1.println("</ul>");
                pw1.println("</body>");
                pw1.println("</html>");
            } else {
                pw1.println("<html><body>");
                pw1.println("<h1>Login failed. Please try again.</h1>");
                pw1.println("</body></html>");
            }
            con.close();

        } catch (Exception e) {
            pw1.println(e);
        }
    }
}
