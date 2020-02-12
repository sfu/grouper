/*******************************************************************************
 * Copyright 2016 Internet2
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
/**
 * 
 */
package ca.sfu.isc.grouper.ws.coresoap;

import edu.internet2.middleware.grouper.exception.InsufficientPrivilegeException;
import edu.internet2.middleware.grouper.misc.GrouperVersion;
import edu.internet2.middleware.grouper.ws.ResultMetadataHolder;
import edu.internet2.middleware.grouper.ws.coresoap.WsResultMeta;
import edu.internet2.middleware.grouper.ws.exceptions.WsInvalidQueryException;
import edu.internet2.middleware.grouper.ws.util.GrouperServiceUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Result of one external being saved.  The number of
 * these result objects will equal the number of external subjects sent in to the method
 * to be saved
 * 
 * @author mchyzer
 */
public class WsExternalEmailSubjectSaveResult implements ResultMetadataHolder {

  /**
   * logger 
   */
  private static final Log LOG = LogFactory.getLog(WsExternalEmailSubjectSaveResult.class);

  /** external subject saved */
  private WsExternalEmailSubject wsExternalSubject;
  
  /**
   * metadata about the result
   */
  private WsResultMeta resultMetadata = new WsResultMeta();

  /**
   * result code of a request
   */
  public static enum WsExternalEmailSubjectSaveResultCode {

    /** successful addition (success: T) */
    SUCCESS_INSERTED {

    },

    /** successful addition (success: T) */
    SUCCESS_UPDATED {

    },

    /** successful addition (success: T) */
    SUCCESS_NO_CHANGES_NEEDED {

    },

    /** invalid query, can only happen if Lite query (success: F) */
    INVALID_QUERY {
      
    },

    /** the external subject was not found (success: F) */
    EXTERNAL_SUBJECT_NOT_FOUND {
      
    },

    /** problem with saving (success: F) */
    EXCEPTION {
      
    },

    /** problem with saving (success: F) */
    EXTERNAL_SUBJECT_ALREADY_EXISTS {
      
    },

    /** user not allowed (success: F) */
    INSUFFICIENT_PRIVILEGES {
      
    }, 
    
    /** was a success but rolled back */
    TRANSACTION_ROLLED_BACK {
    
    };

    /**
     * if this is a successful result
     * @return true if success
     */
    public boolean isSuccess() {
      return this.name().startsWith("SUCCESS");
    }

    /** get the name label for a certain version of client 
     * @param clientVersion 
     * @return name */
    public String nameForVersion(GrouperVersion clientVersion) {

      return this.name();
    }
    

  }

  
  /**
   * assign the code from the enum
   * @param externalSubjectSaveResultCode
   * @param clientVersion 
   */
  public void assignResultCode(WsExternalEmailSubjectSaveResultCode externalSubjectSaveResultCode, GrouperVersion clientVersion) {
    this.getResultMetadata().assignResultCode(
        externalSubjectSaveResultCode == null ? null : externalSubjectSaveResultCode.nameForVersion(clientVersion));
    this.getResultMetadata().assignSuccess(
        GrouperServiceUtils.booleanToStringOneChar(externalSubjectSaveResultCode.isSuccess()));
  }

  /**
   * @return the resultMetadata
   */
  public WsResultMeta getResultMetadata() {
    return this.resultMetadata;
  }
  
  /**
   * @return the wsExternalSubject
   */
  public WsExternalEmailSubject getWsExternalEmailSubject() {
    return this.wsExternalSubject;
  }

  
  /**
   * @param wsExternalSubject1 the wsExternalSubject to set
   */
  public void setWsExternalEmailSubject(WsExternalEmailSubject wsExternalSubject1) {
    this.wsExternalSubject = wsExternalSubject1;
  }

  
  /**
   * @param resultMetadata1 the resultMetadata to set
   */
  public void setResultMetadata(WsResultMeta resultMetadata1) {
    this.resultMetadata = resultMetadata1;
  }

  /**
   * empty
   */
  public WsExternalEmailSubjectSaveResult() {
    //empty
  }

  /**
   * assign a resultcode of exception, and process/log the exception
   * @param e
   * @param wsExternalSubject
   * @param clientVersion 
   */
  public void assignResultCodeException(Exception e, WsExternalEmailSubject wsExternalSubject, GrouperVersion clientVersion) {
    
    //get root exception (might be wrapped in wsInvalidQuery)
    Throwable mainThrowable = (e instanceof WsInvalidQueryException 
        && e.getCause() != null) ? e.getCause() : e;
    
    if (mainThrowable instanceof InsufficientPrivilegeException) {
      this.getResultMetadata().setResultMessage(mainThrowable.getMessage());
      this.assignResultCode(WsExternalEmailSubjectSaveResultCode.INSUFFICIENT_PRIVILEGES, clientVersion);
      
//    } else if (mainThrowable  instanceof GroupNotFoundException) {
//      this.getResultMetadata().setResultMessage(mainThrowable.getMessage());
//      this.assignResultCode(WsExternalSubjectSaveResultCode.EXTERNAL_SUBJECT_NOT_FOUND, clientVersion);
    } else if (e  instanceof WsInvalidQueryException) {
      this.getResultMetadata().setResultMessage(mainThrowable.getMessage());
      this.assignResultCode(WsExternalEmailSubjectSaveResultCode.INVALID_QUERY, clientVersion);
//    } else if (mainThrowable!= null && (mainThrowable instanceof GroupAddAlreadyExistsException
//        || mainThrowable.getCause() instanceof GroupAddAlreadyExistsException)) {
//      this.getResultMetadata().setResultMessage(mainThrowable.getMessage());
//      this.assignResultCode(WsExternalSubjectSaveResultCode.EXTERNAL_SUBJECT_ALREADY_EXISTS, clientVersion);
    } else {
      this.getResultMetadata().setResultMessage(ExceptionUtils.getFullStackTrace(e));
      this.assignResultCode(WsExternalEmailSubjectSaveResultCode.EXCEPTION, clientVersion);
    }
    LOG.error(wsExternalSubject + ", " + e, e);
  }

  /**
   * convert string to result code
   * @return the result code
   */
  public WsExternalEmailSubjectSaveResultCode resultCode() {
    return WsExternalEmailSubjectSaveResultCode.valueOf(this.getResultMetadata().getResultCode());
  }

}
