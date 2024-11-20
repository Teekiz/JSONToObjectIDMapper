import java.io.File;
import java.util.Map;
import org.jsonidmapper.JsonIDMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class JsonIDMapperTest
{
	@Test
	public void testJsonIDMapper(){
		JsonIDMapper jsonIDMapper = new JsonIDMapper("src/test/resources/filepath.properties", 5, "src/test/resources/storagepath.properties", true);
		Map.Entry<String, File> entry = jsonIDMapper.getFileByName("STRANGEINNUMBERS");
		assertNotNull(entry);
	}

	@Test
	public void testJsonIDMapperGet(){
		JsonIDMapper jsonIDMapper = new JsonIDMapper("src/test/resources/filepath.properties", 5, "src/test/resources/storagepath.properties", true);
		File file = jsonIDMapper.getFileFromID("PERKD1");
		assertNotNull(file);
	}
}
