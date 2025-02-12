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
/**
 * @author mchyzer
 * $Id: SimpleMembershipUpdateImportExport.java,v 1.4 2009-10-11 22:04:17 mchyzer Exp $
 */
package edu.internet2.middleware.grouper.grouperUi.serviceLogic;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;
import edu.internet2.middleware.grouper.Group;
import edu.internet2.middleware.grouper.GroupFinder;
import edu.internet2.middleware.grouper.GrouperSession;
import edu.internet2.middleware.grouper.Member;
import edu.internet2.middleware.grouper.audit.AuditEntry;
import edu.internet2.middleware.grouper.audit.AuditTypeBuiltin;
import edu.internet2.middleware.grouper.exception.GrouperSessionException;
import edu.internet2.middleware.grouper.grouperUi.beans.api.GuiGroup;
import edu.internet2.middleware.grouper.grouperUi.beans.json.GuiHideShow;
import edu.internet2.middleware.grouper.grouperUi.beans.json.GuiResponseJs;
import edu.internet2.middleware.grouper.grouperUi.beans.json.GuiScreenAction;
import edu.internet2.middleware.grouper.grouperUi.beans.simpleMembershipUpdate.ImportSubjectWrapper;
import edu.internet2.middleware.grouper.grouperUi.beans.simpleMembershipUpdate.SimpleMembershipUpdateContainer;
import edu.internet2.middleware.grouper.grouperUi.beans.ui.TextContainer;
import edu.internet2.middleware.grouper.hibernate.AuditControl;
import edu.internet2.middleware.grouper.hibernate.GrouperTransactionType;
import edu.internet2.middleware.grouper.hibernate.HibernateHandler;
import edu.internet2.middleware.grouper.hibernate.HibernateHandlerBean;
import edu.internet2.middleware.grouper.hibernate.HibernateSession;
import edu.internet2.middleware.grouper.internal.dao.GrouperDAOException;
import edu.internet2.middleware.grouper.j2ee.GrouperRequestWrapper;
import edu.internet2.middleware.grouper.misc.GrouperSessionHandler;
import edu.internet2.middleware.grouper.subj.SubjectHelper;
import edu.internet2.middleware.grouper.ui.GrouperUiFilter;
import edu.internet2.middleware.grouper.ui.exceptions.ControllerDone;
import edu.internet2.middleware.grouper.ui.exceptions.NoSessionException;
import edu.internet2.middleware.grouper.ui.util.GrouperUiUtils;
import edu.internet2.middleware.grouper.ui.util.HttpContentType;
import edu.internet2.middleware.grouper.util.GrouperUtil;
import edu.internet2.middleware.subject.Subject;


/**
 * ajax methods for simple memebrship update import and export actions
 */
public class SimpleMembershipUpdateImportExport {

  /** logger */
  private static final Log LOG = LogFactory.getLog(SimpleMembershipUpdateImportExport.class);
  /**
   * cols (tolower) which are cols which are not attributes
   */
  private static Set<String> nonAttributeCols = GrouperUtil.toSet(
      "subjectid", "entityid", "sourceid", "memberid", "name", "description", "screenlabel");

  //moved to legacy UI fork of this class
  //public void exportAllCsv(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {

