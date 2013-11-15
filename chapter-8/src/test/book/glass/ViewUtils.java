package test.book.glass;

import java.io.IOException;
import java.io.StringWriter;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public final class ViewUtils
{
  public static String render(ServletContext ctx, String template, Object data)
      throws IOException, ServletException
  {
    Configuration config = new Configuration();
    config.setServletContextForTemplateLoading(ctx, "WEB-INF/views");
    config.setDefaultEncoding("UTF-8");
    Template ftl = config.getTemplate(template);
    try {
      // use the data to render the template to the servlet output
      StringWriter writer = new StringWriter();
      ftl.process(data, writer);
      return writer.toString();
    }
    catch (TemplateException e) {
      throw new ServletException("Problem while processing template", e);
    }
  }

  public static int getPage( HttpServletRequest req )
  {
    String pageStr = req.getParameter("page");
    int pageNum = 1;
    try {
      pageNum = Integer.parseInt( pageStr );
    } catch(NumberFormatException e) {
    }
    return pageNum;
  }

  public static String extractFromPath( HttpServletRequest req )
  {
    String nickname = req.getPathInfo();
    if( nickname == null ) nickname = "";
    return nickname.substring( nickname.indexOf('/') + 1 );
  }
}
