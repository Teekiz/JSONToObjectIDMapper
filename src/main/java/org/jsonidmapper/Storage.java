package org.jsonidmapper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;
import lombok.Data;

@Data
public class Storage
{
	private Properties storedValues;
	private String path;
	private boolean deleteMissingFiles;

	/**
	 * The constructor for a {@link Storage} object.
	 * @param storagePath The path to where the data is stored.
	 * @param loader The {@link Loader} object used to load properties.
	 * @param deleteMissingFiles A {@link Boolean} used to determine if IDs with invalid file paths should be preserved.
	 */
	public Storage(String storagePath, Loader loader, boolean deleteMissingFiles){
		this.path = storagePath;
		this.deleteMissingFiles = deleteMissingFiles;
		this.storedValues = loader.getProperties(path);

		if (storedValues.isEmpty()){
			this.storedValues = new Properties();
			saveFile();
		}
	}

	/**
	 * A method used to load the data from {@code storedValues} into a {@link Map} object.
	 * @return A {@link Map} of {@link String} and {@link File}.
	 */
	public Map<String, File> loadData(){
		Map<String, File> data = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
		if (storedValues != null){
			for (Map.Entry<Object, Object> entry : storedValues.entrySet()) {
				String key = (String) entry.getKey();
				String value = (String) entry.getValue();
				if (value != null && !value.isEmpty()) {
					File file = new File(value);

					if (!deleteMissingFiles || file.exists()){
						data.put(key, file);
					}
				}
			}
		}
		return data;
	}

	/**
	 * A method used to convert data from a {@link Map} object into a {@link Properties file}.
	 * @param data A {@link Map} of {@link String} and {@link File} containing the mapped IDs and Files.
	 */
	public void saveData(Map<String, File> data){
		storedValues = new Properties();
			for (Map.Entry<String, File> entry : data.entrySet()){
				storedValues.setProperty(entry.getKey(), entry.getValue().getPath());
			}
			saveFile();
	}

	/**
	 * A method to create a properties file if one is not found at the existing path.
	 */
	private void saveFile(){
		try (FileOutputStream outputStream = new FileOutputStream(path)) {
			storedValues.store(outputStream, "File Paths");
			//log.info("{} created successfully.", path);
		} catch (IOException e) {
			//log.error("Could not create storage files at path: {}", path, e);
		}
	}
}
