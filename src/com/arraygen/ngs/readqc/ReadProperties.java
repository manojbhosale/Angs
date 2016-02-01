package com.arraygen.ngs.readqc;

public class ReadProperties implements java.io.Serializable{

	private int readLength;
	private Double gcContent;

	public ReadProperties() {
		super();
		this.readLength = 0;
		this.gcContent = 0.0;
	}

	public ReadProperties(int readLength, Double gcContent) {
		super();
		this.readLength = readLength;
		this.gcContent = gcContent;
	}


	public int getReadLength() {
		return readLength;
	}
	public void setReadLength(int readLength) {
		this.readLength = readLength;
	}
	public Double getGcContent() {
		return gcContent;
	}
	public void setGcContent(Double gcContent) {
		this.gcContent = gcContent;
	}

}
