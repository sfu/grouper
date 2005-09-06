/*
 * Copyright (C) 2004-2005 University Corporation for Advanced Internet Development, Inc.
 * Copyright (C) 2004-2005 The University Of Chicago
 * All Rights Reserved. 
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 *  * Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *  * Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *  * Neither the name of the University of Chicago nor the names
 *    of its contributors nor the University Corporation for Advanced
 *   Internet Development, Inc. may be used to endorse or promote
 *   products derived from this software without explicit prior
 *   written permission.
 *
 * You are under no obligation whatsoever to provide any enhancements
 * to the University of Chicago, its contributors, or the University
 * Corporation for Advanced Internet Development, Inc.  If you choose
 * to provide your enhancements, or if you choose to otherwise publish
 * or distribute your enhancements, in source code form without
 * contemporaneously requiring end users to enter into a separate
 * written license agreement for such enhancements, then you thereby
 * grant the University of Chicago, its contributors, and the University
 * Corporation for Advanced Internet Development, Inc. a non-exclusive,
 * royalty-free, perpetual license to install, use, modify, prepare
 * derivative works, incorporate into the software or other computer
 * software, distribute, and sublicense your enhancements or derivative
 * works thereof, in binary and source code form.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND WITH ALL FAULTS.  ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING,
 * BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE, AND NON-INFRINGEMENT ARE DISCLAIMED AND the
 * entire risk of satisfactory quality, performance, accuracy, and effort
 * is with LICENSEE. IN NO EVENT SHALL THE COPYRIGHT OWNER, CONTRIBUTORS,
 * OR THE UNIVERSITY CORPORATION FOR ADVANCED INTERNET DEVELOPMENT, INC.
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR
 * BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR
 * OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OR DISTRIBUTION OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package edu.internet2.middleware.grouper;


import  edu.internet2.middleware.subject.*;
import  edu.internet2.middleware.subject.provider.*;
import  java.util.*;
import  net.sf.hibernate.*;
import  org.apache.commons.logging.Log;
import  org.apache.commons.logging.LogFactory;


/** 
 * Use Grouper's Groups Registry as an I2MI Subject API source.
 * <p />
 * <p>
 * This is an adapter for subjects of type <i>group</i>.  It allows
 * groups (within a Grouper Groups Registry) to be used as I2MI
 * Subjects.
 * </p>
 * <p>
 * To use, add the following to your <i>sources.xml</i> file:
 * </p>
 * <pre class="eg">
 * &lt;source adapterClass="edu.internet2.middleware.grouper.GrouperSourceAdapter"&gt;
 *   &lt;id&gt;grouperAdapter&lt;/id&gt;
 *   &lt;name&gt;Grouper Adapter&lt;/name&gt;
 *   &lt;type&gt;group&lt;/type&gt;
 * &lt;/source&gt;
 * </pre>
 *
 * @author  blair christensen.
 * @version $Id: GrouperSourceAdapter.java,v 1.14 2005-09-06 18:31:30 blair Exp $
 */
public class GrouperSourceAdapter extends BaseSourceAdapter {

  /*
   * PRIVATE CLASS VARIABLES
   */
  private static Log log = LogFactory.getLog(GrouperSourceAdapter.class);


  /*
   * PRIVATE INSTANCE VARIABLES
   */
  private GrouperSession s = null;


  /*
   * CONSTRUCTORS
   */

  /**
   * Allocates new GrouperSourceAdapter.
   */
  public GrouperSourceAdapter() {
    super();
  }

  /**
   * Allocates new GrouperSourceAdapter.
   */
  public GrouperSourceAdapter(String id, String name) {
    super(id, name);
  }


  /*
   * PUBLIC INSTANCE METHODS
   */

  /**
   * Gets a {@link GrouperGroup} subject by its GUID.
   * <p />
   * <pre class="eg">
   * // Use it within the Grouper API
   * try {
   *   Subject subj = SubjectFactory.getSubject(guid, "group");
   * } catch (SubjectNotFoundException e) {
   *   // Subject not found
   * }
   *
   * // Use it directly
   * try {
   *   Subject subj = source.getSubject(guid, "group");
   * } catch (SubjectNotFoundException e) {
   *   // Subject not found
   * }
   * </pre>
   * @param   id  Group GUID
   * @return  A {@link Subject}
   * @throws  SubjectNotFoundException
   */
  public Subject getSubject(String id) 
    throws SubjectNotFoundException 
  {
    GrouperGroup g = GrouperGroup.loadByID(
      GrouperSession.getRootSession(), id
    );
    if (g != null) { // TODO GroupNotFoundException
      Subject subj = new GrouperSubject(g, this); 
      log.debug("Found subject: " + id + ": " + subj);
      return subj;
    }
    String msg = "Unable to find subject: " + id;
    log.debug(msg);
    throw new SubjectNotFoundException(msg);
  }

