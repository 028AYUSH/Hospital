import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
public class sympreg extends HttpServlet
{
public void doGet(HttpServletRequest req,HttpServletResponse res)throws IOException,ServletException
    {
        res.setContentType("text/html");
        PrintWriter pwl=res.getWriter();
        String nm=req.getParameter("L1");
        String nm1=req.getParameter("L2");
        String nm2=req.getParameter("L3");
        
        
      
        
        try
        {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection con=DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE","system","manager");
            Statement stmt=con.createStatement();
            
            String q1="insert into symptoms values('"+nm+"','"+nm1+"','"+nm2+"' )";
            
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