package com.hcl.ownermicroservice.dto;




public class QueryRequest {
	
	private int queryId;
	private String query;
	private String vehicleId;
	private int userId;
	private String status;
	public QueryRequest()
	{
		
	}
	public QueryRequest( String query, String vehicleId, int userId, String status) {
		super();
		this.query = query;
		this.vehicleId = vehicleId;
		this.userId = userId;
		this.status = status;
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
	public String getVehicleId() {
		return vehicleId;
	}
	public void setVehicleId(String vehicleId) {
		this.vehicleId = vehicleId;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	@Override
	public String toString() {
		return "QueryRequest [queryId=" + queryId + ", query=" + query + ", vehicleId=" + vehicleId + ", userId="
				+ userId + ", status=" + status + "]";
	}
	
	

}
