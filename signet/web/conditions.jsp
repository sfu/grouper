<!--
  $Id: conditions.jsp,v 1.26 2005-07-26 23:24:15 acohen Exp $
  $Date: 2005-07-26 23:24:15 $
  
  Copyright 2004 Internet2 and Stanford University.  All Rights Reserved.
  Licensed under the Signet License, Version 1,
  see doc/license.txt in this distribution.
-->

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
    <meta name="robots" content="noindex, nofollow" />
    <title>
      Signet
    </title>
    <link href="styles/signet.css" rel="stylesheet" type="text/css" />
    <script language="JavaScript" type="text/javascript" src="scripts/signet.js">
    </script>
  </head>

  <body onload="javascript:selectLimitCheckbox();">
    <script type="text/javascript">
      function selectLimitCheckbox()
      {
        if (hasUnselectedLimits())
        {
          document.form1.completeAssignmentButton.disabled = true;
        }
        else
        {
          document.form1.completeAssignmentButton.disabled = false;
        }
      }
      
      function hasUnselectedLimits()
      {
        var theForm = document.form1;
        var currentLimitName = null;
        var currentLimitSelected = true;
        
        for (var i = 0; i < theForm.elements.length; i++)
        {
          var currentElement = theForm.elements[i];
             
          if (currentElement.name == null)
          {
            continue;
          }
             
          var nameParts = currentElement.name.split(':');
          
          if (nameParts[0] == 'LIMITVALUE_MULTI')
          {
            if ((currentLimitName != null)
                && (currentLimitName != currentElement.name)
                && (currentLimitSelected == false))
            {
              // We've finished examining a Limit, and it had no
              // selected values.
              return true; // We've found an un-selected Limit.
            }
            else if (currentLimitName == currentElement.name)
            {
              // We're looking at a series of values for a single Limit.
              if (currentElement.checked == true)
              {
                currentLimitSelected = true;
              }
            }
            else
            {
              // We're moving on to a previously unexamined Limit. The previous
              // Limits, if any, all had at least one selected value.
              currentLimitName = currentElement.name;
              currentLimitSelected = currentElement.checked;
            }
          }
        }
        
        return !currentLimitSelected;
      }
  </script>
  
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>

<%@ page import="java.text.DateFormat" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.Set" %>
<%@ page import="java.util.HashSet" %>

<%@ page import="edu.internet2.middleware.signet.PrivilegedSubject" %>
<%@ page import="edu.internet2.middleware.signet.Subsystem" %>
<%@ page import="edu.internet2.middleware.signet.Category" %>
<%@ page import="edu.internet2.middleware.signet.Assignment" %>
<%@ page import="edu.internet2.middleware.signet.Function" %>
<%@ page import="edu.internet2.middleware.signet.tree.TreeNode" %>
<%@ page import="edu.internet2.middleware.signet.Signet" %>
<%@ page import="edu.internet2.middleware.signet.Limit" %>

<%@ page import="edu.internet2.middleware.signet.ui.LimitRenderer" %>
<%@ page import="edu.internet2.middleware.signet.ui.Common" %>

<% 
  Signet signet
     = (Signet)
         (request.getSession().getAttribute("signet"));
         
  PrivilegedSubject loggedInPrivilegedSubject
     = (PrivilegedSubject)
         (request.getSession().getAttribute("loggedInPrivilegedSubject"));
         
  PrivilegedSubject currentGranteePrivilegedSubject;
  Subsystem			currentSubsystem;
  Category			currentCategory;
  Function			currentFunction;
  TreeNode			currentScope;
         
  // If the session contains a "currentAssignment" attribute, then we're
  // editing an existing Assignment. Otherwise, we're attempting to create a
  // new one.
  Assignment currentAssignment
    = (Assignment)(request.getSession().getAttribute("currentAssignment"));
   
  if (currentAssignment != null)
  {
    currentGranteePrivilegedSubject = currentAssignment.getGrantee();
    currentFunction = currentAssignment.getFunction();
    currentSubsystem = currentFunction.getSubsystem();
    currentCategory = currentFunction.getCategory();
    currentScope = currentAssignment.getScope();
  }
  else
  {
    currentGranteePrivilegedSubject
      = (PrivilegedSubject)
          (request
             .getSession()
               .getAttribute
                 ("currentGranteePrivilegedSubject"));
         
    currentSubsystem
      = (Subsystem)
          (request.getSession().getAttribute("currentSubsystem"));
         
    currentCategory
      = (Category)
          (request.getSession().getAttribute("currentCategory"));
         
    currentFunction
      = (Function)
          (request.getSession().getAttribute("currentFunction"));
         
    currentScope
      = (TreeNode)
          (request.getSession().getAttribute("currentScope"));
  }

  Limit[] currentLimits
  	= Common.getLimitsInDisplayOrder(currentFunction.getLimits());
         
  DateFormat dateFormat = DateFormat.getDateInstance();
   
  String personViewHref
    = "PersonView.do?granteeSubjectTypeId="
      + currentGranteePrivilegedSubject.getSubjectTypeId()
      + "&granteeSubjectId="
      + currentGranteePrivilegedSubject.getSubjectId()
      + "&subsystemId="
      + currentSubsystem.getId();

       
  String functionsHref
    = "Functions.do?select="
      + currentSubsystem.getId();
       
  String orgBrowseHref
   	= "OrgBrowse.do?functionSelectList="
      + currentFunction.getId();
