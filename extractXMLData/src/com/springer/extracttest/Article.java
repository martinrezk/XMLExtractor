package com.springer.extracttest;

import java.util.LinkedList;

public class Article {
	String doi;
	LinkedList<String> citedDoi;
	public Article(String doi, LinkedList<String> citedDoi) {
		super();
		this.doi = doi;
		this.citedDoi = citedDoi;
	}
	public LinkedList<String> getCitedDois() {
		return citedDoi;
	}
	
	public String getDoi() {
		return doi;
	}
	
	public void setCitedDoi(LinkedList<String> citedDoi) {
		this.citedDoi.addAll(citedDoi);
	}
	

}
