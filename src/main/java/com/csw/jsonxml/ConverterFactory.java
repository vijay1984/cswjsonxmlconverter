package com.csw.jsonxml;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import com.csw.exception.ConverterException;
import com.csw.jsonxml.service.XMLJSONCovnerterImpl;

public class ConverterFactory {
	
	private static DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();

	public static DocumentBuilder getDocumentBuilder() throws ConverterException {
		try {
			return docFactory.newDocumentBuilder();
		}
		catch(ParserConfigurationException exec) {
			throw new ConverterException(exec.getMessage(), exec);
		}
	}
	
	public static XMLJSONConverterI createXmlJsonConverter() {
		return new XMLJSONCovnerterImpl();
	}
}
