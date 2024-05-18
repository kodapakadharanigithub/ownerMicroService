package com.hcl.ownermicroservice.entities;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class QueryResponse {

	@Id
	private int queryId;
	private String query;
	private String answer;
	public QueryResponse()
	{
		
	}
	public QueryResponse(int queryId, String query, String answer) {
		super();
		this.queryId = queryId;
		this.query = query;
		this.answer = answer;
	}
	public int getQueryId() {
		return queryId;
	}
	public void setQueryId(int queryId) {
		this.queryId = queryId;
	}
	public String getQuery() {
		return query;
	}
	public void setQuery(String query) {
		this.query = query;
	}
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	@Override
	public String toString() {
		return "QueryResponse [queryId=" + queryId + ", query=" + query + ", answer=" + answer + "]";
	}
	
	
	 

}
