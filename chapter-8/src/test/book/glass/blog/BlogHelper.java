package test.book.glass.blog;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;

import test.book.glass.MirrorUtils;
import test.book.glass.blog.models.Blog;
import test.book.glass.blog.models.Post;
import test.book.glass.blog.models.User;

import com.google.api.services.mirror.Mirror;
import com.google.api.services.mirror.model.TimelineItem;

public final class BlogHelper
{
  public static final Post createPost( String title, String body, User user )
      throws ServletException, IOException
  {
    return createPost(title, body, user, user.getBlog());
  }

  // START:createPost
  public static final Post createPost( String title, String body,
                                       User user, Blog blog )
    throws ServletException, IOException
  {
    if( user == null || !user.ownsBlog( blog )) {
      throw new ServletException(
          "You must be logged and own this blog to create a post" );
    }
    
    Post post = new Post( blog.getNickname(), title, body, new Date() );
    post = post.save();
    
    pushPost( blog, post );
    
    return post;
  }
  // END:createPost

  /**
   * Create a timeline item for every follower of this blog
   */
  // START:pushPost
  private static void pushPost( Blog blog, Post post )
    throws IOException
  {
    // find every user that follows that nickname
    List<User> users = blog.getFollowers();

    for (User user : users) {
      String userId = user.getId();
      Mirror mirror = MirrorUtils.getMirror( userId );
      TimelineItem timelineItem = new TimelineItem()
        .setText( post.getBody() );
      mirror.timeline().insert( timelineItem ).execute();
    }
  }
  // END:pushPost
}
