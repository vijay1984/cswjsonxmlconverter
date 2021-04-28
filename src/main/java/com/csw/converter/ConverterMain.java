package com.csw.converter;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.csw.exception.ConverterException;
import com.csw.jsonxml.ConverterFactory;
import com.csw.jsonxml.XMLJSONConverterI;
import com.csw.jsonxml.util.BasicValidation;

public class ConverterMain {
	
	private static Logger LOGGER = Logger.getLogger(ConverterMain.class.getName());

	public static void main(String[] args) throws ConverterException {
		try {
			if(args.length != 2) {
				LOGGER.log(Level.SEVERE, "Number of arguments must be 2. Json File Path and XML File Path");
				return;
			}
			File jsonFile = new File(args[0]);
			File xmlFile = new File(args[1]);
			validateInputFile(jsonFile);
			checkEmptyJsonFile(jsonFile);
			XMLJSONConverterI converter = ConverterFactory.createXmlJsonConverter();
			converter.convertJSONtoXML(jsonFile, xmlFile);
		}
		catch(ConverterException converterExc) {
			throw converterExc;
		}
	}
	
	private static void validateInputFile(File jsonFile) throws ConverterException {
		try {
			BasicValidation.validateInputFile(jsonFile);
			LOGGER.log(Level.INFO, "JSON File Exists");
		}
		catch(ConverterException cvex) {
			throw cvex;
		}
	}
	
	private static void checkEmptyJsonFile(File jsonFile) throws ConverterException {
		try {
			BasicValidation.checkEmptyJsonFile(jsonFile);
			System.out.println(jsonFile.length());
			LOGGER.log(Level.INFO, "JSON File is valid");
		}
		catch(ConverterException cvex) {
			throw cvex;
		}
	}

}
