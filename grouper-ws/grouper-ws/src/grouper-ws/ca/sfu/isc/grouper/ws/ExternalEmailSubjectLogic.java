package ca.sfu.isc.grouper.ws;

import ca.sfu.isc.grouper.ws.coresoap.WsExternalEmailSubject;
import ca.sfu.isc.grouper.ws.coresoap.WsExternalEmailSubjectSaveResult;
import ca.sfu.isc.grouper.ws.coresoap.WsExternalEmailSubjectSaveResults;
import edu.internet2.middleware.grouper.GrouperSession;
import edu.internet2.middleware.grouper.misc.GrouperVersion;
import edu.internet2.middleware.grouper.misc.SaveResultType;
import edu.internet2.middleware.grouper.util.GrouperUtil;
import edu.internet2.middleware.grouper.ws.GrouperServiceJ2ee;
import edu.internet2.middleware.grouper.ws.GrouperWsConfig;
import edu.internet2.middleware.grouper.ws.coresoap.WsParam;
import edu.internet2.middleware.grouper.ws.coresoap.WsSubjectLookup;
import edu.internet2.middleware.grouper.ws.util.GrouperServiceUtils;
import edu.internet2.middleware.grouper.ws.util.GrouperWsLog;
import edu.internet2.middleware.grouper.ws.util.GrouperWsVersionUtils;
import edu.internet2.middleware.grouperClientExt.org.apache.commons.lang3.StringUtils;
import net.unicon.grouper.externalusers.data.GrouperDataAccess;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Map;

// From the Unicon grouper-external-users plugin

public class ExternalEmailSubjectLogic {
    /**
     * logger
     */
    @SuppressWarnings("unused")
    private static final Log LOG = LogFactory.getLog(ExternalEmailSubjectLogic.class);


