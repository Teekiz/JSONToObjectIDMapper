package org.jsonidmapper;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Loader
{
	/**
	 * A method used to get the reader file data.
	 * @return A {@link Properties} file.
	 */
	protected Properties getProperties(String propertiesPath)
	{
		Properties properties = new Properties();
		try (InputStream input = new FileInputStream(propertiesPath))
		{
			log.info("Found {} file. Loading.", propertiesPath);
			properties.load(input);
		} catch (IOException e) {
			if (e instanceof FileNotFoundException) {
				log.error("Cannot find file path properties file!");
			} else {
				log.error("Error loading {} file.", propertiesPath, e);
			}
		}
		return properties;
	}
}
