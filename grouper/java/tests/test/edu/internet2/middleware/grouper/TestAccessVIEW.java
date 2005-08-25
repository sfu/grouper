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

package test.edu.internet2.middleware.grouper;

import  edu.internet2.middleware.grouper.*;
import  edu.internet2.middleware.subject.*;

import  java.util.*;
import  junit.framework.*;


public class TestAccessVIEW extends TestCase {

  private GrouperSession  s, nrs0, nrs1;
  private GrouperQuery    q;

  public TestAccessVIEW(String name) {
    super(name);
  }

  protected void setUp () {
    DB db = new DB();
    db.emptyTables();
    db.stop();
    s = Constants.createSession();
    Assert.assertNotNull("s !null", s);
    nrs0 = Constants.createSession(Constants.mem0I, Constants.mem0T);
    Assert.assertNotNull("nrs0 !null", nrs0);
    nrs1 = Constants.createSession(Constants.mem1I, Constants.mem1T);
    Assert.assertNotNull("nrs1 !null", nrs1);
    Constants.createGroups(s);
    Constants.addMembers(s);
    q = new GrouperQuery(s);
    Assert.assertNotNull("q !null", q);
  }

  protected void tearDown () {
    s.stop();
    nrs0.stop();
    nrs1.stop();
  }


  /*
   * TESTS
   */

  public void testViewAsRoot() {
    Assert.assertNotNull("g0 !null", Constants.g0);
    Assert.assertNotNull("g1 !null", Constants.g1);
    Assert.assertNotNull("g2 !null", Constants.g2);
    Assert.assertNotNull("gA !null", Constants.gA);
    Assert.assertNotNull("gB !null", Constants.gB);
    Assert.assertNotNull("gC !null", Constants.gC);
    Assert.assertNotNull("gD !null", Constants.gD);
  }

  public void testViewAsNonRootWithNoExplicitViewers() {
    GrouperGroup g = Constants.loadGroup(nrs0, Constants.g0s, Constants.g0e);
    Assert.assertNotNull("g0 !null", g);

    g = Constants.loadGroup(nrs0, Constants.g1s, Constants.g1e);
    Assert.assertNotNull("g1 !null", g);

    g = Constants.loadGroup(nrs0, Constants.g2s, Constants.g2e);
    Assert.assertNotNull("g2 !null", g);

    g = Constants.loadGroup(nrs0, Constants.gAs, Constants.gAe);
    Assert.assertNotNull("gA !null", g);

    g = Constants.loadGroup(nrs0, Constants.gBs, Constants.gBe);
    Assert.assertNotNull("gB !null", g);

    g = Constants.loadGroup(nrs0, Constants.gCs, Constants.gCe);
    Assert.assertNotNull("gC !null", g);

    g = Constants.loadGroup(nrs0, Constants.gDs, Constants.gDe);
    Assert.assertNotNull("gD !null", g);
  }

  public void testViewAsNonRootWithExplicitViewersAndNoGrantedPriv() {
    Constants.grantAccessPriv(
      Constants.g0s, Constants.g0e, Constants.m0, Grouper.PRIV_VIEW
    );
    GrouperGroup g = Constants.loadGroup(nrs1, Constants.g0s, Constants.g0e);
    Assert.assertNull("g0 null", g);

    Constants.grantAccessPriv(Constants.g1s, Constants.g1e, Constants.m0, Grouper.PRIV_VIEW);
    g = Constants.loadGroup(nrs1, Constants.g1s, Constants.g1e);
    Assert.assertNull("g1 null", g);

    Constants.grantAccessPriv(Constants.g2s, Constants.g2e, Constants.m0, Grouper.PRIV_VIEW);
    g = Constants.loadGroup(nrs1, Constants.g2s, Constants.g2e);
    Assert.assertNull("g2 null", g);

    Constants.grantAccessPriv(Constants.gAs, Constants.gAe, Constants.m0, Grouper.PRIV_VIEW);
    g = Constants.loadGroup(nrs1, Constants.gAs, Constants.gAe);
    Assert.assertNull("gA null", g);

    Constants.grantAccessPriv(Constants.gBs, Constants.gBe, Constants.m0, Grouper.PRIV_VIEW);
    g = Constants.loadGroup(nrs1, Constants.gBs, Constants.gBe);
    Assert.assertNull("gB null", g);

    Constants.grantAccessPriv(Constants.gCs, Constants.gCe, Constants.m0, Grouper.PRIV_VIEW);
    g = Constants.loadGroup(nrs1, Constants.gCs, Constants.gCe);
    Assert.assertNull("gC null", g);

    Constants.grantAccessPriv(Constants.gDs, Constants.gDe, Constants.m0, Grouper.PRIV_VIEW);
    g = Constants.loadGroup(nrs1, Constants.gDs, Constants.gDe);
    Assert.assertNull("gD null", g);
  }

