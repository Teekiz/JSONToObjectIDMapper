package org.jsonidmapper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
public class Mapper
{
	private final int prefixLength;

	/**
	 * The constructor for a {@link Mapper} object.
	 * @param prefixLength The maximum length of the ID prefix.
	 */
	public Mapper(int prefixLength){
		this.prefixLength = prefixLength;
	}

	/**
	 * A method used to load the file paths and assign an ID to {@code data}.
	 * @param paths A {@link Properties} file containing the name of the data and the file path.
	 * @param data A {@link Map} of {@link String} and {@link File} containing previously loaded files.
	 * @return A {@link Map} with the ID ({@link String}) and the {@link File} associated with the ID.
	 */
	protected Map<String, File> getData(Properties paths, Map<String, File> data)
	{
		try {
			if (paths != null && data != null){
				for (Map.Entry<Object, Object> entry : paths.entrySet()) {
					String type = (String) entry.getKey();
					String prefix = getPrefix(type);
					File directory = new File((String) entry.getValue());

					List<File> files = getAllJSONFiles(directory);

					if (!files.isEmpty()){

						//sets the ID to the last occurrence found
						AtomicInteger idNumber = new AtomicInteger(1);

						files.forEach(file -> {
							String id = prefix + idNumber.getAndIncrement();
							while (data.containsKey(id)) {
								id = prefix + idNumber.getAndIncrement();
							}

							if (!data.containsValue(file)) {
								data.put(id.toUpperCase(), file);
								log.debug("Added file with ID: {}", id);
							} else {
								log.debug("Skipped duplicate file: {}", file.getPath());
							}
						});
					} else {
						log.warn("Directory does not contain any JSON files.");
					}
				}
			}
		}
		catch (Exception e)
		{
			log.error("Cannot load data.", e);
			throw new RuntimeException("Error loading data", e);
		}
		return data;
	}

	/**
	 * A method to find all JSON files in a directory
	 * @param directory The root directory to be searched.
	 * @return All .JSON files found within the directory.
	 */
	private List<File> getAllJSONFiles(File directory)
	{
		try(Stream<Path> walk = Files.walk(directory.toPath()))
		{
			return walk
				.filter(Files::isRegularFile)
				.filter(path -> path.toString().toUpperCase().endsWith(".json".toUpperCase()))
				.map(Path::toFile)
				.collect(Collectors.toList());
		} catch (IOException e) {
			log.error("Cannot read JSON files from path {}", directory);
			return new ArrayList<>();
		}
	}

	/**
	 * A method used to get the prefix for the file type.
	 * @param name The name of the file type being provided.
	 * @return A prefix for an ID determined by {@code prefixLength}
	 */
	private String getPrefix(String name){
		int nameLength = Math.min(name.length(), prefixLength);
		StringBuilder prefixBuilder = new StringBuilder(name.substring(0, nameLength));

		int remainingLength = prefixLength - prefixBuilder.length();
		if (remainingLength > 0) {
			prefixBuilder.append("#".repeat(remainingLength));
		}

		return prefixBuilder.toString();
	}
}
