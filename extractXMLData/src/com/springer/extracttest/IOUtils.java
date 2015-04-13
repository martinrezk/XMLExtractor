package com.springer.extracttest;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class IOUtils {
	static String path;
	

	public IOUtils(String path) {
		super();
		this.path = path;
	}
	
	/**
	 * This methods returns the list of files in a given directory
	 * @param articlePath
	 * @return
	 */
	private static  File[] getFilesFromDirectory() {
		File filePath = new File(path);

		File[] articleFiles = null;

	        if (filePath.isDirectory()) {
	            System.out.println("Working Directory with XML articles: " + filePath.getName());
	            articleFiles = filePath.listFiles();
	        } else {
	            System.out.println("You must enter a valid Directory. For instance /Users/mrezk/Downloads/test-articles/ ");
	        }
	    
		return  articleFiles;
	}
	
	public static  LinkedList<Document> getDocsFromDirectory() throws SAXException, IOException {
		File[] articleList = getFilesFromDirectory();
		
		LinkedList<Document> docs =new LinkedList<Document>();;
		
		DocumentBuilderFactory factory = DocumentBuilderFactory
				.newInstance();
		factory.setValidating(false);
	
		for (File xmlFile : articleList) {
			
			try {
				DocumentBuilder builder = factory.newDocumentBuilder();
				Document doc = builder.parse(xmlFile);
				docs.add(doc);
			} catch (ParserConfigurationException e) {
				System.out.println("Bad XML");

			} catch (SAXException e) {
				System.out.println("Bad SAX");
				
			}catch (IOException e) {
				System.out.println("Bad File");

			}
		}//end for

		return docs;
		
	}

	
}
	
