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
/* Generated By:JJTree: Do not edit this line. ASTFalseNode.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package edu.internet2.middleware.grouperActivemqExt.org.apache.commons.jexl2.parser;

import edu.internet2.middleware.grouperActivemqExt.org.apache.commons.jexl2.parser.JexlNode;
import edu.internet2.middleware.grouperActivemqExt.org.apache.commons.jexl2.parser.Parser;
import edu.internet2.middleware.grouperActivemqExt.org.apache.commons.jexl2.parser.ParserVisitor;

public
class ASTFalseNode extends JexlNode {
  public ASTFalseNode(int id) {
    super(id);
  }

  public ASTFalseNode(Parser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(ParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=5c7ee1ac9277f16597926c1fed96d4bc (do not edit this line) */
