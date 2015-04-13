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

	static ArrayList<String> articles = new ArrayList<String>();;
	static ArrayList<String> authors = new ArrayList<String>();;
	static ArrayList<String> citedAuthors = new ArrayList<String>();;

	/**
	 * @param args
	 * @throws XPathExpressionException
	 * @throws IOException
	 * @throws SAXException
	 */

	public static void main(String[] args) throws XPathExpressionException,
			SAXException, IOException {
		String articlePath = "";
		String task = "";
		int intTask = 0;

		if (args.length > 0) {
			articlePath = args[0];
			task = args[1];
			intTask= Integer.parseInt(task);
		} else {
			System.out
					.println("You must enter two arguments: the absolute path to get the articles and the test to perform (1-n)");

		}

		// Getting the list of XML documents
		IOUtils xmlIO = new IOUtils(articlePath);
		LinkedList<Document> docs = IOUtils.getDocsFromDirectory();

	
			try {

				switch (intTask) {
				case 1:
					getTitles(docs);
					break;
				case 2:
					getAuthors(docs);
					break;
				case 3:
					getCitedAuthors(docs);
					/*
					 * THIS SPEC WAS WRONG!
					 * Publisher/Journal/Volume/Issue/Article
					 * /Bibliography/Citation
					 */

					break;
				default:
					System.out.println("Not a Valid Task");
					break;
				}

			} catch (XPathExpressionException e) {
				System.out.println("Bad Xpath");
			}

		


	


	}

	/**
	 * Sorting  an array ignoring case
	 * @param list
	 * @return
	 */
	private static ArrayList<String> insensitiveSort(ArrayList<String> list) {
		Collections.sort(list, new Comparator<String>() {
			@Override
			public int compare(String s1, String s2) {
				return s1.compareToIgnoreCase(s2);
			}
		});
		return list;
	}

	private static void getCitedAuthors(LinkedList<Document> docs)
			throws XPathExpressionException {
		for (Document doc : docs) {

			XMLDoc workingDoc = new XMLDoc(doc);

		String citedAuthors = "/Publisher/Journal/Volume/Issue/Article/ArticleBackmatter/Bibliography/Citation";
		NodeList nodesCited = workingDoc.evaluateXPathString(citedAuthors);
		addArticleCitedAuthors(nodesCited);
		}
		citedAuthors= insensitiveSort(citedAuthors);
		
		for (int i = 0; i < citedAuthors.size(); i++) {
			System.out.println(i+ "->" +citedAuthors.get(i));
		}


	}

	private static void getAuthors(LinkedList<Document> docs)
			throws XPathExpressionException {
		for (Document doc : docs) {

			XMLDoc workingDoc = new XMLDoc(doc);

		String nameExp = "/Publisher/Journal/Volume/Issue/Article/ArticleHeader/AuthorGroup/Author/AuthorName";
		NodeList nodesNames = workingDoc.evaluateXPathString(nameExp);
		addArticleAuthors(nodesNames);
		}
		authors= insensitiveSort(authors);

		for (int i = 0; i < authors.size(); i++) {
			System.out.println(i+ "->" +authors.get(i));
		}

	}

	private static void getTitles(LinkedList<Document> docs)
			throws XPathExpressionException {
		for (Document doc : docs) {

			XMLDoc workingDoc = new XMLDoc(doc);

		String xpathTitles = "/Publisher/Journal/Volume/Issue/Article/ArticleInfo/ArticleTitle";
		NodeList nodesTitles = workingDoc.evaluateXPathString(xpathTitles);
		addArticleTitles(nodesTitles);
		}
		articles= insensitiveSort(articles);

		for (int i = 0; i < articles.size(); i++) {
			System.out.println(i+ "->" +articles.get(i));
		}

	}

	/**
	 * Recursively iterates over the node's children
	 * 
	 * @param node
	 */
	public static LinkedList<String> getCitedAuthorsName(Node node) {
		// do something with the current node instead of System.out
		LinkedList<String> names = new LinkedList<String>();
		return getCitedAuthorsName(node, false, false, "", names);
	}

	public static LinkedList<String> getCitedAuthorsName(Node node,
			boolean haveGiven, boolean haveFamily, String currentName,
			LinkedList<String> names) {

		// System.out.println(node.getNodeName());

		NodeList nodeList = node.getChildNodes();
		int length = nodeList.getLength();
		for (int i = 0; i < length; i++) {
			Node currentNode = nodeList.item(i);
			short nodeType = currentNode.getNodeType();
			if (nodeType == Node.ELEMENT_NODE) {
				String tag = currentNode.getNodeName();
				if (tag.equals("Initials")) {
					currentName = currentName + " "
							+ currentNode.getFirstChild().getNodeValue();
					haveGiven = true;
				}
				if (tag.equals("FamilyName")) {
					currentName = currentNode.getFirstChild().getNodeValue()
							+ currentName;
					haveFamily = true;
				}

				if (haveGiven && haveFamily) {
					names.add(currentName);
					return names;
				} else {
					getCitedAuthorsName(currentNode, haveGiven, haveFamily,
							currentName, names);
				}
			}

		}
		return names;
	}// end iterateOverNodes

	/**
	 * Print the string value of a list of nodes
	 * 
	 * @param nodes
	 */
	public static void printXpathResult(NodeList nodes) {
		// NodeList nodes = (NodeList) result;
		System.out.println(nodes.getLength());
		for (int i = 0; i < nodes.getLength(); i++) {
			System.out.println(nodes.item(i).getFirstChild().getNodeValue());

			// System.out.println(nodes.item(i).getNodeValue());
		}
	}

	/**
	 * Given a set of list of nodes containing titles, it adds the strings to
	 * the corresponding field
	 * 
	 * @param nodeList
	 */
	private static void addArticleTitles(NodeList nodeList) {
		// Adding titles to the list of articles
		for (int i = 0; i < nodeList.getLength(); i++) {

			Node item = nodeList.item(i);
			Node itemChild = item.getFirstChild();
			articles.add(itemChild.getNodeValue());

			// System.out.println(item.getFirstChild().getNodeValue());
		}
	}

	private static void addArticleAuthors(NodeList nodeList) {
		// Adding titles to the list of articles
		String fullName = "";
		for (int i = 0; i < nodeList.getLength(); i++) {

			Node author = nodeList.item(i);
			fullName = getAuthorsName(author);
			System.out.println(fullName);

			/*
			 * String tag= item.getNodeName(); if (tag.equals("GivenName")){
			 * Node itemChild = item.getFirstChild(); first =
			 * itemChild.getNodeValue(); }
			 * 
			 * if (tag.equals("FamilyName")){ Node itemChild =
			 * item.getFirstChild(); last = itemChild.getNodeValue(); }
			 */
			if (!authors.contains(fullName)) {
				authors.add(fullName);
			}
			// System.out.println(item.getFirstChild().getNodeValue());
		}
	}

	private static void addArticleCitedAuthors(NodeList nodeList) {
		// Adding titles to the list of articles

		// System.out.println(nodeList.getLength());
		for (int i = 0; i < nodeList.getLength(); i++) {

			Node author = nodeList.item(i);
			LinkedList<String> names = getCitedAuthorsName(author);
			// System.out.println(fullName);

			for (String fullName : names) {
				if (!citedAuthors.contains(fullName)) {
					citedAuthors.add(fullName);
				}

			}
		}
	}

	private static String getAuthorsName(Node node) {
		// do something with the current node instead of System.out
		return getAuthorsName(node, false, false, "").trim();
	}

	private static String getAuthorsName(Node node, boolean haveGiven,
			boolean haveFamily, String name) {

		// System.out.println(node.getNodeName());

		NodeList nodeList = node.getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node currentNode = nodeList.item(i);

			if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
				String tag = currentNode.getNodeName();
				if (tag.equals("GivenName")) {
					haveGiven = true;
					Node itemChild = currentNode.getFirstChild();
					name = name + itemChild.getNodeValue().trim();
				}

				if (tag.equals("FamilyName")) {
					haveFamily = true;
					Node itemChild = currentNode.getFirstChild();
					name = itemChild.getNodeValue().trim() + " " + name;
				}

				if (haveGiven && haveFamily) {
					return name;
				} else {
					getAuthorsName(currentNode, haveGiven, haveFamily, name);
				}
			}
		}
		return name;
	}// end iterateOverNodes

	// String familyExp
	// ="/Publisher/Journal/Volume/Issue/Article/ArticleHeader/AuthorGroup/Author/AuthorName/FamilyName";
	// String concatName = "concat(" + familyExp + ", " +
	// nameExp+")";

}