  /**
   * Gets a {@link GrouperGroup} subject by its name.
   * <p />
   * <pre class="eg">
   * // Use it within the Grouper API
   * try {
   *   Subject subj = SubjectFactory.getSubjectByIdentifier(name, "group");
   * } catch (SubjectNotFoundException e) {
   *   // Subject not found
   * }
   *
   * // Use it directly
   * try {
   *   Subject subj = source.getSubjectByIdentifier(name, "group");
   * } catch (SubjectNotFoundException e) {
   *   // Subject not found
   * }
   * </pre>
   * @param   name  Group name
   * @return  A {@link Subject}
   * @throws  SubjectNotFoundException
   */
  public Subject getSubjectByIdentifier(String name) 
    throws SubjectNotFoundException 
  {
    GrouperGroup g = GrouperGroup.loadByName(
      GrouperSession.getRootSession(), name
    );
    if (g != null) { // TODO GroupNotFoundException
      Subject subj = new GrouperSubject(g, this); 
      log.debug("Found subject: " + name + ": " + subj);
      return subj;
    }
    String msg = "Unable to find subject: " + name;
    log.debug(msg);
    throw new SubjectNotFoundException(msg);
  }

  /** 
   * Initializes the {@link Grouper} source adapter.
   * <p />
   * <p>
   * No initialization is currently performed by this adapter.
   * </p>
   * @throws  SourceUnavailableException
   */
  public void init() throws SourceUnavailableException {
    // DESIGN What initialization should I be doing?
    log.info("Initializing GrouperSourceAdapter");
  }

  /**
   * Searches for {@link GrouperGroup} subjects by naming attributes.
   * <p />
   * <p>
   * This method performs a fuzzy search on the <i>stem</i>,
   * <i>extension</i>, <i>displayExtension</i>, <i>name</i> and
   * <i>displayName</i> group attributes.
   * </p>
   * <pre class="eg">
   * // Use it within the Grouper API
   * Set subjects = SubjectFactory.search("admins");
   *
   * // Use it directly
   * Set subjects = source.search("admins");
   * </pre>
   */
  // TODO Is this the right search?
  // TODO ideally this query could be moved to GrouperQuery
  // TODO There is overlap in code between this and sBID
  public Set search(String searchValue) {
    String  qry   = "Group.subject.search";
    Set     vals  = new HashSet();
    try {
      Query q = GrouperSession.getRootSession().
                  dbSess().session().getNamedQuery(qry);
      // TODO Move _%_ to _Grouper.hbm.xml_
      q.setString(0, "%" + searchValue + "%"); // _stem_
      // TODO Move _%_ to _Grouper.hbm.xml_
      q.setString(1, "%" + searchValue + "%"); // _extension_
      // TODO Move _%_ to _Grouper.hbm.xml_
      q.setString(2, "%" + searchValue + "%"); // _displayextension_
      // TODO Move _%_ to _Grouper.hbm.xml_
      q.setString(3, "%" + searchValue + "%"); // _name_
      // TODO Move _%_ to _Grouper.hbm.xml_
      q.setString(4, "%" + searchValue + "%"); // _displayname_
      try {
        Iterator iter = q.list().iterator();
        while (iter.hasNext()) {
          try {
            String key = (String) iter.next();
            GrouperGroup g = (GrouperGroup) GrouperGroup.loadByKey(
              GrouperSession.getRootSession(), key
            );
            Subject subj = new GrouperSubject(g, this);
            vals.add(subj);
            log.debug("search found: " + g + "/" + subj);
          } catch (InsufficientPrivilegeException e) {
            // Ignore
          }
        }
      } catch (HibernateException e) {
        throw new RuntimeException(
          "Error retrieving results for " + qry + ": " + e.getMessage()
        );
      }
    } catch (HibernateException e) {
      throw new RuntimeException(
        "Unable to get query " + qry + ": " + e.getMessage()
      );
    }
    log.debug("search results: " + vals.size());
    return vals;
  }

}

