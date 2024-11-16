import java.io.File;
import org.jsonidmapper.JsonIDMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class JsonIDMapperTest
{
	@Test
	public void testJsonIDMapper(){
		JsonIDMapper jsonIDMapper = new JsonIDMapper("filepath.properties", 5, "src/test/resources/storagepath.properties");
		File file = jsonIDMapper.getFileByName("STRANGEINNUMBERS").getValue();
		assertNotNull(file);
	}
}
