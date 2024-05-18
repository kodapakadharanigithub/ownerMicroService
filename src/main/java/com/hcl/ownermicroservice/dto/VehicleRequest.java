package com.hcl.ownermicroservice.dto;

public class VehicleRequest {
	

	private String vehicleId;
	private String model;
	private String type;
	public VehicleRequest()
	{
		
	}
	public VehicleRequest(String vehicleId, String model, String type) {
		super();
		this.vehicleId = vehicleId;
		this.model = model;
		this.type = type;
	}
	public String getVehicleId() {
		return vehicleId;
	}
	public void setVehicleId(String vehicleId) {
		this.vehicleId = vehicleId;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	@Override
	public String toString() {
		return "VehicleRequest [vehicleId=" + vehicleId + ", model=" + model + ", type=" + type + "]";
	}
	

	
}
