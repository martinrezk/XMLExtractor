package com.springer.extracttest;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;


import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class DataExtractor {

	static ArrayList<String> articles = new  ArrayList<String>();;
	static ArrayList<String> authors = new  ArrayList<String>();;

	
	
	
	/**
	 * @param args
	 * @throws XPathExpressionException 
	 */
	
	
	
	public static void main(String[] args) throws XPathExpressionException {
		String articlePath;
		articlePath="";
		if (args.length > 0 ) {
			articlePath = args[0];
			
		}else{
			System.out.println("You must enter the absolute path to get the articles");
			
		}
		
		File[] articleList = getFilesFromDirectory(articlePath);
		
		for (File xmlFile: articleList){
			System.out.println("Working with: "+xmlFile);
		
		 DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		    
		 /*Not validating the XML schema*/
		 factory.setValidating(false);
		    try {
		        DocumentBuilder builder = factory.newDocumentBuilder();
		          
		        
		        Document doc = builder.parse(xmlFile);
		        
		        
		     // Create XPathFactory for creating XPath Object
		        XPathFactory xPathfactory = XPathFactory.newInstance();
		        
		     // Create XPath object from XPathFactory
		        XPath xpath = xPathfactory.newXPath();
		        
		     // Compile the XPath expression for getting 
		       // XPathExpression expr = xpath.compile("/Journal/Volume/Issue/Article/ArticleInfo/ArticleTitle");
		       
		        XPathExpression exprTitles = xpath.compile("/Publisher/Journal/Volume/Issue/Article/ArticleInfo/ArticleTitle");
		     //   XPathExpression exprAuthors = xpath.compile("/Publisher/Journal/Volume/Issue/Article/ArticleHeader/AuthorGroup/Author/AuthorName/*[self::GivenName or self::FamilyName]");
		       String nameExp ="/Publisher/Journal/Volume/Issue/Article/ArticleHeader/AuthorGroup/Author/AuthorName";
		     //  String familyExp ="/Publisher/Journal/Volume/Issue/Article/ArticleHeader/AuthorGroup/Author/AuthorName/FamilyName";
		      // String concatName = "concat(" + familyExp + ", " + nameExp+")";

		       XPathExpression exprAuthors = xpath.compile(nameExp);
		        
		        
		        
		        
		     // XPath  executing xpath expression in java
		        //Object result = expr.evaluate(doc);
		        NodeList nodesTitles = (NodeList) exprTitles.evaluate(doc, XPathConstants.NODESET);
		      
		        NodeList nodesAuthors = (NodeList) exprAuthors.evaluate(doc, XPathConstants.NODESET);

		      //  printXpathResult(nodesTitles);
		        
		       // addArticleTitles(nodesTitles);
		    
		        addArticleAuthors(nodesAuthors);

		        
		       // System.out.println("Titles of Articles in:"+ xmlFile.getName());
		        //printXpathResult(nodeList);

		        
		    } catch (ParserConfigurationException e) {
		    	System.out.println("Bad XML");

		    } catch (SAXException e) {
		    	System.out.println("Bad SAX");
		    } catch (XPathExpressionException e) {   
		    	System.out.println("Bad Xpath");
		    } catch (IOException e) { 
		    	System.out.println("Bad File");

		    }
		}//End For files!	
		
		
		// sorting ignoring case
		  Collections.sort(articles, new Comparator<String>() {
		        @Override
		        public int compare(String s1, String s2) {
		            return s1.compareToIgnoreCase(s2);
		        }
		    });
		  
		  
		/*  for(int i=0; i<articles.size(); i++){
			    System.out.println(articles.get(i));
		  }*/
		  
		  
		  
			// sorting ignoring case
		  Collections.sort(authors, new Comparator<String>() {
		        @Override
		        public int compare(String s1, String s2) {
		            return s1.compareToIgnoreCase(s2);
		        }
		    });

		  
		  System.out.println(authors.size());
		  for(int i=0; i<authors.size(); i++){
			    System.out.println(authors.get(i));
		  }
		  
		  
		  // iterateOverNodes(doc.getDocumentElement());
			
	}
	
	/**
	 * Recursively iterates over the node's children
	 * @param node
	 */
	public static String getAuthorsName(Node node) {
	    // do something with the current node instead of System.out
		return getAuthorsName(node, false, false, "").trim();
	}
	
	public static String getAuthorsName(Node node, boolean haveGiven, boolean haveFamily, String name) {
		   
	//	System.out.println(node.getNodeName());

	    NodeList nodeList = node.getChildNodes();
	    for (int i = 0; i < nodeList.getLength(); i++) {
	        Node currentNode = nodeList.item(i);
	        if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
	        	String tag= currentNode.getNodeName();
	        	if (tag.equals("GivenName")){
	        		haveGiven = true;
	        		Node itemChild = currentNode.getFirstChild();
	        		name = name + itemChild.getNodeValue().trim();
	        	}
	        	
	        	if (tag.equals("FamilyName")){
	        		haveFamily = true;
	        		Node itemChild = currentNode.getFirstChild();
	        		name = itemChild.getNodeValue().trim() + " "+name;
	        	}
	        	
	            	
	        	if (haveGiven && haveFamily){
	        		return name;
	        	}else{
	        		getAuthorsName(currentNode,haveGiven, haveFamily, name );
	        	}
	        }
	    }
		return name;
	}// end  iterateOverNodes
	
	
	
	
	
	/**
	 * Print the string value of a  list of nodes
	 * @param nodes
	 */
	public static void printXpathResult(NodeList nodes){
        //NodeList nodes = (NodeList) result;
		System.out.println(nodes.getLength());
        for (int i = 0; i < nodes.getLength(); i++) {
            System.out.println(nodes.item(i).getFirstChild().getNodeValue()); 

          //  System.out.println(nodes.item(i).getNodeValue());
        }
    }

	
	public static  File[] getFilesFromDirectory(String articlePath) {
		File filePath = new File(articlePath);

		File[] articleFiles = null;

	        if (filePath.isDirectory()) {
	            System.out.println("Working Directory with XML articles: " + filePath.getName());
	            articleFiles = filePath.listFiles();
	        } else {
	            System.out.println("You must enter a valid Directory. For instance /Users/mrezk/Downloads/test-articles/ ");
	        }
	    
		return  articleFiles;
	}
	
	/**
	 * Given a set of list of nodes containing titles, it adds the strings 
	 * to the corresponding field 
	 * @param nodeList
	 */
	private static void addArticleTitles(NodeList nodeList){
	    //Adding titles to the list of articles
        for (int i = 0; i < nodeList.getLength(); i++) {
        	
        	Node item = nodeList.item(i);
        	Node itemChild = item.getFirstChild();
			articles.add(itemChild.getNodeValue()); 

            //System.out.println(item.getFirstChild().getNodeValue());
        }
	}
	
	
	private static void addArticleAuthors(NodeList nodeList ){
	    //Adding titles to the list of articles
		String fullName= "";		
        for (int i = 0; i < nodeList.getLength(); i++) {
        	
        	Node author = nodeList.item(i);
        	fullName = getAuthorsName(author);
        	System.out.println(fullName);
        	
            
            
        	/*String tag= item.getNodeName();
        	if (tag.equals("GivenName")){
        		Node itemChild = item.getFirstChild();
        		first = itemChild.getNodeValue();
        	}
        	
         	if (tag.equals("FamilyName")){
        		Node itemChild = item.getFirstChild();
        		last = itemChild.getNodeValue();
        	}*/
        	if (!authors.contains(fullName)){
        		authors.add(fullName); 
        	}
            //System.out.println(item.getFirstChild().getNodeValue());
        }
	}


	

}
