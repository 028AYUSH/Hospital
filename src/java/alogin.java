import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
public class alogin extends HttpServlet
{
public void doGet(HttpServletRequest req,HttpServletResponse res)throws IOException,ServletException
    {
        res.setContentType("text/html");
        PrintWriter pwl=res.getWriter();
        String nm=req.getParameter("L1");
        String nm1=req.getParameter("L2");
       
        
      
        
        try
        {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection con=DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE","system","manager");
            Statement stmt=con.createStatement();
            
            String q1="insert into admin_info values('"+nm+"','"+nm1+"' )";
            
            int x=stmt.executeUpdate(q1);
            if(x>0)
            {pwl.println("<html><body>Sign in successful <br>" + "<a href=new1.html>Goto Login Page</a></body></html>");
            }
            else{
                pwl.println("Registration unsuccessful");
            }
            con.close();
        }
        catch(Exception e)
        {
        pwl.println(e);
        }
}
}