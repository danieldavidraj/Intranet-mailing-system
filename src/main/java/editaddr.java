import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.sql.*;
import javax.sql.*;
import javax.naming.*;
import javax.servlet.annotation.WebServlet;

@WebServlet("/editaddr")
public class editaddr extends HttpServlet {
	Connection con=null;
	Statement st=null;
	ResultSetMetaData meta=null;
	PrintWriter out=null;

	public void init(ServletConfig sc)throws ServletException {
		try {
			super.init(sc);
			Context initCtx = new InitialContext();
			Context envCtx = (Context) initCtx.lookup("java:comp/env");
			DataSource ds = (DataSource) envCtx.lookup("jdbc/Postgres");
			
			con = ds.getConnection();
			st=con.createStatement();
		} catch(Exception e) {
			System.out.println(e.toString());
		}
	}
	public void service(HttpServletRequest req,HttpServletResponse res)throws ServletException,IOException {
		try { 
			res.setContentType("text/html");
			out=res.getWriter();
			String name=req.getParameter("nam");
			String nick=req.getParameter("nic");
			String addr=req.getParameter("add");

			out.println("<!DOCTYPE html> <html lang='en' > <head> <meta charset='UTF-8'> <title>Edit Address</title> <style> .mainDiv { display: flex; min-height: 100%; align-items: center; justify-content: center; background-color: #f9f9f9; font-family: 'Open Sans', sans-serif; } .cardStyle { width: 500px; border-color: white; background: #fff; padding: 36px 0; border-radius: 4px; margin: 30px 0; box-shadow: 0px 0 2px 0 rgba(0,0,0,0.25); } #signupLogo { max-height: 100px; margin: auto; display: flex; flex-direction: column; } .formTitle{ font-weight: 600; margin-top: 20px; color: #2F2D3B; text-align: center; } .inputLabel { font-size: 12px; color: #555; margin-bottom: 6px; margin-top: 24px; } .inputDiv { width: 70%; display: flex; flex-direction: column; margin: auto; } input { height: 40px; font-size: 16px; border-radius: 4px; border: none; border: solid 1px #ccc; padding: 0 11px; } input:disabled { cursor: not-allowed; border: solid 1px #eee; } .buttonWrapper { margin-top: 40px; } .submitButton { width: 70%; height: 40px; margin: auto; display: block; color: #fff; background-color: #065492; border-color: #065492; text-shadow: 0 -1px 0 rgba(0, 0, 0, 0.12); box-shadow: 0 2px 0 rgba(0, 0, 0, 0.035); border-radius: 4px; font-size: 14px; cursor: pointer; } .submitButton:disabled, button[disabled] { border: 1px solid #cccccc; background-color: #cccccc; color: #666666; } #loader { position: absolute; z-index: 1; margin: -2px 0 0 10px; border: 4px solid #f3f3f3; border-radius: 50%; border-top: 4px solid #666666; width: 14px; height: 14px; -webkit-animation: spin 2s linear infinite; animation: spin 2s linear infinite; } @keyframes spin { 0% { transform: rotate(0deg); } 100% { transform: rotate(360deg); } } </style> </head> <body> <div class='mainDiv'> <div class='cardStyle'>");
			out.println("<form action='changename' method='post'> <img src='https://s3-us-west-2.amazonaws.com/shipsy-public-assets/shipsy/SHIPSY_LOGO_BIRD_BLUE.png' id='signupLogo'/>");
			
			out.println("<h2 class='formTitle'>Adding new address to "+name+" address book</h2>");

			out.println("<div class='inputDiv'> <label class='inputLabel' for='nametext'>Name</label> <input type='text' id='nametext' name='nametext' value="+name+" required> </div>");
			out.println("<div class='inputDiv'> <label class='inputLabel' for='nicktext'>Nickname</label> <input type='text' id='nicktext' name='nicktext' value="+nick+" required> </div>");
			out.println("<div class='inputDiv'> <label class='inputLabel' for='addrtext'>OldMail_ID</label> <input type='text' id='addrtext' name='addrtext' value="+addr+" required> </div>");
			out.println("<div class='inputDiv'> <label class='inputLabel' for='newaddr'>NewMail_ID</label> <input type='text' id='newaddr' name='newaddr' required> </div>");

			out.println("<div class='buttonWrapper'> <button type='submit' id='submitButton' onclick='validateSignupForm()' class='submitButton pure-button pure-button-primary'> <span>Continue</span> </button> </div> </form> </div> </div></body></html>");

		} catch(Exception e) {
			out.println(e.toString());
		}
	}
}