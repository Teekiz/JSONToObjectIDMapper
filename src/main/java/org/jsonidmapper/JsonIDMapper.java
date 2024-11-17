package org.jsonidmapper;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;
public class JsonIDMapper
{
	private final int prefixLength;
	private HashMap<String, File> data;
	private final Properties filePath;
	private final Mapper mapper;
	private final Storage storage;

	/**
	 * The constructor for a {@link JsonIDMapper} object.
	 * @param filesPath A {@link String} value containing the absolute path to the properties file.
	 * @param prefixLength A {@link Integer} value sets the maximum length of the prefix for the ID.
	 *                     If path names do not match the length, they will either be reduced to the length
	 *                     or extended by padding the prefix with {@code #}.
	 * @param storagePath A {@link String} value containing the absolute path to the location where the data should be stored locally.
	 *                    Set to {@code null} to avoid saving locally.
	 * @param deleteMissingFiles A {@link Boolean} value used to determine if missing files should be included when mapping
	 *                            files to IDs. If {@code true} then the existing ID and file will be deleted. If {@code false}
	 *                            then the ID will be preserved. WARNING: If the ID is deleted, then this may lead to unintended side effects
	 *                            for existing systems.
	 */
	public JsonIDMapper(String filesPath, int prefixLength, String storagePath, boolean deleteMissingFiles){
		this.prefixLength = prefixLength;
		Loader loader = new Loader();
		this.mapper = new Mapper(prefixLength);

		if (storagePath != null && !storagePath.isEmpty()){
			this.storage = new Storage(storagePath, loader, deleteMissingFiles);
			this.data = storage.loadData();
		} else {
			this.data = new HashMap<>();
			this.storage = null;
		}
		this.filePath = loader.getProperties(filesPath);
		this.data = mapper.getData(filePath, data);
		saveData();
	}

	/**
	 * A method that can be called if changes to the data have been made.
	 */
	public void updateMappedData(){
		this.data = mapper.getData(filePath, data);
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
		String finalId = prefix.substring(0, prefixLength);
		return data.entrySet().stream()
			.filter(file -> file.getKey().startsWith(finalId))
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
			.filter(file -> file.getValue().getName().contains(fileName))
			.findFirst().orElse(null);
	}
}
