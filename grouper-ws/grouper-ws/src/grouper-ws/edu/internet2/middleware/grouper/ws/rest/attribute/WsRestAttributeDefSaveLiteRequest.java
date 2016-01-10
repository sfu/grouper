/*******************************************************************************
 * Copyright 2012 Internet2
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under
 * the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 ******************************************************************************/
/**
 * @author vsachdeva $Id$
 */
package edu.internet2.middleware.grouper.ws.rest.attribute;

import edu.internet2.middleware.grouper.ws.rest.WsRequestBean;
import edu.internet2.middleware.grouper.ws.rest.method.GrouperRestHttpMethod;

/**
 * request bean in body of rest request
 */
public class WsRestAttributeDefSaveLiteRequest implements WsRequestBean {

  /**
   * is the GrouperTransactionType for the request.  If blank, defaults to
   * NONE (will finish as much as possible).  Generally the only values for this param that make sense
   * are NONE (or blank), and READ_WRITE_NEW.
   * @return txType
   */
  public String getTxType() {
    return this.txType;
  }

  /**
   * is the GrouperTransactionType for the request.  If blank, defaults to
   * NONE (will finish as much as possible).  Generally the only values for this param that make sense
   * are NONE (or blank), and READ_WRITE_NEW.
   * @param txType1
   */
  public void setTxType(String txType1) {
    this.txType = txType1;
  }

  /**
   * is the GrouperTransactionType for the request.  If blank, defaults to
   * NONE (will finish as much as possible).  Generally the only values for this param that make sense
   * are NONE (or blank), and READ_WRITE_NEW.
   */
  private String txType;

  /**
   * @see edu.internet2.middleware.grouper.ws.rest.WsRequestBean#retrieveRestHttpMethod()
   */
  @Override
  public GrouperRestHttpMethod retrieveRestHttpMethod() {
    return GrouperRestHttpMethod.PUT;
  }

  /** is the version of the client.  Must be in GrouperWsVersion, e.g. v1_3_000 */
  private String clientVersion;

  /**
   * is the version of the client.  Must be in GrouperWsVersion, e.g. v1_3_000
   * @return version
   */
  public String getClientVersion() {
    return this.clientVersion;
  }

  /**
   * is the version of the client.  Must be in GrouperWsVersion, e.g. v1_3_000
   * @param clientVersion1
   */
  public void setClientVersion(String clientVersion1) {
    this.clientVersion = clientVersion1;
  }

  /** if acting as another user */
  private String actAsSubjectId;

  /** if acting as another user */
  private String actAsSubjectIdentifier;

  /** if acting as another user */
  private String actAsSubjectSourceId;

  /** reserved for future use */
  private String paramName0;

  /** reserved for future use */
  private String paramName1;

  /** reserved for future use */
  private String paramValue0;

  /** reserved for future use */
  private String paramValue1;

  /** uuidOfAttributeDef to edit **/
  private String uuidOfAttributeDef;

  /** name of attribute def to edit  **/
  private String nameOfAttributeDef;

  /**
   * can be assigned to these types: ATTRIBUTE_DEF, ATTRIBUTE_DEF_ASSIGNMENT, EFFECTIVE_MEMBERSHIP,
   * EFFECTIVE_MEMBERSHIP_ASSIGNMENT, GROUP, GROUP_ASSIGNMENT, IMMEDIATE_MEMBERSHIP,
   * IMMEDIATE_MEMBERSHIP_ASSIGNMENT, MEMBER, MEMBER_ASSIGNMENT, STEM, STEM_ASSIGNMENT
   */
  private String[] assignableTos;

  /** type of attribute def, from enum AttributeDefType, e.g. attr, domain, type, limit, perm **/
  private String attributeDefType;

  /**
   * T of F for if can be assigned multiple times to one object
   */
  private String multiAssignable;

  /**
   * T or F for if multiple values can be assigned to the attribute assignment
   */
  private String multiValued;

  /**
   * what type of value on assignments: AttributeDefValueType: e.g. integer, timestamp, string, floating, marker, memberId
   */
  private String valueType;

  /**
   * uuidOfAttributeDef to edit
   * @return uuidOfAttributeDef
   */
  public String getUuidOfAttributeDef() {
    return this.uuidOfAttributeDef;
  }

