package test.book.glass.auth;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import test.book.glass.SessionUtils;

public class AuthFilter implements Filter
{
  public void doFilter(ServletRequest req, ServletResponse res, FilterChain fc)
    throws IOException, ServletException
  {
    HttpServletRequest request = (HttpServletRequest)req;
    HttpServletResponse response = (HttpServletResponse)res;

    // If this path is not destined for a redirectable URI
    // and has no access token, redirect to the oauth2 path
    if (isRedirectable(request)
        && !AuthUtils.hasAccessToken(SessionUtils.getUserId(request)))
    {
      response.sendRedirect( AuthUtils.OAUTH2_PATH );
      return;
    }

    // execute any remaining filters
    fc.doFilter( request, response );
  }

  private boolean isRedirectable(HttpServletRequest request) {
    return !request.getRequestURI().contains( AuthUtils.OAUTH2_PATH )
        && !request.getRequestURI().equals( "/" )
        && !request.getRequestURI().startsWith( "/blog" )
        && !request.getRequestURI().startsWith( "/posts" );
  }

  public void init(FilterConfig fc) throws ServletException {}
  public void destroy() {}
}