%>

    <form name="form1" action="Confirm.do">
      <tiles:insert page="/tiles/header.jsp" flush="true" />
      <div id="Navbar">
        <span class="logout">
          <a href="NotYetImplemented.do">
            <%=loggedInPrivilegedSubject.getName()%>: Logout
          </a>
        </span> <!-- logout -->
        <span class="select">
          <a href="Start.do">
            Home
          </a>
          &gt; <!-- displays as text right-angle bracket -->
        <a href="<%=personViewHref%>"> 
            <%=currentGranteePrivilegedSubject.getName()%>
          </a>
<% if (currentAssignment == null)
   {
%>
          &gt; Grant new privilege
<%
   }
   else
   {
%>
          &gt; Edit privilege
<%
   }
%>
          
        </span> <!-- select -->
      </div>  <!-- Navbar -->
      
      <div id="Layout">
        <div id="Content">
          <div id="ViewHead">
<% if (currentAssignment == null)
   {
%>
            Granting new privilege to
<%
   }
   else
   {
%>
            Editing privilege for
<%
   }
%>
            <h1>
              <%=currentGranteePrivilegedSubject.getName()%>
       	    </h1>
       	    <span class="dropback"><%=currentGranteePrivilegedSubject.getDescription()%></span><!--,	Technology Strategy and Support Operations-->
          </div>  <!-- ViewHead -->

         	<div class="section">
<% if (currentAssignment == null)
   {
%>
            <h2>New assignment details</h2>
<%
   }
   else
   {
%>
            <h2>Current assignment details</h2>
<%
   }
%>
							<table>
              	<tr>
              		<th width="15%" class="label" scope="row">Granted to:</td>
              		<td width="75%"><%=currentGranteePrivilegedSubject.getName()%></td>
<% if (currentAssignment == null)
   {
%>
              		<td width="10%">&nbsp;</td> <!-- 3rd col appears only for new assignments -->
<%
   }
%>									
             		</tr>
              	<tr>
              		<th class="label" scope="row">Type:</td>
              		<td><%=currentSubsystem.getName()%></td>
<% if (currentAssignment == null)
   {
%>
              		<td style="white-space: nowrap;">
														<a href="<%=personViewHref%>"><img src="images/arrow_left.gif" alt="" />change</a>
					</td>
<%
	}
%>									
         		</tr>								
              	<tr>
              		<th class="label" scope="row">Privilege:</td>
              		<td><p><span class="category"><%=currentCategory.getName()%></span> : <span class="function"><%=currentFunction.getName()%></span></p>
              		    <p><%=currentFunction.getHelpText()%></p></td>
           		  <% if (currentAssignment == null)
   {
%>
              		<td style="white-space: nowrap;">
										<a href="<%=functionsHref%>"><img src="images/arrow_left.gif" />change</a>
 				  	</td>
<%
   }
%>				
                </tr>			
			    <tr>
              		<th class="label" scope="row">Scope:</td>
              		<td>
											<%=signet.displayAncestry
													(currentScope,
													 " : ",  // childSeparatorPrefix
													 "",                // levelPrefix
													 "",               // levelSuffix
													 "")                 // childSeparatorSuffix
											 %>
				  </td>