  /**
   * export all fields of a group
   * @param group
   * @param headersCommaSeparated
   * @param exportAllSortField
   * @param immediateOnly
   * @throws IOException
   */
  public static void exportGroupAllFieldsToBrowser(Group group, String headersCommaSeparated, String exportAllSortField, boolean immediateOnly) {
    
    try {
      Set<Member> members = immediateOnly ? group.getImmediateMembers() : group.getMembers();
      
      Member.resolveSubjects(members, true);
      
      HttpServletResponse response = GrouperUiFilter.retrieveHttpServletResponse(); 
      
      String[] headers = GrouperUtil.splitTrim(headersCommaSeparated, ",");
      
      //note: isError is second to last col, error is the last column
      boolean[] isAttribute = new boolean[headers.length];
      int sortCol = 0;
      int sourceIdCol = -1;
      for (int i=0;i<headers.length;i++) {
        isAttribute[i] = !nonAttributeCols.contains(headers[i].toLowerCase());
  
        if (StringUtils.equalsIgnoreCase(headers[i], exportAllSortField)) {
          sortCol = i;
        } else if (StringUtils.equalsIgnoreCase("sourceId", headers[i])) {
          sourceIdCol = i;
        }
      }
      
      List<String[]> memberData = new ArrayList<String[]>(); 
      for (Member member : members) {
        // feed in your array (or convert your data to an array)
        String[] entries = exportAllStringArray(member, headers, isAttribute);
        memberData.add(entries);
      }      
   
      final int SOURCE_ID_COL = sourceIdCol;
      final int SORT_COL = sortCol;
      //sort
      Collections.sort(memberData, new Comparator() {
   
        /**
         * 
         * @param o1
         * @param o2
         * @return 1, -1, 0
         */
        public int compare(Object o1, Object o2) {
          String[] first = (String[])o1;
          String[] second = (String[])o2;
          if (SOURCE_ID_COL != -1 && !StringUtils.equals(first[SOURCE_ID_COL], second[SOURCE_ID_COL])) {
            return GrouperUiUtils.compare(first[SOURCE_ID_COL], second[SOURCE_ID_COL], true);
          }
          return GrouperUiUtils.compare(first[SORT_COL], second[SORT_COL], true);
        }
      });
      
      //say it is CSV
      response.setContentType(HttpContentType.TEXT_CSV.getContentType());
    
      String groupExtensionFileName = GuiGroup.getExportAllFileNameStatic(group);
      
      response.setHeader ("Content-Disposition", "inline;filename=\"" + groupExtensionFileName + "\"");
      
      //just write some stuff
      PrintWriter out = null;
    
      try {
        out = response.getWriter();
      } catch (Exception e) {
        throw new RuntimeException("Cant get response.getWriter: ", e);
      }
      
      CSVWriter writer = new CSVWriter(out);
      String[] headersNew = new String[headers.length+2];
      System.arraycopy(headers, 0, headersNew, 0, headers.length);
      headersNew[headers.length] = "success";
      headersNew[headers.length+1] = "errorMessage";
      writer.writeNext(headersNew);
      for (String[] entries: memberData) {
        // feed in your array (or convert your data to an array)
        writer.writeNext(entries);
      }      
      writer.close();
      auditExport(group.getUuid(), group.getName(), memberData.size(), groupExtensionFileName);
 
    } catch (NoSessionException se) {
      throw se;
    } catch (Exception se) {
      throw new RuntimeException("Error exporting all members from group: " + group.getName() 
          + ", " + se.getMessage(), se);
    }
    throw new ControllerDone();
  }

  /**
   * export all members
   * @param member
   * @param headers 
   * @param isAttribute which indexes are attributes
   * @return the stirng array for csv
   */
  public static String[] exportAllStringArray(Member member, String[] headers, boolean[] isAttribute) {
    String[] result = new String[headers.length+2];
    
    //lets see what we can get from the member
    for (int i=0;i<headers.length;i++) {
      String header = headers[i];
      if ("subjectId".equalsIgnoreCase(header)) {
        result[i] = member.getSubjectId();
      } else if ("entityId".equalsIgnoreCase(header)) {
        result[i] = member.getSubjectId();
      } else if ("sourceId".equalsIgnoreCase(header)) {
        result[i] = member.getSubjectSourceId();
      } else if ("memberId".equalsIgnoreCase(header)) {
        result[i] = member.getUuid();
      }
    }
    
    
    try {
      
      Subject subject = member.getSubject();
      
      //lets see what we can get from the subject
      for (int i=0;i<headers.length;i++) {
        String header = headers[i];
        if ("name".equalsIgnoreCase(header)) {
          result[i] = subject.getName();
        } else if ("description".equalsIgnoreCase(header)) {
          result[i] = subject.getDescription();
        } else if ("screenLabel".equalsIgnoreCase(header)) {
          result[i] = GrouperUiUtils.convertSubjectToLabelConfigured(subject);
        } else if (isAttribute[i]) {
          result[i] = subject.getAttributeValueOrCommaSeparated(header);
        }
      }
      
      result[headers.length] = "T";
    } catch (NoSessionException se) {
      throw se;
    } catch (Exception e) {
      result[headers.length] = "F";
      String error = "error with memberId: " + member.getUuid() + ", subjectId: " + member.getSubjectId()
        + ", " + ExceptionUtils.getFullStackTrace(e);
      LOG.error(error);
      result[headers.length + 1] = error;
    }
    return result;
  }

