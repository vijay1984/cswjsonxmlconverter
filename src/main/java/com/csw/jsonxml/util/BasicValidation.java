package com.csw.jsonxml.util;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.csw.exception.ConverterException;

public class BasicValidation {

	private static Logger LOGGER = Logger.getLogger(BasicValidation.class.getName());
	
	public static void validateInputFile(File jsonFilePath) throws ConverterException {
		if(!jsonFilePath.exists()) {
			LOGGER.log(Level.SEVERE, jsonFilePath.getAbsolutePath() + " does not exist");
			throw new ConverterException("File Does Not Exist");
		}
	}
	
	public static void checkEmptyJsonFile(File jsonFile) throws ConverterException {
		if(jsonFile.length() == 0) {
			LOGGER.log(Level.SEVERE, "File content is empty.");
			throw new ConverterException("Invalid File.");
		}
	}
	
}
