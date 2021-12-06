import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.sql.*;
import javax.sql.*;
import javax.naming.*;
import javax.servlet.annotation.WebServlet;

@WebServlet("/inboxserver")
public class inboxserver extends HttpServlet {
	Connection con;
	ResultSet rs=null;
	Statement st;
	PrintWriter out;

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
			String una=null;
			Cookie[] c = req.getCookies();
			if(c!=null)
			for(int i=0;i<c.length;i++) {
				if(c[i].getName().equals("signin")) {
					una=c[i].getValue();
					break;
				}
			}
			String mailsel="select count(*) AS rowcount from "+una;
			rs=st.executeQuery(mailsel);
			rs.next();
			out.println("<!DOCTYPE html><html lang='en'><head><meta charset='utf-8'><title>Mailing System</title><meta name='viewport' content='width=device-width, initial-scale=1'><script src='https://code.jquery.com/jquery-1.10.2.min.js'></script><link href='https://cdn.jsdelivr.net/npm/bootstrap@4.4.1/dist/css/bootstrap.min.css' rel='stylesheet'><script src='https://cdn.jsdelivr.net/npm/bootstrap@4.4.1/dist/js/bootstrap.bundle.min.js'></script></head><body><link href='https://maxcdn.bootstrapcdn.com/font-awesome/4.3.0/css/font-awesome.min.css' rel='stylesheet' /><div class='container'><div class='row'><div class='col-md-12'><div class='card'><div class='card-body bg-primary text-white mailbox-widget pb-0'><h2 class='text-white pb-3'>"+ una.substring(0, 1).toUpperCase() + una.substring(1) + "&apos;s Mailbox</h2><ul class='nav nav-tabs custom-tab border-bottom-0 mt-4' id='myTab' role='tablist'><li class='nav-item'><a class='nav-link active' id='inbox-tab' data-toggle='tab' aria-controls='inbox' href='#inbox' role='tab' aria-selected='true'><span class='d-block d-md-none'><i class='ti-email'></i></span><span class='d-none d-md-block'> INBOX</span></a></li><li class='nav-item'><a class='nav-link' id='sent-tab' data-toggle='tab' aria-controls='sent' href='#sent' role='tab' aria-selected='false'><span class='d-block d-md-none'><i class='ti-export'></i></span><span class='d-none d-md-block'>SENT</span></a></li><li class='nav-item'><a class='nav-link' id='spam-tab' data-toggle='tab' aria-controls='spam' href='#spam' role='tab' aria-selected='false'><span class='d-block d-md-none'><i class='ti-panel'></i></span><span class='d-none d-md-block'>SPAM</span></a></li><li class='nav-item'><a class='nav-link' id='delete-tab' data-toggle='tab' aria-controls='delete' href='#delete' role='tab' aria-selected='false'><span class='d-block d-md-none'><i class='ti-trash'></i></span><span class='d-none d-md-block'>DELETED</span></a></li></ul></div><div class='tab-content' id='myTabContent'><div class='tab-pane fade active show' id='inbox' aria-labelledby='inbox-tab' role='tabpanel'><div><div class='row p-4 no-gutters align-items-center'><div class='col-sm-12 col-md-6'><h3 class='font-light mb-0'>");
			out.println("<i class='ti-email mr-2'></i>"+ rs.getInt("rowcount") +" emails</h3></div><div class='col-sm-12 col-md-6'><ul class='list-inline dl mb-0 float-left float-md-right'>");
			
			out.println("<form action='selectfolder' method=post name='f1'>");
			
			out.println("<li class='list-inline-item text-info mr-3'><button type='submit' class='btn btn-circle btn-warning text-white'><i class='fa fa-arrows'></i></button><span class='ml-2 font-normal text-dark'>Move to </span><select name='li'>");

			String foldsel="select foldername from "+una+"folder";
			rs=st.executeQuery(foldsel);
			while(rs.next()) {
				out.println("<option>"+rs.getString(1));
			}
			out.println("</select></li>");

