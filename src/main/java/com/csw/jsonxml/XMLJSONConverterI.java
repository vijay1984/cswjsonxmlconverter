package com.csw.jsonxml;

import java.io.File;

import com.csw.exception.ConverterException;

public interface XMLJSONConverterI {

	public void convertJSONtoXML(File jsonFilePath, File xmlFilePath) throws ConverterException;
	
}
