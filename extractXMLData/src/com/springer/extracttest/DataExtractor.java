package com.springer.extracttest;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;


import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class DataExtractor {



	
	
	
	/**
	 * @param args
	 */
	
	
	
	public static void main(String[] args) {
		String articlePath;
		articlePath="";
		if (args.length > 0 ) {
			articlePath = args[0];
			
		}else{
			System.out.println("You must enter the absolute path to get the articles");
			
		}
		 DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		    
		 /*Not validating the XML schema*/
		 factory.setValidating(false);
		 
		 factory.setIgnoringElementContentWhitespace(true);
		    try {
		        DocumentBuilder builder = factory.newDocumentBuilder();
		        File file = new File(articlePath);
		        Document doc = builder.parse(file);
		        
		        
		        //String tt = doc.getFirstChild().getBaseURI().toString();
		        //System.out.println(tt);
		        
		        iterateOverNodes(doc.getDocumentElement());
		        
		        
		    } catch (ParserConfigurationException e) {
		    } catch (SAXException e) {
		    } catch (IOException e) { 
		    }
	}
	
	
	public static void iterateOverNodes(Node node) {
	    // do something with the current node instead of System.out
	    System.out.println(node.getNodeName());

	    NodeList nodeList = node.getChildNodes();
	    for (int i = 0; i < nodeList.getLength(); i++) {
	        Node currentNode = nodeList.item(i);
	        if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
	            //calls this method for all the children which is Element
	            iterateOverNodes(currentNode);
	        }
	    }
	}

}