  public void testViewAsNonRootWithExplicitViewersAndNoGrantedPrivButRoot() {
    Constants.grantAccessPriv(Constants.g0s, Constants.g0e, Constants.m0, Grouper.PRIV_VIEW);
    GrouperGroup g = Constants.loadGroup(s, Constants.g0s, Constants.g0e);
    Assert.assertNotNull("g0 !null", g);

    Constants.grantAccessPriv(Constants.g1s, Constants.g1e, Constants.m0, Grouper.PRIV_VIEW);
    g = Constants.loadGroup(s, Constants.g1s, Constants.g1e);
    Assert.assertNotNull("g1 !null", g);

    Constants.grantAccessPriv(Constants.g2s, Constants.g2e, Constants.m0, Grouper.PRIV_VIEW);
    g = Constants.loadGroup(s, Constants.g2s, Constants.g2e);
    Assert.assertNotNull("g2 !null", g);

    Constants.grantAccessPriv(Constants.gAs, Constants.gAe, Constants.m0, Grouper.PRIV_VIEW);
    g = Constants.loadGroup(s, Constants.gAs, Constants.gAe);
    Assert.assertNotNull("gA !null", g);

    Constants.grantAccessPriv(Constants.gBs, Constants.gBe, Constants.m0, Grouper.PRIV_VIEW);
    g = Constants.loadGroup(s, Constants.gBs, Constants.gBe);
    Assert.assertNotNull("gB !null", g);

    Constants.grantAccessPriv(Constants.gCs, Constants.gCe, Constants.m0, Grouper.PRIV_VIEW);
    g = Constants.loadGroup(s, Constants.gCs, Constants.gCe);
    Assert.assertNotNull("gC !null", g);

    Constants.grantAccessPriv(Constants.gDs, Constants.gDe, Constants.m0, Grouper.PRIV_VIEW);
    g = Constants.loadGroup(s, Constants.gDs, Constants.gDe);
    Assert.assertNotNull("gD !null", g);
  }

  public void testViewAsNonRootWithExplicitViewersAndGrantedPriv() {
    Constants.grantAccessPriv(Constants.g0s, Constants.g0e, Constants.m0, Grouper.PRIV_VIEW);
    GrouperGroup g = Constants.loadGroup(nrs0, Constants.g0s, Constants.g0e);
    Assert.assertNotNull("g0 !null", g);

    Constants.grantAccessPriv(Constants.g1s, Constants.g1e, Constants.m0, Grouper.PRIV_VIEW);
    g = Constants.loadGroup(nrs0, Constants.g1s, Constants.g1e);
    Assert.assertNotNull("g1 !null", g);

    Constants.grantAccessPriv(Constants.g2s, Constants.g2e, Constants.m0, Grouper.PRIV_VIEW);
    g = Constants.loadGroup(nrs0, Constants.g2s, Constants.g2e);
    Assert.assertNotNull("g2 !null", g);

    Constants.grantAccessPriv(Constants.gAs, Constants.gAe, Constants.m0, Grouper.PRIV_VIEW);
    g = Constants.loadGroup(nrs0, Constants.gAs, Constants.gAe);
    Assert.assertNotNull("gA !null", g);

    Constants.grantAccessPriv(Constants.gBs, Constants.gBe, Constants.m0, Grouper.PRIV_VIEW);
    g = Constants.loadGroup(nrs0, Constants.gBs, Constants.gBe);
    Assert.assertNotNull("gB !null", g);

    Constants.grantAccessPriv(Constants.gCs, Constants.gCe, Constants.m0, Grouper.PRIV_VIEW);
    g = Constants.loadGroup(nrs0, Constants.gCs, Constants.gCe);
    Assert.assertNotNull("gC !null", g);

    Constants.grantAccessPriv(Constants.gDs, Constants.gDe, Constants.m0, Grouper.PRIV_VIEW);
    g = Constants.loadGroup(nrs0, Constants.gDs, Constants.gDe);
    Assert.assertNotNull("gD !null", g);
  }

