package org.jsonidmapper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public class Mapper
{
	private final int prefixLength;

	public Mapper(int prefixLength){
		this.prefixLength = prefixLength;
	}

	/**
	 * A method used to load the file paths and assign an ID to {@code data}.
	 * @param paths A {@link Properties} file containing the name of the data and the file path.
	 * @return A {@link HashMap} with the ID ({@link String}) and the {@link File} associated with the ID.
	 */
	protected HashMap<String, File> getData(Properties paths)
	{
		HashMap<String, File> data = new HashMap<>();
		try {
			if (paths != null){
				for (Map.Entry<Object, Object> entry : paths.entrySet()) {
					String type = (String) entry.getKey();
					File directory = new File((String) entry.getValue());

					List<File> files = getAllJSONFiles(directory);

					if (!files.isEmpty()){
						String prefix = getPrefix(type);
						AtomicInteger idNumber = new AtomicInteger();

						files.forEach(file -> {
							String id = prefix + idNumber.getAndIncrement();
							data.put(id, file);
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
	protected List<File> getAllJSONFiles(File directory)
	{
		try(Stream<Path> walk = Files.walk(directory.toPath()))
		{
			return walk
				.filter(Files::isRegularFile)
				.filter(path -> path.toString().endsWith(".json"))
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
	protected String getPrefix(String name){
		int nameLength = Math.min(name.length(), prefixLength);
		StringBuilder prefixBuilder = new StringBuilder(name.substring(0, nameLength));

		int remainingLength = prefixLength - prefixBuilder.length();
		if (remainingLength > 0) {
			prefixBuilder.append("#".repeat(remainingLength));
		}

		return prefixBuilder.toString();
	}
}
