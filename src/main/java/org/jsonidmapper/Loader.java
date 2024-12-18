package org.jsonidmapper;

import java.io.FileInputStream;
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
			log.error("Error loading {} file.", propertiesPath, e);
		}
		return properties;
	}
}
