package test.book.glass;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;

public final class SessionUtils
{
  public static String getUserId(HttpServletRequest request) {
    return (String)request.getSession().getAttribute("userId");
  }

  public static void setUserId(HttpServletRequest request, String userId) {
    request.getSession().setAttribute("userId", userId);
  }

  public static void removeUserId(HttpServletRequest request) throws IOException {
    // Remove their ID from the local session
    request.getSession().removeAttribute("userId");
  }
}