  public void testViewDisplayExtensionAsNonRootAndEmptyVIEW() {
    String attr = "displayExtension";

    GrouperStem ns = Constants.loadStem(nrs0, Constants.ns0s, Constants.ns0e);
    Assert.assertNotNull("ns0", ns);
    Assert.assertTrue(
      "ns0 "+attr, !ns.attribute(attr).value().equals("")
    );
    
    GrouperGroup g = Constants.loadGroup(nrs0, Constants.g0s, Constants.g0e);
    Assert.assertNotNull("g0", g);
    Assert.assertTrue(
      "g0 "+attr, !g.attribute(attr).value().equals("")
    );
 
    g = Constants.loadGroup(nrs0, Constants.g1s, Constants.g1e);
    Assert.assertNotNull("g1", g);
    Assert.assertTrue(
      "g1 "+attr, !g.attribute(attr).value().equals("")
    );
  }

  public void testViewDisplayExtensionAsNonRootAndNonEmptyVIEW() {
    String attr = "displayExtension";

    GrouperStem ns = Constants.loadStem(nrs0, Constants.ns0s, Constants.ns0e);
    Assert.assertNotNull("ns0", ns);
    Assert.assertTrue(
      "ns0 "+attr, !ns.attribute(attr).value().equals("")
    );
   
    // Member 
    Constants.grantAccessPriv(
      s, Constants.g0, Constants.g0.toMember(), Grouper.PRIV_VIEW
    );
    GrouperGroup g = Constants.loadGroup(nrs0, Constants.g0s, Constants.g0e);
    Assert.assertNotNull("g0", g);
    Assert.assertTrue(
      "g0 "+attr, !g.attribute(attr).value().equals("")
    );

    // !Member 
    Constants.grantAccessPriv(
      s, Constants.g1, Constants.g1.toMember(), Grouper.PRIV_VIEW
    );
    Assert.assertNull(
      "g1", Constants.loadGroup(nrs0, Constants.g1s, Constants.g1e)
    );
  }

  public void testViewExtensionAsNonRootAndEmptyVIEW() {
    String attr = "extension";

    GrouperStem ns = Constants.loadStem(nrs0, Constants.ns0s, Constants.ns0e);
    Assert.assertNotNull("ns0", ns);
    Assert.assertTrue(
      "ns0 "+attr, !ns.attribute(attr).value().equals("")
    );
    
    GrouperGroup g = Constants.loadGroup(nrs0, Constants.g0s, Constants.g0e);
    Assert.assertNotNull("g0", g);
    Assert.assertTrue(
      "g0 "+attr, !g.attribute(attr).value().equals("")
    );
 
    g = Constants.loadGroup(nrs0, Constants.g1s, Constants.g1e);
    Assert.assertNotNull("g1", g);
    Assert.assertTrue(
      "g1 "+attr, !g.attribute(attr).value().equals("")
    );
  }

  public void testViewExtensionAsNonRootAndNonEmptyVIEW() {
    String attr = "extension";

    GrouperStem ns = Constants.loadStem(nrs0, Constants.ns0s, Constants.ns0e);
    Assert.assertNotNull("ns0", ns);
    Assert.assertTrue(
      "ns0 "+attr, !ns.attribute(attr).value().equals("")
    );
   
    // Member 
    Constants.grantAccessPriv(
      s, Constants.g0, Constants.g0.toMember(), Grouper.PRIV_VIEW
    );
    GrouperGroup g = Constants.loadGroup(nrs0, Constants.g0s, Constants.g0e);
    Assert.assertNotNull("g0", g);
    Assert.assertTrue(
      "g0 "+attr, !g.attribute(attr).value().equals("")
    );

    // !Member 
    Constants.grantAccessPriv(
      s, Constants.g1, Constants.g1.toMember(), Grouper.PRIV_VIEW
    );
    Assert.assertNull(
      "g1", Constants.loadGroup(nrs0, Constants.g1s, Constants.g1e)
    );
  }

  public void testViewNameNonRootAndEmptyVIEW() {
    String attr = "name";

    GrouperStem ns = Constants.loadStem(nrs0, Constants.ns0s, Constants.ns0e);
    Assert.assertNotNull("ns0", ns);
    Assert.assertTrue(
      "ns0 "+attr, !ns.attribute(attr).value().equals("")
    );
    
    GrouperGroup g = Constants.loadGroup(nrs0, Constants.g0s, Constants.g0e);
    Assert.assertNotNull("g0", g);
    Assert.assertTrue(
      "g0 "+attr, !g.attribute(attr).value().equals("")
    );
 
    g = Constants.loadGroup(nrs0, Constants.g1s, Constants.g1e);
    Assert.assertNotNull("g1", g);
    Assert.assertTrue(
      "g1 "+attr, !g.attribute(attr).value().equals("")
    );
  }

