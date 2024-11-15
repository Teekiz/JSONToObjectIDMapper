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

	/**
	 * The constructor for a {@link JsonIDMapper} object.
	 * @param filesPath A {@link String} value containing the path to the properties file.
	 * @param prefixLength A {@link Integer} value sets the maximum length of the prefix for the ID.
	 *                     If path names do not match the length, they will either be reduced to the length
	 *                     or extended by padding the prefix with {@code #}.
	 */
	public JsonIDMapper(String filesPath, int prefixLength){
		this.prefixLength = prefixLength;
		Loader loader = new Loader();
		this.mapper = new Mapper(prefixLength);
		this.filePath = loader.getProperties(filesPath);
		this.data = mapper.getData(filePath);
	}

	/**
	 * A method that can be called if changes to the data have been made.
	 */
	public void updateMappedData(){
		this.data = mapper.getData(filePath);
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
	 * A method used to gather all files containing the same prefix as the id.
	 * @param id The prefix of the data to be returned.
	 * @return A {@link HashMap} of {@link String} and {@link File} containing the IDs and file paths for the data.
	 */
	public HashMap<String, File> getFileTypesFromData(String id){
		String finalId = id.substring(0, prefixLength);
		return data.entrySet().stream()
			.filter(file -> file.getKey().startsWith(finalId))
			.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, _) -> oldValue, HashMap::new));
	}
}
