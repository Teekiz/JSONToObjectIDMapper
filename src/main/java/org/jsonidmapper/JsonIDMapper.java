package org.jsonidmapper;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.Setter;
@Getter
public class JsonIDMapper
{
	@Setter
	private int prefixLength;
	private HashMap<String, File> data;
	private final Properties filePath;

	private final Loader loader;
	private final Mapper mapper;

	public JsonIDMapper(String filesPath, int prefixLength){
		this.prefixLength = prefixLength;
		this.loader = new Loader();
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
