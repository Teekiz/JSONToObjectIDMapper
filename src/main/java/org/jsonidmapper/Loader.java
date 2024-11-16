package org.jsonidmapper;

import java.io.IOException;
import java.io.InputStream;
import lombok.extern.slf4j.Slf4j;
import java.util.Properties;

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
		try (InputStream input = getClass().getClassLoader().getResourceAsStream(propertiesPath))
		{
			if (input == null){
				log.warn("{} file not found in classpath.", propertiesPath);
			} else {
				log.info("Found {} file. Loading.", propertiesPath);
				properties.load(input);
			}
		} catch (IOException e) {
			log.error("Error loading {} file.", propertiesPath, e);
		}
		return properties;
	}
}
