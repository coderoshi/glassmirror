package test.book.glass.auth;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import test.book.glass.LunchRoulette;
import test.book.glass.SessionUtils;

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.AuthorizationCodeTokenRequest;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;

/**
 * URL endpoint which handles all oauth requests.
 * GET requests without a code will redirect to Google's authorization code flow.
 * GET requests with a code will 
 */
@SuppressWarnings("serial")
// START:get
public class OAuth2Servlet extends HttpServlet
{
  protected void doGet( HttpServletRequest req, HttpServletResponse res )
      throws IOException
  {
    if( !hasError(req, res) ) {
      res.sendRedirect( doAuth(req) );
    }
  }
// END:get  
  private boolean hasError( HttpServletRequest req, HttpServletResponse res )
      throws IOException
  {
    String error = req.getParameter( "error" );
    if( error != null ) {
      res.getWriter().write( "Sorry, auth failed because: " + error );
      return true;
    }
    return false;
  }
  
// START:auth
  private String doAuth(HttpServletRequest req)
      throws IOException
  {
    String authCode = req.getParameter( "code" );

    String callbackUri = AuthUtils.fullUrl( req, AuthUtils.OAUTH2_PATH );

    // We need a flow no matter what to either redirect or extract information
    AuthorizationCodeFlow flow = AuthUtils.buildCodeFlow();

    // Without a response code, redirect to Google's authorization URI
    if( authCode == null ) {
      return flow.newAuthorizationUrl().setRedirectUri( callbackUri ).build();
    }

    // With a response code, store the user's credential, and 
    // set the user's ID into the session
    GoogleTokenResponse tokenRes = getTokenRes( flow, authCode, callbackUri );

    // Extract the Google user ID from the ID token in the auth response
    String userId = getUserId( tokenRes );

    // Store the user if for the session
    SessionUtils.setUserId( req, userId );

    // START:subscribe
    // Store the credential with the user
    flow.createAndStoreCredential( tokenRes, userId );

    // successful authorization, subscribe to notifications
    LunchRoulette.subscribe( req, userId );

    return "/";
    // ENDT:subscribe
  }
// END:auth

  /**
   * Makes a remote call to the Google Auth server to authorize the grant code,
   * in order to issue a request token.
   * @param flow
   * @param code
   * @param callbackUri
   * @return
   * @throws IOException
   */
  private GoogleTokenResponse getTokenRes( AuthorizationCodeFlow flow, String code, String callbackUri ) 
      throws IOException
  {
    AuthorizationCodeTokenRequest tokenReq = flow
        .newTokenRequest( code )
        .setRedirectUri( callbackUri );

    TokenResponse tokenRes = tokenReq.execute();
    
    return (GoogleTokenResponse)tokenRes;
  }

  /**
   * Extract the Google user ID from the ID token in the auth response
   */
  private String getUserId( GoogleTokenResponse tokenRes )
      throws IOException
  {
    return tokenRes.parseIdToken().getPayload().getUserId();
  }
}