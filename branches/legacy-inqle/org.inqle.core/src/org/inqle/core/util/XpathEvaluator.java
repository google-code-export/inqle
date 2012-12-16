package org.inqle.core.util;

import javax.xml.xpath.*;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XpathEvaluator {

	private static Logger log = Logger.getLogger(XpathEvaluator.class);
	
	private XPathExpression expression;

	public XpathEvaluator(String xpath) throws XPathExpressionException {
		
		XPathFactory factory = XPathFactory.newInstance();
		XPath xpathQueryer = factory.newXPath();
		expression = xpathQueryer.compile(xpath);
	}
	
	public NodeList queryGetNodeList(Document document) throws XPathExpressionException {
		NodeList result = (NodeList)expression.evaluate(document, XPathConstants.NODESET);
		return result;
	}
	
	public String queryGetString(Document document) throws XPathExpressionException {
		String result = (String)expression.evaluate(document, XPathConstants.STRING);
		return result;
	}
	
	public Node queryGetNode(Document document) throws XPathExpressionException {
		Node result = (Node)expression.evaluate(document, XPathConstants.NODE);
		return result;
	}
}
