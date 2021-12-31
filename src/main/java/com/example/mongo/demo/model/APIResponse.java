package com.example.mongo.demo.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class APIResponse {
    private String message;

    public APIResponse() {}
    
    public APIResponse(String message) {
    	this.message = message;
    }

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}