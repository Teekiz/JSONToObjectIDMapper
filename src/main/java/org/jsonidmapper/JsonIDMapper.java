package org.jsonidmapper;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.io.FilenameUtils;

public class JsonIDMapper
{
	@Getter
	private List<String> prefixes;
	private Map<String, File> data;
	private final Properties filePath;
	private final Mapper mapper;
	private final Storage storage;

	/**
	 * The constructor for a {@link JsonIDMapper} object.
	 * @param filesPath A {@link String} value containing the absolute path to the properties file.
	 * @param storagePath A {@link String} value containing the absolute path to the location where the data should be stored locally.
	 *                    Set to {@code null} to avoid saving locally.
	 * @param deleteMissingFiles A {@link Boolean} value used to determine if missing files should be included when mapping
	 *                            files to IDs. If {@code true} then the existing ID and file will be deleted. If {@code false}
	 *                            then the ID will be preserved. WARNING: If the ID is deleted, then this may lead to unintended side effects
	 *                            for existing systems.
	 */
	public JsonIDMapper(String filesPath, String storagePath, boolean deleteMissingFiles){
		Loader loader = new Loader();
		this.mapper = new Mapper();

		if (storagePath != null && !storagePath.isEmpty()){
			this.storage = new Storage(storagePath, loader, deleteMissingFiles);
			this.data = storage.loadData();
		} else {
			this.data = new HashMap<>();
			this.storage = null;
		}
		this.filePath = loader.getProperties(filesPath);
		this.data = mapper.getData(filePath, data);
		this.prefixes = mapper.getPrefixes(filePath);
		saveData();
	}

	/**
	 * A method that can be called if changes to the data have been made.
	 */
	public void updateMappedData(){
		this.data = mapper.getData(filePath, data);
		this.prefixes = mapper.getPrefixes(filePath);
		saveData();
	}

	/**
	 * A method used to save data after all the data has been loaded.
	 */
	private void saveData(){
		if (storage != null){
			storage.saveData(data);
		}
	}

	/**
	 * Get a file based on the ID provided.
	 * @param id The ID of the file to retrieve
	 * @return A {@link File} based on the ID.
	 */
	public File getFileFromID(String id){
		return data.get(id);
	}

	/**
	 * A method used to gather all files containing the same prefix as the prefix.
	 * @param prefix The prefix of the data to be returned.
	 * @return A {@link HashMap} of {@link String} and {@link File} containing the IDs and file paths for the data.
	 */
	public HashMap<String, File> getFilesFromPrefix(String prefix){
		return data.entrySet().stream()
			.filter(file -> file.getKey().startsWith(prefix.toUpperCase()))
			.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, _) -> oldValue, HashMap::new));
	}

	/**
	 * A method used to find a file with the given name.
	 * @param fileName The name of the file to find.
	 * @return A {@link Map.Entry} with the {@link File} and its {@link String ID}.
	 */
	public Map.Entry<String, File> getFileByName(String fileName){
		return data.entrySet()
			.stream()
			.filter(file -> StringUtils.containsIgnoreCase(file.getValue().getName(), fileName))
			.findFirst().orElse(null);
	}

	/**
	 * A method used to find the name of a file, and return the corresponding ID.
	 * @param fileName The name of the file used to get the corresponding ID (without the extension).
	 * @return The ID of the file.
	 */
	public String getIDFromFileName(String fileName)
	{
		return data.entrySet()
			.stream()
			.filter(entry -> FilenameUtils.removeExtension(entry.getValue().getName()).equalsIgnoreCase(fileName))
			.map(Map.Entry::getKey)
			.findFirst()
			.orElse("");
	}
}
