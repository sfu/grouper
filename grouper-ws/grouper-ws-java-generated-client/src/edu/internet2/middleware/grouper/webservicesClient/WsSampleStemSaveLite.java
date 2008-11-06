/**
 *
 */
package edu.internet2.middleware.grouper.webservicesClient;

import org.apache.axis2.client.Options;
import org.apache.axis2.transport.http.HTTPConstants;
import org.apache.axis2.transport.http.HttpTransportProperties;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;

import edu.internet2.middleware.grouper.webservicesClient.util.GeneratedClientSettings;
import edu.internet2.middleware.grouper.ws.samples.types.WsSampleGenerated;
import edu.internet2.middleware.grouper.ws.samples.types.WsSampleGeneratedType;
import edu.internet2.middleware.grouper.ws.soap.xsd.StemSaveLite;
import edu.internet2.middleware.grouper.ws.soap.xsd.WsStemSaveLiteResult;


/**
 *
 * @author mchyzer
 *
 */
public class WsSampleStemSaveLite implements WsSampleGenerated {
    /**
     * @param args
     */
    public static void main(String[] args) {
        stemSaveLite(WsSampleGeneratedType.soap);
    }

    /**
     * @see edu.internet2.middleware.grouper.ws.samples.types.WsSampleGenerated#executeSample(edu.internet2.middleware.grouper.ws.samples.types.WsSampleGeneratedType)
     */
    public void executeSample(WsSampleGeneratedType wsSampleGeneratedType) {
        stemSaveLite(wsSampleGeneratedType);
    }

    /**
     * @param wsSampleGeneratedType can run as soap or xml/http
     */
    public static void stemSaveLite(WsSampleGeneratedType wsSampleGeneratedType) {
        try {
            //URL, e.g. http://localhost:8091/grouper-ws/services/GrouperService
            GrouperServiceStub stub = new GrouperServiceStub(GeneratedClientSettings.URL);
            Options options = stub._getServiceClient().getOptions();
            HttpTransportProperties.Authenticator auth = new HttpTransportProperties.Authenticator();
            auth.setUsername(GeneratedClientSettings.USER);
            auth.setPassword(GeneratedClientSettings.PASS);
            auth.setPreemptiveAuthentication(true);

            options.setProperty(HTTPConstants.AUTHENTICATE, auth);
            options.setProperty(HTTPConstants.SO_TIMEOUT, new Integer(3600000));
            options.setProperty(HTTPConstants.CONNECTION_TIMEOUT,
                new Integer(3600000));

            StemSaveLite stemSaveLite = StemSaveLite.class.newInstance();

            //version, e.g. v1_3_000
            stemSaveLite.setClientVersion(GeneratedClientSettings.VERSION);

            stemSaveLite.setActAsSubjectId("GrouperSystem");
            stemSaveLite.setActAsSubjectIdentifier("");
            stemSaveLite.setActAsSubjectSourceId("");
            stemSaveLite.setDescription("test stem");
            stemSaveLite.setDisplayExtension("the test stem");
            stemSaveLite.setStemName("aStem:test");
            stemSaveLite.setStemUuid("");
            stemSaveLite.setSaveMode("");

            stemSaveLite.setStemLookupName("aStem:test");
            stemSaveLite.setStemLookupUuid("");

            WsStemSaveLiteResult wsStemSaveLiteResults = stub.stemSaveLite(stemSaveLite)
                                                             .get_return();

            System.out.println(ToStringBuilder.reflectionToString(
                    wsStemSaveLiteResults));
            if (!StringUtils.equals("T", 
                wsStemSaveLiteResults.getResultMetadata().getSuccess())) {
              throw new RuntimeException("didnt get success! ");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
