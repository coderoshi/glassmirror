package test.book.glass.blog.mirror;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import test.book.glass.MirrorUtils;
import test.book.glass.blog.BlogHelper;
import test.book.glass.blog.models.User;

import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.api.services.mirror.Mirror;
import com.google.api.services.mirror.model.Contact;
import com.google.api.services.mirror.model.Notification;
import com.google.api.services.mirror.model.TimelineItem;
import com.google.api.services.mirror.model.UserAction;

@SuppressWarnings("serial")
public class PostCallbackServlet extends HttpServlet
{
  protected void doPost(HttpServletRequest req, HttpServletResponse res)
      throws IOException, ServletException
  {
    // Generate Notification from request body
    JsonFactory jsonFactory = new JacksonFactory();
    Notification notification =
      jsonFactory.fromInputStream( req.getInputStream(), Notification.class );

    // START:cb1
    // Get this user action's type
    UserAction launchAction = null;
    for (UserAction userAction : notification.getUserActions()) {
      if( "LAUNCH".equals( userAction.getType() ) ) {
        launchAction = userAction;
        break;
      }
    }
    // return, because this is the wrong kind of user action 
    if( launchAction == null ) return;
    // END:cb1

    // START:cb2
    String userId = notification.getUserToken();
    String itemId = notification.getItemId();

    Mirror mirror = MirrorUtils.getMirror( userId );
    TimelineItem timelineItem = mirror.timeline().get( itemId ).execute();

    // ensure that ChittrChattr is one of this post's intended recipients
    boolean belongsHere = false;
    for( Contact contact : timelineItem.getRecipients() ) {
      if( "chittrchattr".equals( contact.getId() ) ) {
        belongsHere = true;
        break;
      }
    }
    // return, because this post wasn't intended for ChittrChattr
    if( !belongsHere ) return;
    // END:cb2

    // at this point we have the body text, and the user intended us to have it

    // START:cb3
    String body = timelineItem.getText();

    User user = User.get( userId );

    BlogHelper.createPost( null, body, user );
    // END:cb3
  }
}