  /**
   * uuidOfAttributeDef to edit
   * @param uuidOfAttributeDef1
   */
  public void setUuidOfAttributeDef(String uuidOfAttributeDef1) {
    this.uuidOfAttributeDef = uuidOfAttributeDef1;
  }

  /**
   * name of attribute def to edit
   * @return nameOfAttributeDef
   */
  public String getNameOfAttributeDef() {
    return this.nameOfAttributeDef;
  }

  /**
   * name of attribute def to edit
   * @param nameOfAttributeDef1
   */
  public void setNameOfAttributeDef(String nameOfAttributeDef1) {
    this.nameOfAttributeDef = nameOfAttributeDef1;
  }

  /**
   * can be assigned to these types: ATTRIBUTE_DEF, ATTRIBUTE_DEF_ASSIGNMENT, EFFECTIVE_MEMBERSHIP,
   * EFFECTIVE_MEMBERSHIP_ASSIGNMENT, GROUP, GROUP_ASSIGNMENT, IMMEDIATE_MEMBERSHIP,
   * IMMEDIATE_MEMBERSHIP_ASSIGNMENT, MEMBER, MEMBER_ASSIGNMENT, STEM, STEM_ASSIGNMENT
   * @return assignableTos
   */
  public String[] getAssignableTos() {
    return this.assignableTos;
  }

  /**
   * can be assigned to these types: ATTRIBUTE_DEF, ATTRIBUTE_DEF_ASSIGNMENT, EFFECTIVE_MEMBERSHIP,
   * EFFECTIVE_MEMBERSHIP_ASSIGNMENT, GROUP, GROUP_ASSIGNMENT, IMMEDIATE_MEMBERSHIP,
   * IMMEDIATE_MEMBERSHIP_ASSIGNMENT, MEMBER, MEMBER_ASSIGNMENT, STEM, STEM_ASSIGNMENT
   * @param assignableTos1
   */
  public void setAssignableTos(String[] assignableTos1) {
    this.assignableTos = assignableTos1;
  }

  /** 
   * type of attribute def, from enum AttributeDefType, e.g. attr, domain, type, limit, perm 
   * @return attributeDefType
   */
  public String getAttributeDefType() {
    return this.attributeDefType;
  }

  /** 
   * type of attribute def, from enum AttributeDefType, e.g. attr, domain, type, limit, perm 
   * @param attributeDefType1
   */
  public void setAttributeDefType(String attributeDefType1) {
    this.attributeDefType = attributeDefType1;
  }

  /**
   * @return the multiAssignable
   */
  public String getMultiAssignable() {
    return this.multiAssignable;
  }

  /**
   * @param multiAssignable1 the multiAssignable to set
   */
  public void setMultiAssignable(String multiAssignable1) {
    this.multiAssignable = multiAssignable1;
  }

  /**
   * @return the multiValued
   */
  public String getMultiValued() {
    return this.multiValued;
  }

  /**
   * @param multiValued1 the multiValued to set
   */
  public void setMultiValued(String multiValued1) {
    this.multiValued = multiValued1;
  }

  /**
   * @return the valueType
   */
  public String getValueType() {
    return this.valueType;
  }

  /**
   * @param valueType1 the valueType to set
   */
  public void setValueType(String valueType1) {
    this.valueType = valueType1;
  }

  /**
   * the uuid of the attributeDefName to edit (mutually exclusive with attributeDefNameLookupName)
   */
  private String attributeDefLookupUuid;

  /**
   * to lookup the attributeDef (mutually exclusive with attributeDefName)
   * @return lookup uuid
   */
  public String getAttributeDefLookupUuid() {
    return this.attributeDefLookupUuid;
  }

  /**
   * to lookup the attributeDef (mutually exclusive with attributeDefName)
   * @param attributeDefLookupUuid1
   */
  public void setAttributeDefLookupUuid(String attributeDefLookupUuid1) {
    this.attributeDefLookupUuid = attributeDefLookupUuid1;
  }

  /**
   * to lookup the attributeDef (mutually exclusive with attributeDefUuid)
   * @return lookup name
   */
  public String getAttributeDefLookupName() {
    return this.attributeDefLookupName;
  }

