package test.book.glass.auth;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import test.book.glass.SessionUtils;
import test.book.glass.blog.models.User;

@SuppressWarnings("serial")
public class LogoutServlet extends HttpServlet
{
  protected void doGet( HttpServletRequest req, HttpServletResponse res )
     throws ServletException, IOException
  {
    User user = SessionUtils.getUser(req);
    if( user != null ) {
      AuthUtils.deleteCredential( user.getId() );
    }
    SessionUtils.removeUser( req );
    res.sendRedirect( "/" );
  }
}
