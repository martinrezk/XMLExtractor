package com.springer.extracttest;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
/**
 * This class contains a XML documents and provide methods 
 * to evaluate paths expressions on it
 * @author mrezk
 *
 */
public class XMLDoc {
	Document doc;

	public XMLDoc(Document doc) {
		super();
		this.doc = doc;
	}
	
	/**
	 * Takes a xpath expression, evaluate it, and returns the list of nodes
	 * @param expr
	 * @return
	 * @throws XPathExpressionException 
	 */
	public NodeList evaluateXPathString(String expr) throws XPathExpressionException{
		
		// Create XPathFactory for creating XPath Object
		XPathFactory xPathfactory = XPathFactory.newInstance();

		// Create XPath object from XPathFactory
		XPath xpath = xPathfactory.newXPath();

		// Compile the XPath expression for getting the required information
		XPathExpression exprTitles = xpath.compile(expr);
		NodeList nodes = (NodeList) exprTitles.evaluate(doc,
				XPathConstants.NODESET);
	
		return nodes;
		
	}
}