  /**
   * to lookup the attributeDef (mutually exclusive with attributeDefUuid)
   * @param attributeDefLookupName1
   */
  public void setAttributeDefLookupName(String attributeDefLookupName1) {
    this.attributeDefLookupName = attributeDefLookupName1;
  }

  /**
   * of the attributeDefName, empty will be ignored
   * @return description
   */
  public String getDescription() {
    return this.description;
  }

  /**
   * of the attributeDefName, empty will be ignored
   * @param description1
   */
  public void setDescription(String description1) {
    this.description = description1;
  }

  /**
   * if the save should be constrained to INSERT, UPDATE, or INSERT_OR_UPDATE (default)
   * @return save mode
   */
  public String getSaveMode() {
    return this.saveMode;
  }

  /**
   * if the save should be constrained to INSERT, UPDATE, or INSERT_OR_UPDATE (default)
   * @param saveMode1
   */
  public void setSaveMode(String saveMode1) {
    this.saveMode = saveMode1;
  }

  /**
   * T or F (default is F) if parent stems should be created if not exist
   * @return if create parent stems if not exist
   */
  public String getCreateParentStemsIfNotExist() {
    return this.createParentStemsIfNotExist;
  }

  /**
   * T or F (default is F) if parent stems should be created if not exist
   * @param createParentStemsIfNotExist1
   */
  public void setCreateParentStemsIfNotExist(String createParentStemsIfNotExist1) {
    this.createParentStemsIfNotExist = createParentStemsIfNotExist1;
  }

  /**
   * to lookup the attributeDef (mutually exclusive with attributeDefUuid)
   */
  private String attributeDefLookupName;

  /**
   * of the attributeDefName, empty will be ignored
   */
  private String description;

  /**
   * if the save should be constrained to INSERT, UPDATE, or INSERT_OR_UPDATE (default)
   */
  private String saveMode;

  /**
   * T or F (default is F) if parent stems should be created if not exist
   */
  private String createParentStemsIfNotExist;

  /**
   * if acting as another user
   * @return id
   */
  public String getActAsSubjectId() {
    return this.actAsSubjectId;
  }

  /**
   * if acting as another user
   * @return subject identifier
   */
  public String getActAsSubjectIdentifier() {
    return this.actAsSubjectIdentifier;
  }

  /**
   * if acting as another user
   * @return source id 
   */
  public String getActAsSubjectSourceId() {
    return this.actAsSubjectSourceId;
  }

  /**
   * reserved for future use
   * @return param name 0
   */
  public String getParamName0() {
    return this.paramName0;
  }

  /**
   * reserved for future use
   * @return paramname1
   */
  public String getParamName1() {
    return this.paramName1;
  }

  /**
   * reserved for future use
   * @return param value 0
   */
  public String getParamValue0() {
    return this.paramValue0;
  }

  /**
   * reserved for future use
   * @return param value 1
   */
  public String getParamValue1() {
    return this.paramValue1;
  }

  /**
   * if acting as another user
   * @param actAsSubjectId1
   */
  public void setActAsSubjectId(String actAsSubjectId1) {
    this.actAsSubjectId = actAsSubjectId1;
  }

  /**
   * if acting as another user
   * @param actAsSubjectIdentifier1
   */
  public void setActAsSubjectIdentifier(String actAsSubjectIdentifier1) {
    this.actAsSubjectIdentifier = actAsSubjectIdentifier1;
  }

  /**
   * if acting as another user
   * @param actAsSubjectSourceId1
   */
  public void setActAsSubjectSourceId(String actAsSubjectSourceId1) {
    this.actAsSubjectSourceId = actAsSubjectSourceId1;
  }

  /**
   * reserved for future use
   * @param _paramName0
   */
  public void setParamName0(String _paramName0) {
    this.paramName0 = _paramName0;
  }

  /**
   * reserved for future use
   * @param _paramName1
   */
  public void setParamName1(String _paramName1) {
    this.paramName1 = _paramName1;
  }

  /**
   * reserved for future use
   * @param _paramValue0
   */
  public void setParamValue0(String _paramValue0) {
    this.paramValue0 = _paramValue0;
  }

  /**
   * reserved for future use
   * @param _paramValue1
   */
  public void setParamValue1(String _paramValue1) {
    this.paramValue1 = _paramValue1;
  }

}