    /**
       * save an external subject (insert or update).
       *
       * @param clientVersion is the version of the client.  Must be in GrouperWsVersion, e.g. v1_3_000
       * @param wsExternalEmailSubjects
       *            external subjects to save
       * @param actAsSubjectLookup
       * @param params optional: reserved for future use
       * @since 2.3.0.patch
       * @return the results
       */
      @SuppressWarnings("unchecked")
      public static WsExternalEmailSubjectSaveResults externalSubjectSave(final GrouperVersion clientVersion,
                                  final WsExternalEmailSubject[] wsExternalEmailSubjects, final WsSubjectLookup actAsSubjectLookup,
                                  final WsParam[] params) {

          Map<String, Object> debugMap = GrouperServiceJ2ee.retrieveDebugMap();

          final WsExternalEmailSubjectSaveResults wsExternalEmailSubjectSaveResults = new WsExternalEmailSubjectSaveResults();

          GrouperSession session = null;
          String theSummary = null;

          GrouperWsVersionUtils.assignCurrentClientVersion(clientVersion, wsExternalEmailSubjectSaveResults.getResponseMetadata().warnings());

          theSummary = "clientVersion: " + clientVersion + ", wsExternalEmailSubjectToSaves: "
              + GrouperUtil.toStringForLog(wsExternalEmailSubjects, 200) + "\n, actAsSubject: "
              + actAsSubjectLookup + ", paramNames: "
              + "\n, params: " + GrouperUtil.toStringForLog(params, 200);

          GrouperWsLog.addToLogIfNotBlank(debugMap, "method", "externalEmailSubjectSave");

          GrouperWsLog.addToLogIfNotBlank(debugMap, "actAsSubjectLookup", actAsSubjectLookup);
          GrouperWsLog.addToLogIfNotBlank(debugMap, "clientVersion", clientVersion);
          GrouperWsLog.addToLogIfNotBlank(debugMap, "params", params);
          GrouperWsLog.addToLogIfNotBlank(debugMap, "wsExternalSubjects", wsExternalEmailSubjects);

          //start session based on logged in user or the actAs passed in
          session = GrouperServiceUtils.retrieveGrouperSession(actAsSubjectLookup);

          final GrouperSession SESSION = session;

          final String THE_SUMMARY = theSummary;

          //convert the options to a map for easy access, and validate them
          @SuppressWarnings("unused")
          final Map<String, String> paramMap = GrouperServiceUtils.convertParamsToMap(params);

          int wsExternalSubjectsLength = GrouperServiceUtils.arrayLengthAtLeastOne(wsExternalEmailSubjects, GrouperWsConfig.WS_GROUP_SAVE_MAX, 1000000, "groupsToSave");

          wsExternalEmailSubjectSaveResults.setResults(new WsExternalEmailSubjectSaveResult[wsExternalSubjectsLength]);

          int resultIndex = 0;

          SaveResultType saveResultType = SaveResultType.NO_CHANGE;

          //loop through all externalSubjects and do the save
          for (WsExternalEmailSubject wsExternalEmailSubject : wsExternalEmailSubjects) {
            final WsExternalEmailSubjectSaveResult wsExternalEmailSubjectSaveResult = new WsExternalEmailSubjectSaveResult();
            wsExternalEmailSubjectSaveResults.getResults()[resultIndex++] = wsExternalEmailSubjectSaveResult;
            try {
              // Check if this should be an insert or update
              if (GrouperDataAccess.externalUserExists(wsExternalEmailSubject.getMail())) {
                // already exists. Do an update if necessary
                if (StringUtils.isNotBlank(wsExternalEmailSubject.getGivenName()) && StringUtils.isNotBlank(wsExternalEmailSubject.getSurname())) {
                  // First and last name were supplied. Update them
                  GrouperDataAccess.updateExternalUser(wsExternalEmailSubject.getMail(), wsExternalEmailSubject.getGivenName(),
                          wsExternalEmailSubject.getSurname(), SESSION.getSubject().getId());
                  saveResultType = SaveResultType.UPDATE;
                }
              } else {
                // Insert
                GrouperDataAccess.createExternalUser(wsExternalEmailSubject.getMail(), wsExternalEmailSubject.getGivenName(),
                        wsExternalEmailSubject.getSurname(), SESSION.getSubject().getId());
                saveResultType = SaveResultType.INSERT;
              }
              wsExternalEmailSubjectSaveResult.setWsExternalEmailSubject(wsExternalEmailSubject);

              if (saveResultType == SaveResultType.INSERT) {
                wsExternalEmailSubjectSaveResult.assignResultCode(WsExternalEmailSubjectSaveResult.WsExternalEmailSubjectSaveResultCode.SUCCESS_INSERTED, clientVersion);
              } else if (saveResultType == SaveResultType.UPDATE) {
                wsExternalEmailSubjectSaveResult.assignResultCode(WsExternalEmailSubjectSaveResult.WsExternalEmailSubjectSaveResultCode.SUCCESS_UPDATED, clientVersion);
              } else {
                wsExternalEmailSubjectSaveResult.assignResultCode(WsExternalEmailSubjectSaveResult.WsExternalEmailSubjectSaveResultCode.SUCCESS_NO_CHANGES_NEEDED, clientVersion);
              }
            } catch (Exception e) {
              wsExternalEmailSubjectSaveResult.assignResultCodeException(e, wsExternalEmailSubject, clientVersion);
            }
          }

          //see if any inner failures cause the whole tx to fail, and/or change the outer status
          if (!wsExternalEmailSubjectSaveResults.tallyResults(THE_SUMMARY, clientVersion)) {
              // Log something?
          }

          GrouperWsLog.addToLogIfNotBlank(debugMap, "resultsSize", wsExternalEmailSubjectSaveResults == null ? 0 : GrouperUtil.length(wsExternalEmailSubjectSaveResults.getResults()));

          //this should be the first and only return, or else it is exiting too early
          return wsExternalEmailSubjectSaveResults;
      }


}