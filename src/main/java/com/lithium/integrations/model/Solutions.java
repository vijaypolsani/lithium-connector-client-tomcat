package com.lithium.integrations.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "response")
public class Solutions {

	private String value;

	public String getValue() {
		return value;
	}

	@XmlElement
	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "Solutions [value=" + value + "]";
	}

}
