package test.book.glass;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.api.services.mirror.Mirror.Timeline;
import com.google.api.services.mirror.model.TimelineItem;

@SuppressWarnings("serial")
public class LunchRouletteServlet extends HttpServlet
{
  // START:doget
  public void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws IOException, ServletException
  {
    ServletContext ctx = getServletContext();
    
    String userId = SessionUtils.getUserId( req );
    TimelineItem timelineItem = LunchRoulette.getLastSavedTimelineItem(userId);

    // If it exists, isn't deleted, and is pinned, then update
    if (timelineItem != null
        && !(timelineItem.getIsDeleted() != null && timelineItem.getIsDeleted())
        && (timelineItem.getIsPinned() != null && timelineItem.getIsPinned()))
    {
      String html = LunchRoulette.renderRandomCuisine( ctx );
      timelineItem.setHtml( html );

      // update the old timeline item
      Timeline timeline = MirrorUtils.getMirror( userId ).timeline();
      timeline.patch( timelineItem.getId(), timelineItem ).execute();
    }
    // Otherwise, create a new one
    else {
      LunchRoulette.insertAndSaveSimpleHtmlTimelineItem( ctx, userId );
    }

    resp.setContentType("text/plain");
    resp.getWriter().append( "Inserted Timeline Item" );
  }
  // END:doget
}
