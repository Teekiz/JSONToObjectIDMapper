package org.jsonidmapper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class Storage
{
	private Properties storedValues;
	private String path;

	/**
	 * The constructor for a {@link Storage} object.
	 * @param storagePath The path to where the data is stored.
	 * @param loader The {@link Loader} object used to load properties.
	 */
	public Storage(String storagePath, Loader loader){
		this.path = storagePath;
		this.storedValues = loader.getProperties(path);

		if (storedValues.isEmpty()){
			this.storedValues = new Properties();
			saveFile();
		}
	}

	/**
	 * A method to create a properties file if one is not found at the existing path.
	 */
	private void saveFile(){
		try (FileOutputStream outputStream = new FileOutputStream(path)) {
			storedValues.store(outputStream, "File Paths");
			log.info("{} created successfully.", path);
		} catch (IOException e) {
			log.error("Could not create storage files at path: {}", path, e);
		}
	}

	/**
	 * A method used to load the data from {@code storedValues} into a {@link HashMap} object.
	 * @return A {@link HashMap} of {@link String} and {@link File}.
	 */
	public HashMap<String, File> loadData(){
		HashMap<String, File> data = new HashMap<>();
		if (storedValues != null){
			for (Map.Entry<Object, Object> entry : storedValues.entrySet()) {
				String key = (String) entry.getKey();
				String value = (String) entry.getValue();

				if (value != null && !value.isEmpty()) {
					File file = new File(value);
					if (file.exists()){
						data.put(key, file);
					}
				}
			}
		}
		return data;
	}

	/**
	 * A method used to convert data from a {@link HashMap} object into a {@link Properties file}.
	 * @param data A {@link HashMap} of {@link String} and {@link File} containing the mapped IDs and Files.
	 */
	public void saveData(HashMap<String, File> data){
		if (storedValues != null){
			for (Map.Entry<String, File> entry : data.entrySet()){
				storedValues.setProperty(entry.getKey(), entry.getValue().getPath());
			}
			saveFile();
		}
	}
}
