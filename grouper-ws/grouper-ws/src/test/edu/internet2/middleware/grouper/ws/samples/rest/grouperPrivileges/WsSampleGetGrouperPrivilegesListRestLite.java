/*******************************************************************************
 * Copyright 2012 Internet2
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package edu.internet2.middleware.grouper.ws.samples.rest.grouperPrivileges;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.DefaultHttpParams;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.lang.StringUtils;

import edu.internet2.middleware.grouper.Group;
import edu.internet2.middleware.grouper.GroupFinder;
import edu.internet2.middleware.grouper.GrouperSession;
import edu.internet2.middleware.grouper.SubjectFinder;
import edu.internet2.middleware.grouper.util.GrouperUtil;
import edu.internet2.middleware.grouper.ws.coresoap.WsGetGrouperPrivilegesLiteResult;
import edu.internet2.middleware.grouper.ws.coresoap.WsGrouperPrivilegeResult;
import edu.internet2.middleware.grouper.ws.rest.group.WsRestGetGrouperPrivilegesLiteRequest;
import edu.internet2.middleware.grouper.ws.samples.types.WsSampleRest;
import edu.internet2.middleware.grouper.ws.samples.types.WsSampleRestType;
import edu.internet2.middleware.grouper.ws.util.RestClientSettings;

/**
 * @author mchyzer
 */
public class WsSampleGetGrouperPrivilegesListRestLite implements WsSampleRest {

  /**
   * get grouper privileges lite web service with REST
   * @param wsSampleRestType is the type of rest (xml, xhtml, etc)
   */
  @SuppressWarnings("deprecation")
  public static void getGrouperPrivilegesListLite(WsSampleRestType wsSampleRestType) {

    try {
      
      HttpClient httpClient = new HttpClient();
      
      DefaultHttpParams.getDefaultParams().setParameter(
          HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(0, false));
      
      //URL e.g. http://localhost:8093/grouper-ws/servicesRest/v1_3_000/...
      //NOTE: aStem:aGroup urlencoded substitutes %3A for a colon
      PostMethod method = new PostMethod(
          RestClientSettings.URL + "/" + RestClientSettings.VERSION  
            + "/grouperPrivileges");

      httpClient.getParams().setAuthenticationPreemptive(true);
      Credentials defaultcreds = new UsernamePasswordCredentials(RestClientSettings.USER, 
          RestClientSettings.PASS);
      
      //no keep alive so response if easier to indent for tests
      method.setRequestHeader("Connection", "close");
      
      //e.g. localhost and 8093
      String host = RestClientSettings.HOST;
      int port = RestClientSettings.PORT;
      httpClient.getState()
          .setCredentials(new AuthScope(host, port), defaultcreds);

      //Make the body of the request, in this case with beans and marshaling, but you can make
      //your request document in whatever language or way you want
      WsRestGetGrouperPrivilegesLiteRequest wsRestGetGrouperPrivilegesLiteRequest 
        = new WsRestGetGrouperPrivilegesLiteRequest();

      // set the act as id
      wsRestGetGrouperPrivilegesLiteRequest.setActAsSubjectId("GrouperSystem");
      
      wsRestGetGrouperPrivilegesLiteRequest.setSubjectId("test.subject.9");
      wsRestGetGrouperPrivilegesLiteRequest.setGroupName("aStem:aGroup");
      
      wsRestGetGrouperPrivilegesLiteRequest.setPrivilegeType("access");

      //get the xml / json / xhtml / paramString
      String requestDocument = wsSampleRestType.getWsLiteRequestContentType().writeString(wsRestGetGrouperPrivilegesLiteRequest);
      
      //make sure right content type is in request (e.g. application/xhtml+xml
      String contentType = wsSampleRestType.getWsLiteRequestContentType().getContentType();
      
      method.setRequestEntity(new StringRequestEntity(requestDocument, contentType, "UTF-8"));

      httpClient.executeMethod(method);

      //make sure a request came back
      Header successHeader = method.getResponseHeader("X-Grouper-success");
      String successString = successHeader == null ? null : successHeader.getValue();
      if (StringUtils.isBlank(successString)) {
        throw new RuntimeException("Web service did not even respond!");
      }
      boolean success = "T".equals(successString);
      String resultCode = method.getResponseHeader("X-Grouper-resultCode").getValue();
      
      String response = RestClientSettings.responseBodyAsString(method);

      //convert to object (from xhtml, xml, json, etc)
      WsGetGrouperPrivilegesLiteResult wsGetGrouperPrivilegesLiteResult = (WsGetGrouperPrivilegesLiteResult)wsSampleRestType
        .getWsLiteResponseContentType().parseString(response);
      
      String resultMessage = wsGetGrouperPrivilegesLiteResult.getResultMetadata().getResultMessage();

      // see if request worked or not
      if (!success) {
        throw new RuntimeException("Bad response from web service: resultCode: " + resultCode
            + ", " + resultMessage);
      }
      
      System.out.println("Server version: " + wsGetGrouperPrivilegesLiteResult.getResponseMetadata().getServerVersion()
          + ", result code: " + resultCode
          + ", result message: " + resultMessage );
      
      System.out.println(wsGetGrouperPrivilegesLiteResult);
      
      boolean foundAdmin = false;
      
      //lets make sure admin is in there
      for (WsGrouperPrivilegeResult wsGrouperPrivilegeResult : GrouperUtil.nonNull(wsGetGrouperPrivilegesLiteResult.getPrivilegeResults(), WsGrouperPrivilegeResult.class)) {
        if (StringUtils.equals("admin", wsGrouperPrivilegeResult.getPrivilegeName())
            && StringUtils.equals("T", wsGrouperPrivilegeResult.getAllowed())) {
          foundAdmin = true;
        }
          
      }
      GrouperSession grouperSession = GrouperSession.startRootSession();
      boolean shouldHaveAdmin = false;
      try {
        Group group = GroupFinder.findByName(grouperSession, "aStem:aGroup");
        shouldHaveAdmin = group.hasAdmin(SubjectFinder.findById("test.subject.9"));
      } finally {
        GrouperSession.stopQuietly(grouperSession);
      }
      if (shouldHaveAdmin != foundAdmin) {
        throw new RuntimeException("admin priv found (" + foundAdmin 
            + ") is not the same as it should be: " + shouldHaveAdmin);
      }
      
    } catch (Exception e) {
      throw new RuntimeException(e);
    }

  }

  /**
   * @param args
   */
  @SuppressWarnings("unchecked")
  public static void main(String[] args) {
    RestClientSettings.resetData();
    
    getGrouperPrivilegesListLite(WsSampleRestType.xhtml);
  }

  /**
   * @see edu.internet2.middleware.grouper.ws.samples.types.WsSampleRest#executeSample(edu.internet2.middleware.grouper.ws.samples.types.WsSampleRestType)
   */
  public void executeSample(WsSampleRestType wsSampleRestType) {
    getGrouperPrivilegesListLite(wsSampleRestType);
  }

  /**
   * @see edu.internet2.middleware.grouper.ws.samples.types.WsSampleRest#validType(edu.internet2.middleware.grouper.ws.samples.types.WsSampleRestType)
   */
  public boolean validType(WsSampleRestType wsSampleRestType) {
    return true;
  }
}
