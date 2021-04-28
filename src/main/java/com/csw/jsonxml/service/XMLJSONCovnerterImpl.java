package com.csw.jsonxml.service;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonString;
import javax.json.JsonValue;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.csw.exception.ConverterException;
import com.csw.jsonxml.ConverterFactory;
import com.csw.jsonxml.XMLJSONConverterI;

public class XMLJSONCovnerterImpl implements XMLJSONConverterI {
	
	private static final Logger LOGGER = Logger.getLogger(XMLJSONCovnerterImpl.class.getName());
	
	private Document xmlDoc = null;
	
	public void convertJSONtoXML(File jsonFilePath, File xmlFile) throws ConverterException {
		LOGGER.log(Level.INFO, "Start convertJSONtoXML");
		try {
			String inputJson = readJsonAsString(jsonFilePath);
			JsonValue jsonValueAsObject = parseJson(inputJson);
			xmlDoc = createXmlDocument();
			this.convertJsonValueToXml(jsonValueAsObject, null, null);
			writeToFile(xmlDoc, xmlFile);
		}
		catch(ConverterException exec) {
			LOGGER.log(Level.SEVERE, exec.getMessage());
			throw exec;
		}
		LOGGER.log(Level.INFO, "End convertJSONtoXML");
	}

	private String readJsonAsString(File jsonFile) throws ConverterException {
		StringBuilder fileBuilder = new StringBuilder();
		try(BufferedReader reader = new BufferedReader(new FileReader(jsonFile))) {
			String line = null;
			while((line = reader.readLine()) != null) {
				fileBuilder.append(line);
			}
		}
		catch(IOException ioex) {
			throw new ConverterException(ioex.getMessage(), ioex);
		}
		return fileBuilder.toString();
	}
		
	
	public JsonValue parseJson(String inputJson) throws ConverterException {
		JsonValue jsonAsObject = null;
		try(BufferedInputStream bufferedIs = new BufferedInputStream(new ByteArrayInputStream(inputJson.getBytes()))) {
			JsonReader jsonReader = Json.createReader(bufferedIs);
			jsonAsObject = jsonReader.read();
		}
		catch(IOException ioe) {
			throw new ConverterException(ioe.getMessage(), ioe);
		}
		return jsonAsObject;
	}
	
	public Document createXmlDocument() throws ConverterException {
		DocumentBuilder builder = ConverterFactory.getDocumentBuilder();
		Document doc = builder.newDocument();
		return doc;
	}
		
	private void convertJsonValueToXml(JsonValue jsonValue, String jsonKey, Element xmlElement) {
		switch(jsonValue.getValueType()) {
			case OBJECT:
				Element objectElement = xmlDoc.createElement("object");
				if(xmlElement == null) {
					xmlDoc.appendChild(objectElement); // If parent is null, this is considered as root element.
				}
				else {
					xmlElement.appendChild(objectElement);
				}
				if(jsonKey != null) {
					Attr attr = xmlDoc.createAttribute("name");
					attr.setValue(jsonKey);
					objectElement.setAttributeNode(attr);
				}
				JsonObject jsonObject = (JsonObject) jsonValue;
				for(String eachJsonKey : jsonObject.keySet()) {
					convertJsonValueToXml(jsonObject.get(eachJsonKey), eachJsonKey, objectElement);
				}
				break;
				
			case ARRAY:	
				Element arrayElement = xmlDoc.createElement("array");
				if(xmlElement == null) {
					xmlDoc.appendChild(arrayElement); // If parent is null, this is considered as root element.
				}
				else {
					xmlElement.appendChild(arrayElement);
				}
				if(jsonKey != null) {
					Attr attr = xmlDoc.createAttribute("name");
					attr.setValue(jsonKey);
					arrayElement.setAttributeNode(attr);
				}
				JsonArray jsonArray = (JsonArray) jsonValue;
				for(JsonValue eachArrayValue : jsonArray) {
					convertJsonValueToXml(eachArrayValue, null, arrayElement);
				}
				break;
				
			case STRING:
				Element stringElement = xmlDoc.createElement("string");
				JsonString stringValue = (JsonString) jsonValue;
				stringElement.appendChild(xmlDoc.createTextNode(stringValue.getString()));
				xmlElement.appendChild(stringElement);
				if(jsonKey != null) {
					Attr attr = xmlDoc.createAttribute("name");
					attr.setValue(jsonKey);
					stringElement.setAttributeNode(attr);
				}
				break;
				
			case NUMBER:
				Element numberElement = xmlDoc.createElement("number");
				JsonNumber numberValue = (JsonNumber) jsonValue;
				numberElement.appendChild(xmlDoc.createTextNode(numberValue.toString()));
				xmlElement.appendChild(numberElement);
				if(jsonKey != null) {
					Attr attr = xmlDoc.createAttribute("name");
					attr.setValue(jsonKey);
					numberElement.setAttributeNode(attr);
				}
				break;
				
			case FALSE:
				Element booleanFalseElement = xmlDoc.createElement("boolean");
				booleanFalseElement.appendChild(xmlDoc.createTextNode("false"));
				xmlElement.appendChild(booleanFalseElement);
				if(jsonKey != null) {
					Attr attr = xmlDoc.createAttribute("name");
					attr.setValue(jsonKey);
					booleanFalseElement.setAttributeNode(attr);
				}
				break;
			
			case TRUE:
				Element booleanTrueElement = xmlDoc.createElement("boolean");
				booleanTrueElement.appendChild(xmlDoc.createTextNode("true"));
				xmlElement.appendChild(booleanTrueElement);
				if(jsonKey != null) {
					Attr attr = xmlDoc.createAttribute("name");
					attr.setValue(jsonKey);
					booleanTrueElement.setAttributeNode(attr);
				}
				break;
			
			case NULL:
				Element nullElement = xmlDoc.createElement("null");
				xmlElement.appendChild(nullElement);
				if(jsonKey != null) {
					Attr attr = xmlDoc.createAttribute("name");
					attr.setValue(jsonKey);
					nullElement.setAttributeNode(attr);
				}
				break;
		}
	}
	
	private void writeToFile(Document doc, File xmlFile) throws ConverterException {
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(xmlFile);
		try {
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.transform(source, result);
			LOGGER.log(Level.INFO, "File successfully written to: " + xmlFile.getAbsolutePath());
		}
		catch(TransformerException tfex) {
			throw new ConverterException(tfex.getMessage(), tfex);
		}
	}

}