  /**
   * export group subject ids
   * @param group
   * @param immediateOnly
   */
  public static void exportGroupSubjectIdsCsv(Group group, boolean immediateOnly) {
    
    try {
  
      Set<Member> members = immediateOnly ? group.getImmediateMembers() : group.getMembers();
      
      HttpServletResponse response = GrouperUiFilter.retrieveHttpServletResponse(); 
      
      List<String[]> memberData = new ArrayList<String[]>(); 
      for (Member member : members) {
        // feed in your array (or convert your data to an array)
        String[] entries = new String[]{member.getSubjectSourceId(), member.getSubjectId()};
        memberData.add(entries);
      }      
  
      //sort
      Collections.sort(memberData, new Comparator() {
  
        /**
         * 
         * @param o1
         * @param o2
         * @return 1, -1, 0
         */
        public int compare(Object o1, Object o2) {
          String[] first = (String[])o1;
          String[] second = (String[])o2;
          if (!StringUtils.equals(first[0], second[0])) {
            return GrouperUiUtils.compare(first[0], second[0], true);
          }
          return GrouperUiUtils.compare(first[1], second[1], true);
        }
      });
      
      //say it is CSV
      response.setContentType("text/csv");
    
      String groupExtensionFileName = GuiGroup.getExportSubjectIdsFileNameStatic(group);
      
      response.setHeader ("Content-Disposition", "inline;filename=\"" + groupExtensionFileName + "\"");
      
      //just write some stuff
      PrintWriter out = null;
    
      try {
        out = response.getWriter();
      } catch (Exception e) {
        throw new RuntimeException("Cant get response.getWriter: ", e);
      }
      
      CSVWriter writer = new CSVWriter(out);
      writer.writeNext(new String[]{"sourceId", "entityId"});
      for (String[] entries: memberData) {
        // feed in your array (or convert your data to an array)
        writer.writeNext(entries);
      }      
      writer.close();
      
      auditExport(group.getUuid(), group.getName(), memberData.size(), groupExtensionFileName);
      throw new ControllerDone();
    } catch (NoSessionException se) {
      throw se;
    } catch (ControllerDone cd) {
      throw cd;
    } catch (Exception se) {
      throw new RuntimeException("Error exporting all members from group: " + group.getName()
          + ", " + se.getMessage(), se);
    }
    
  }

	private static void auditExport(final String groupId, final String groupName, final int exportSize, final String groupExtensionFileName) {
		HibernateSession.callbackHibernateSession(
	          GrouperTransactionType.READ_WRITE_OR_USE_EXISTING, AuditControl.WILL_AUDIT,
	    	      new HibernateHandler() {
	    	          public Object callback(HibernateHandlerBean hibernateHandlerBean)
	    	              throws GrouperDAOException {
	    	        	  
	    	        	  AuditEntry auditEntry = new AuditEntry(AuditTypeBuiltin.MEMBERSHIP_GROUP_EXPORT, 
	    	        			  "exportSize", String.valueOf(exportSize), "groupId", groupId,
	    	        			  "groupName", groupName, "file", groupExtensionFileName);
	    	                    
	    	              String description = "exported : " + exportSize + " subjects from "+groupName + " in : " + groupExtensionFileName 
	    	                  + " file.";
	    	              auditEntry.setDescription(description);
	    	              auditEntry.saveOrUpdate(true);
	    	        	  
	    	        	  return null;
	    	          }
	      });
	}


  // Moved to legacy UI fork of this class
  //public void exportSubjectIdsCsv(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
  //public void importCsv(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)

  /**
   * exception from import
   */
  @SuppressWarnings("serial")
  public static class GrouperImportException extends RuntimeException {

    /**
     * 
     */
    public GrouperImportException() {
      super();
    }

    /**
     * @param message
     * @param cause
     */
    public GrouperImportException(String message, Throwable cause) {
      super(message, cause);
    }

    /**
     * @param message
     */
    public GrouperImportException(String message) {
      super(message);
    }

    /**
     * @param cause
     */
    public GrouperImportException(Throwable cause) {
      super(cause);
    }
    
    
  }
  
