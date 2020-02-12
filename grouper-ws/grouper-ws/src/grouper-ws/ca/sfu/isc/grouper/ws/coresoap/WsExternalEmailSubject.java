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

import edu.internet2.middleware.grouper.util.GrouperUtil;
import edu.internet2.middleware.grouper.ws.exceptions.WsInvalidQueryException;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Result of one external Email subject being retrieved.
 * 
 * @author Steve Hillman (hillman@sfu.ca)
 */
public class WsExternalEmailSubject implements Comparable<WsExternalEmailSubject> {

  /**
   * make sure this is an explicit toString
   */
  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

  /** 
   * description, which is generated from other attributes 
   */
  private String description;

  /** email address */
  private String mail;

  /** name of subject */
  private String givenName;

  private String surname;


  public String getMail() {
    return mail;
  }

  public void setMail(String mail) {
    this.mail = mail;
  }

  public String getGivenName() {
    return givenName;
  }

  public void setGivenName(String givenName) {
    this.givenName = givenName;
  }

  public String getSurname() {
    return surname;
  }

  public void setSurname(String surname) {
    this.surname = surname;
  }

  /**
   * no arg constructor
   */
  public WsExternalEmailSubject() {
    //blank

  }

  /**
   * construct based on external email, assign all fields
   * @param mail Email address of external user
   * @param givenName Given name of external user (may be null)
   * @param surname Surname of external user (may be null),.l,
   */
  public WsExternalEmailSubject(String mail, String givenName, String surname) throws WsInvalidQueryException {
    if (mail != null && mail.contains("@") && !mail.endsWith("@sfu.ca")) {
      this.setMail(StringUtils.trimToNull(mail));
      this.setGivenName(StringUtils.trimToNull(givenName));
      this.setSurname(StringUtils.trimToNull(surname));
    } else throw new WsInvalidQueryException("Invalid External Email address");
  }

  /**
   * description, which is generated from other attributes 
   * @return the description
   */
  public String getDescription() {
    return this.description;
  }

  /**
   * name of subject   
   * @return the name
   */
  public String getName() {
    return this.mail;
  }

  /**
   * @see java.lang.Comparable#compareTo(java.lang.Object)
   */
  @Override
  public int compareTo(WsExternalEmailSubject o2) {
    if (this == o2) {
      return 0;
    }
    //lets by null safe here
    if (this == null) {
      return -1;
    }
    if (o2 == null) {
      return 1;
    }
    return GrouperUtil.compare(this.getName(), o2.getName());
  }
}