  public void testViewNameAsNonRootAndNonEmptyVIEW() {
    String attr = "name";

    GrouperStem ns = Constants.loadStem(nrs0, Constants.ns0s, Constants.ns0e);
    Assert.assertNotNull("ns0", ns);
    Assert.assertTrue(
      "ns0 "+attr, !ns.attribute(attr).value().equals("")
    );
   
    // Member 
    Constants.grantAccessPriv(
      s, Constants.g0, Constants.g0.toMember(), Grouper.PRIV_VIEW
    );
    GrouperGroup g = Constants.loadGroup(nrs0, Constants.g0s, Constants.g0e);
    Assert.assertNotNull("g0", g);
    Assert.assertTrue(
      "g0 "+attr, !g.attribute(attr).value().equals("")
    );

    // !Member 
    Constants.grantAccessPriv(
      s, Constants.g1, Constants.g1.toMember(), Grouper.PRIV_VIEW
    );
    Assert.assertNull(
      "g1", Constants.loadGroup(nrs0, Constants.g1s, Constants.g1e)
    );
  }

  public void testViewStemAsNonRootAndEmptyVIEW() {
    String attr = "stem";

    GrouperStem ns = Constants.loadStem(nrs0, Constants.ns0s, Constants.ns0e);
    Assert.assertNotNull("ns0", ns);
    Assert.assertTrue(
      "ns0 "+attr, !ns.attribute(attr).value().equals("")
    );
    
    GrouperGroup g = Constants.loadGroup(nrs0, Constants.g0s, Constants.g0e);
    Assert.assertNotNull("g0", g);
    Assert.assertTrue(
      "g0 "+attr, !g.attribute(attr).value().equals("")
    );
 
    g = Constants.loadGroup(nrs0, Constants.g1s, Constants.g1e);
    Assert.assertNotNull("g1", g);
    Assert.assertTrue(
      "g1 "+attr, !g.attribute(attr).value().equals("")
    );
  }

  public void testViewStemAsNonRootAndNonEmptyVIEW() {
    String attr = "stem";

    GrouperStem ns = Constants.loadStem(nrs0, Constants.ns0s, Constants.ns0e);
    Assert.assertNotNull("ns0", ns);
    Assert.assertTrue(
      "ns0 "+attr, !ns.attribute(attr).value().equals("")
    );
   
    // Member 
    Constants.grantAccessPriv(
      s, Constants.g0, Constants.g0.toMember(), Grouper.PRIV_VIEW
    );
    GrouperGroup g = Constants.loadGroup(nrs0, Constants.g0s, Constants.g0e);
    Assert.assertNotNull("g0", g);
    Assert.assertTrue(
      "g0 "+attr, !g.attribute(attr).value().equals("")
    );

    // !Member 
    Constants.grantAccessPriv(
      s, Constants.g1, Constants.g1.toMember(), Grouper.PRIV_VIEW
    );
    Assert.assertNull(
      "g1", Constants.loadGroup(nrs0, Constants.g1s, Constants.g1e)
    );
  }

  public void testViewGUIDAsRoot() {
    Assert.assertTrue("ns0", !Constants.ns0.id().equals(""));
    Assert.assertTrue("ns1", !Constants.ns1.id().equals(""));
    Assert.assertTrue("ns1", !Constants.ns1.id().equals(""));
    Assert.assertTrue("g0", !Constants.g0.id().equals(""));
    Assert.assertTrue("g1", !Constants.g1.id().equals(""));
    Assert.assertTrue("g2", !Constants.g2.id().equals(""));
    Assert.assertTrue("gA", !Constants.gA.id().equals(""));
    Assert.assertTrue("gB", !Constants.gB.id().equals(""));
    Assert.assertTrue("gC", !Constants.gC.id().equals(""));
    Assert.assertTrue("gD", !Constants.gD.id().equals(""));
  }

  public void testViewGUIDAsNonRootAndEmptyVIEW() {
    GrouperStem ns = Constants.loadStem(nrs0, Constants.ns0s, Constants.ns0e);
    Assert.assertNotNull("ns0", ns);
    Assert.assertTrue("ns0 priv", !ns.id().equals(""));

    GrouperGroup g = Constants.loadGroup(nrs0, Constants.g0s, Constants.g0e);
    Assert.assertNotNull("g0", g);
    Assert.assertTrue("g0 priv", !g.id().equals(""));
 
    g = Constants.loadGroup(nrs0, Constants.g1s, Constants.g1e);
    Assert.assertNotNull("g1", g);
    Assert.assertTrue("g1 priv", !g.id().equals(""));
  }