  /**
   * Note, this will close the reader passed in
   * @param originalReader
   * @param fileName
   * @param subjectErrors pass in a list and errors will be put in here
   * @param errorSubjectIdsOnRow is a map where the key is subjectId and the value if the line number
   * @param isFileUpload true if file upload, as opposed to a textarea
   * @return the list, never null
   * @throws GrouperImportException for messages to the screen
   */
  @SuppressWarnings("unchecked")
  public static List<Subject> parseCsvImportFile(Reader originalReader, String fileName, List<String> subjectErrors, 
      Map<String, Integer> errorSubjectIdsOnRow, boolean isFileUpload) throws GrouperImportException {
    
    //convert from CSV to 
    CSVReader reader = null;
    
    try {
      //note, the first row is the title
      List<String[]> csvEntries = null;
    
      try {
        reader = new CSVReader(originalReader);
        csvEntries = reader.readAll();
      } catch (IOException ioe) {
        throw new GrouperImportException("Error processing file: " + fileName, ioe);
      }
      
      List<Subject> uploadedSubjects = new ArrayList<Subject>();
      
      //lets get the headers
      int sourceIdColumn = -1;
      int subjectIdColumn = -1;
      int subjectIdentifierColumn = -1;
      int subjectIdOrIdentifierColumn = -1;
      
      //must have lines
      if (GrouperUtil.length(csvEntries) == 0) {
        if (isFileUpload) {
          throw new GrouperImportException(GrouperUiUtils.message("simpleMembershipUpdate.importErrorNoWrongFile"));
        }
        throw new GrouperImportException(GrouperUiUtils.message("simpleMembershipUpdate.importErrorBlankTextarea"));
      }
      
      //lets go through the headers
      String[] headers = csvEntries.get(0);
      int headerSize = headers.length;
      boolean foundHeader = false;
      for (int i=0;i<headerSize;i++) {
        if ("sourceId".equalsIgnoreCase(headers[i])) {
          foundHeader = true;
          sourceIdColumn = i;
        }
        if ("subjectId".equalsIgnoreCase(headers[i]) || "entityId".equalsIgnoreCase(headers[i])) {
          foundHeader = true;
          subjectIdColumn = i;
        }
        if ("subjectIdentifier".equalsIgnoreCase(headers[i]) || "entityIdentifier".equalsIgnoreCase(headers[i])) {
          foundHeader = true;
          subjectIdentifierColumn = i;
        }
        if ("subjectIdOrIdentifier".equalsIgnoreCase(headers[i]) || "entityIdOrIdentifier".equalsIgnoreCase(headers[i])) {
          foundHeader = true;
          subjectIdOrIdentifierColumn = i;
        }
      }
      
      //normally start on index 1, if the first row is header
      int startIndex = 1;
      
      //must pass in an id
      if (subjectIdColumn == -1 && subjectIdentifierColumn == -1 && subjectIdOrIdentifierColumn == -1) {
        if (!foundHeader && headerSize == 1) {
          //there was no header, so pretend like it was subjectIdOrIdentifier
          subjectIdOrIdentifierColumn = 0;
          startIndex = 0;
        } else {
          throw new GrouperImportException(TextContainer.retrieveFromRequest().getText().get("simpleMembershipUpdate.importErrorNoIdCol"));
        }
      }
      
      //ok, lets go through the rows, start after the headers
      for (int i=startIndex;i<csvEntries.size();i++) {
        String[] csvEntry = csvEntries.get(i);
        int row = i+1;
        
        //try catch each one and see where we get
        String subjectId = null;
        String subjectIdentifier = null;
        String subjectIdOrIdentifier = null;
        try {
          String sourceId = null;
    
          sourceId = sourceIdColumn == -1 ? null : csvEntry[sourceIdColumn]; 
          subjectId = subjectIdColumn == -1 ? null : csvEntry[subjectIdColumn]; 
          subjectIdentifier = subjectIdentifierColumn == -1 ? null : csvEntry[subjectIdentifierColumn]; 
          subjectIdOrIdentifier = subjectIdOrIdentifierColumn == -1 ? null : csvEntry[subjectIdOrIdentifierColumn]; 
          
          ImportSubjectWrapper importSubjectWrapper = 
            new ImportSubjectWrapper(row, sourceId, subjectId, subjectIdentifier, subjectIdOrIdentifier, csvEntry);
          uploadedSubjects.add(importSubjectWrapper);
          
        } catch (Exception e) {
          LOG.info(e);
          subjectErrors.add("Error on " + ImportSubjectWrapper.errorLabelForRowStatic(row, csvEntry) + ": " +    e.getMessage());
          
          String errorSubjectId = StringUtils.defaultIfEmpty(subjectId, subjectIdentifier);
          errorSubjectId = StringUtils.defaultIfEmpty(errorSubjectId, subjectIdOrIdentifier);
          
          errorSubjectIdsOnRow.put(errorSubjectId, row);
        }
      
      }
      
      return uploadedSubjects;
      
    } finally {
      if (reader != null) {
        try {
          reader.close();
        } catch (Exception e) {
          LOG.warn("error", e);
        }
      }
    }
    
  }

}