<% if (currentAssignment == null)
   {
%>
              		<td style="white-space: nowrap;">
											<a href="<%=orgBrowseHref%>"><img src="images/arrow_left.gif" />change</a>
				  </td>
<%
   }
%>									
							  </tr>					
							</table>						
		  </div>

         
            
<%
  if (currentLimits.length > 0)
  {
%>
            <div class="section">
<% if (currentAssignment == null)
   {
%>
              <h2>Select limits</h2>
<%
   }
   else
   {
%>
              <h2>Edit limits</h2>
<%
   }
%>
           
<%
    for (int i = 0; i < currentLimits.length; i++)
    {
      request.setAttribute("limitAttr", currentLimits[i]);
      request.setAttribute
        ("grantableChoiceSubsetAttr",
         loggedInPrivilegedSubject.getGrantableChoices
           (currentFunction, currentScope, currentLimits[i]));
           
      if (currentAssignment != null)
      {
        request.setAttribute
          ("assignmentLimitValuesAttr", currentAssignment.getLimitValues());
      }
      else
      {
        request.setAttribute
          ("assignmentLimitValuesAttr", new HashSet());
      }
%>
              
              <fieldset>
                <legend>
                  <%=currentLimits[i].getName()%>
                </legend>
                <p>
                  <%=currentLimits[i].getHelpText()%>
                </p>
                  <tiles:insert
                     page='<%="/tiles/" + currentLimits[i].getRenderer()%>'
                     flush="true">
                    <tiles:put name="limit" beanName="limitAttr" />
                    <tiles:put name="grantableChoiceSubset" beanName="grantableChoiceSubsetAttr" />
                    <tiles:put name="assignmentLimitValues" beanName="assignmentLimitValuesAttr" />
                  </tiles:insert>
              </fieldset>
<%
    }
%>
            </div> <!-- section -->
<%
  }
%>
		 
            <div class="section">
<% if (currentAssignment == null)
   {
%>
              <h2>Set conditions</h2>
<%
   }
   else
   {
%>
              <h2>Edit conditions</h2>
<%
   }
%>
              <fieldset>
                <legend>
                  Extensibility: privilege holder can...
                </legend>
                  <input
                     name="can_use"
                     id="can_use"
                     type="checkbox"
                     value="checkbox"
                     <%=(currentAssignment==null
                         /* We're not editing an existing Assignment, so
                          * check this box as a default value.
                          */
                         ? "checked=\"checked\""
                         /* We are editing an existing Assignment, so get
                          * this box's status from the Assignment.
                          */
                         : (currentAssignment.isGrantOnly()
                              ? ""
                              : "checked=\"checked\""))%>
                  />
                  <label for="can_use">use this privilege</label>
                  <br />
                  <input
                     name="can_grant"
                     id="can_grant"
                     type="checkbox"
                     value="checkbox"
                     <%=(currentAssignment==null
                         /* We're not editing an existing Assignment, so
                          * un-check this box as a default value.
                          */
                         ? ""
                         /* We are editing an existing Assignment, so get
                          * this box's status from the Assignment.
                          */
                         : (currentAssignment.isGrantable()
                              ? "checked=\"checked\""
                              : ""))%>
                  />
                  <label for="can_grant">grant this privilege to others</label>

              </fieldset>
              <p>
                <input
                   name="completeAssignmentButton"
                   type="submit"
                   class="button-def"
                   value="<%=(currentAssignment==null?"Complete assignment":"Save changes")%>" />
              </p>
              <p>
                <a href="<%=personViewHref%>">
                  <img src="images/arrow_left.gif" alt="" />CANCEL and return to <%=currentGranteePrivilegedSubject.getName()%>'s view
                </a>
              </p>
            </div> <!-- section -->
        </div> <!--Content -->
				      
        <div id="Sidebar">
          <div class="helpbox">
			 	  	<h2>Help</h2>
			  		<jsp:include page="grant-help.jsp" flush="true" />          
		  </div> <!-- Helpbox -->
        </div> <!-- Sidebar -->
       <tiles:insert page="/tiles/footer.jsp" flush="true" />
      </div> <!-- Layout -->
    </form>
  </body>
</html>
