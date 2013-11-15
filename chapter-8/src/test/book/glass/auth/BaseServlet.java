package test.book.glass.auth;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;

import test.book.glass.ViewUtils;

@SuppressWarnings("serial")
public class BaseServlet extends HttpServlet
{
  protected void render( HttpServletResponse resp, String template, Object data )
    throws IOException, ServletException
  {
    String output = ViewUtils.render(getServletContext(), template, data);
    resp.setContentType("text/html");
    resp.getWriter().append( output );
  }
}
