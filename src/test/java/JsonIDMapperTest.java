import java.io.File;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.jsonidmapper.JsonIDMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Slf4j
public class JsonIDMapperTest
{
	@Test
	public void testJsonIDMapper(){
		log.debug("TEST - testJsonIDMapper");
		JsonIDMapper jsonIDMapper = new JsonIDMapper("src/test/resources/filepath.properties", "src/test/resources/storagepath.properties", true);
		Map.Entry<String, File> entry = jsonIDMapper.getFileByName("STRANGEINNUMBERS");
		assertNotNull(entry);
	}

	@Test
	public void testPrefixes(){
		log.debug("TEST - testPrefixes");
		JsonIDMapper jsonIDMapper = new JsonIDMapper("src/test/resources/filepath.properties", "src/test/resources/storagepath.properties", true);
		List<String> prefixes = jsonIDMapper.getPrefixes();
		assertFalse(prefixes.isEmpty());
		assertNotNull(jsonIDMapper.getFilesFromPrefix(prefixes.getFirst()));
	}

	@Test
	public void testFileName(){
		log.debug("TEST - testFileName");
		JsonIDMapper jsonIDMapper = new JsonIDMapper("src/test/resources/filepath.properties", "src/test/resources/storagepath.properties", true);
		File file = jsonIDMapper.getFileByName("GAUSSRIFLE").getValue();
		assertNotNull(file);

		File fileLowerCase = jsonIDMapper.getFileByName("gaussrifle").getValue();
		assertNotNull(fileLowerCase);
	}
}
