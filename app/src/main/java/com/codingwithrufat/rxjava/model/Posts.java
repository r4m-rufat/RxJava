package com.codingwithrufat.rxjava.model;

import java.util.List;

public class Posts {
	private int id;
	private String title;
	private String body;
	private int userId;
	private List<Comments> comments;

	public int getId(){
		return id;
	}

	public String getTitle(){
		return title;
	}

	public String getBody(){
		return body;
	}

	public int getUserId(){
		return userId;
	}

	public List<Comments> getComments() {
		return comments;
	}

	public void setComments(List<Comments> comments) {
		this.comments = comments;
	}
}
