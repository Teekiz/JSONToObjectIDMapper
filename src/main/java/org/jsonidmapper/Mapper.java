package org.jsonidmapper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
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
					String prefix = (String) entry.getKey();
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
				.sorted(Comparator.comparing(File::getName))
				.collect(Collectors.toList());
		} catch (IOException e) {
			log.error("Cannot read JSON files from path {}", directory);
			return new ArrayList<>();
		}
	}

	/**
	 * A method used to create a list of prefixes from the filepaths properties file.
	 * @param paths The properties file containing the file paths to the data.
	 * @return A {@link List} of {@link String} prefixes for each file path found in the {@code paths} {@link Properties} file.
	 */
	protected List<String> getPrefixes(Properties paths){
		List<String> prefixes = new ArrayList<>();
		if (paths != null) {
			for (Object prefix : paths.keySet()) {
				prefixes.add((String) prefix);
			}
		}
		log.info("Found {} paths. Prefixes: {}", prefixes.size(), prefixes);
		return prefixes;
	}
}