			out.println("<li class='list-inline-item text-info mr-3'><a href='compose.html'><button class='btn btn-circle btn-success text-white' href='javascript:void(0)'><i class='fa fa-plus'></i></button><span class='ml-2 font-normal text-dark'>Compose</span></a></li><li class='list-inline-item text-danger'><a href='deletemail'><button class='btn btn-circle btn-danger text-white' href='javascript:void(0)'><i class='fa fa-trash'></i></button><span class='ml-2 font-normal text-dark'>Delete</span></a></li></ul></div></div>");
			out.println("<div class='table-responsive'><table class='table email-table no-wrap table-hover v-middle mb-0 font-14'><tbody>");
	        mailsel="select msgfrom,subject,msgdate from "+una;
			rs=st.executeQuery(mailsel);

			int i = 1;
			while(rs.next()) {	
				String from=rs.getString(1);
				String sub=rs.getString(2);
				String dat=rs.getString(3);
				out.println("<tr>  <td class='pl-3'> <div class='custom-control custom-checkbox'> ");
				out.println("<input type='checkbox' class='custom-control-input' id='cst"+i+"' name="+from+"|"+dat+" /> ");
				out.println("<label class='custom-control-label' for='cst"+i+"'> </label> ");
				out.println("</div> </td>  <td><i class='fa fa-star text-warning'></i></td> <td> <a href='getmsg?msgf="+from+"&msgd="+dat+"' target='rightf'> <span class='mb-0 text-muted'>"+from+"</span> </a> </td>  <td> <a class='link' href='javascript: void(0)'> <span class='badge badge-pill text-white font-medium badge-danger mr-2'>Work</span> <span class='text-dark'>"+sub+"</span> </a> </td>  <td><i class='fa fa-paperclip text-muted'></i></td>  <td class='text-muted'>"+dat+"</td> </tr>");
				i++;
			}
			out.println("</tbody></table></div></div> </div> <div class='tab-pane fade' id='sent' aria-labelledby='sent-tab' role='tabpanel'> <div class='row p-3 text-dark'> <div class='col-md-6'> <h3 class='font-light'>Lets check profile</h3> <h4 class='font-light'>you can use it with the small code</h4> </div> <div class='col-md-6 text-right'> <p>Donec pede justo, fringilla vel, aliquet nec, vulputate eget, arcu. In enim justo, rhoncus ut, imperdiet a.</p> </div> </div> </div> <div class='tab-pane fade' id='spam' aria-labelledby='spam-tab' role='tabpanel'> <div class='row p-3 text-dark'> <div class='col-md-6'> <h3 class='font-light'>Come on you have a lot message</h3> <h4 class='font-light'>you can use it with the small code</h4> </div> <div class='col-md-6 text-right'> <p>Donec pede justo, fringilla vel, aliquet nec, vulputate eget, arcu. In enim justo, rhoncus ut, imperdiet a.</p> </div> </div> </div> <div class='tab-pane fade' id='delete' aria-labelledby='delete-tab' role='tabpanel'> <div class='row p-3 text-dark'> <div class='col-md-6'> <h3 class='font-light'>Just do Settings</h3> <h4 class='font-light'>you can use it with the small code</h4> </div> <div class='col-md-6 text-right'> <p>Donec pede justo, fringilla vel, aliquet nec, vulputate eget, arcu. In enim justo, rhoncus ut, imperdiet a.</p> </div> </div> </div> </div> </div> </div> </div> </div> <style type='text/css'> body{ background: #edf1f5; margin-top:20px; } .card { position: relative; display: flex; flex-direction: column; min-width: 0; word-wrap: break-word; background-color: #fff; background-clip: border-box; border: 0 solid transparent; border-radius: 0; } .mailbox-widget .custom-tab .nav-item .nav-link { border: 0; color: #fff; border-bottom: 3px solid transparent; } .mailbox-widget .custom-tab .nav-item .nav-link.active { background: 0 0; color: #fff; border-bottom: 3px solid #2cd07e; } .no-wrap td, .no-wrap th { white-space: nowrap; } .table td, .table th { padding: .9375rem .4rem; vertical-align: top; border-top: 1px solid rgba(120,130,140,.13); } .font-light { font-weight: 300; } </style>");
			out.println("</form></body></html>");
		} catch(Exception e) {
			out.println(e.toString());
		}
	}
}