  public void testViewGUIDAsNonRootAndNonEmptyVIEW() {
    GrouperStem ns = Constants.loadStem(nrs0, Constants.ns0s, Constants.ns0e);
    Assert.assertNotNull("ns0", ns);
    Assert.assertTrue("ns0 priv", !ns.id().equals(""));

    // Can m0, a member of g0, load it and view its guid?
    Constants.grantAccessPriv(
      s, Constants.g0, Constants.g0.toMember(), Grouper.PRIV_VIEW
    );
    GrouperGroup g = Constants.loadGroup(nrs0, Constants.g0s, Constants.g0e);
    Assert.assertNotNull("g0", g);
    Assert.assertTrue("g0 priv", !g.id().equals(""));

    // Can m0, not a member of g1, load it and view its guid?
    Constants.grantAccessPriv(
      s, Constants.g1, Constants.g1.toMember(), Grouper.PRIV_VIEW
    );
    g = Constants.loadGroup(nrs0, Constants.g1s, Constants.g1e);
    Assert.assertNull("g1 = null", g);
  }

  public void testViewViaGroupThatCannotBeViewed() {
    // setup
    // root: add g0/m0

    // root: add g1/g0
    try {
      Constants.g1.listAddVal(Constants.g0.toMember());
      Assert.assertTrue("root: add g1/g0", true);
    } catch (Exception e) {
      Assert.fail("root: add g1/g0");
    }
    // Assert memberships
    Assert.assertTrue(
      "root: g0 hasMember m0", Constants.g0.hasMember(Constants.m0)
    );
    Assert.assertTrue(
      "root: g1 hasMember g0", Constants.g1.hasMember(Constants.g0.toMember())
    );
    Assert.assertTrue(
      "root: g1 hasMember m0", Constants.g1.hasMember(Constants.m0)
    );
    // root: grant g1/root/VIEW
    Assert.assertTrue(
      "root: grant g1/root/VIEW",
      s.access().grant(
        s, Constants.g1, Constants.mr, Grouper.PRIV_VIEW
      )
    );
    // Assert privileges
    Assert.assertTrue(
      "root: has g1/root/VIEW",
      s.access().has(
        s, Constants.g1, Constants.mr, Grouper.PRIV_VIEW
      )
    );
    Assert.assertFalse(
      "root: ! has g1/g0/VIEW",
      s.access().has(
        s, Constants.g1, Constants.g0.toMember(), Grouper.PRIV_VIEW
      )
    );
    Assert.assertFalse(
      "root: ! has g1/m0/VIEW",
      s.access().has(
        s, Constants.g1, Constants.m0, Grouper.PRIV_VIEW
      )
    );
    // Assert memberships as root
    Assert.assertTrue(
      "root: isMember g0", Constants.m0.isMember(Constants.g0)
    );
    Assert.assertTrue(
      "root: isMember g1", Constants.m0.isMember(Constants.g1)
    );
    // Assert memberships as m0
    GrouperGroup g0 = Constants.loadGroup(
      nrs0, Constants.g0s, Constants.g0e
    );
    Assert.assertNotNull("m0: g0", g0);
    GrouperGroup g1 = Constants.loadGroup(
      nrs0, Constants.g1s, Constants.g1e
    );
    Assert.assertNull("m0: ! g1", g1);
    try {
      GrouperMember m0 = GrouperMember.load(
        nrs0, Constants.mem0I, Constants.mem0T
      );
      Assert.assertNotNull("m0", m0);
      List effs = m0.listEffVals();
      Assert.assertTrue("m0: 1 effs", effs.size() == 1);
      GrouperList eff0 = (GrouperList) effs.get(0);
      try {
        GrouperGroup eff0g = (GrouperGroup) eff0.group();
        Assert.fail("m0: ! eff0g");
      } catch (RuntimeException e) {
        Assert.assertTrue("m0: ! eff0g", true);
      }
      try {
        GrouperGroup eff0v = (GrouperGroup) eff0.via();
        Assert.assertTrue(
          "m0: eff0v name",
          eff0v.getName().equals(Constants.g0.getName())
        );
      } catch (RuntimeException e) {
        Assert.fail("m0: eff0v");
      }
      String list = eff0.groupField();
      Assert.assertNotNull("m0: list ! null");
      Assert.assertTrue(
        "m0: list", list.equals(Grouper.DEF_LIST_TYPE)
      );
    } catch (SubjectNotFoundException e) {
      Assert.fail("! m0");
    }
  }

}

