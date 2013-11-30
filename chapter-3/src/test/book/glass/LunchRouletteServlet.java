package test.book.glass;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
//START:randomlunch
public class LunchRouletteServlet extends HttpServlet
{
  /** Accepts an HTTP GET request, and writes a random lunch type. */
  public void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws IOException, ServletException
  {
    resp.setContentType("text/html; charset=utf-8");

    Map<String, Object> data = new HashMap<String, Object>();
    data.put("food", LunchRoulette.getRandomCuisine());

    String html = LunchRoulette.render(
        getServletContext(), "web/cuisine.ftl", data);
    resp.getWriter().append(html);
  }
}
// END:randomlunch
