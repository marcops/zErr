package zerr.configuration;

import java.nio.file.Files;
import java.nio.file.Paths;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;

import zerr.configuration.model.ZErrConfModel;

public class ConfigurationService {
	
	public ZErrConfModel load(String configuration) throws Exception {
		byte[] jsonData = Files.readAllBytes(
					Paths.get(getClass()
						.getClassLoader()
						.getResource(configuration).toURI()));

		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
		return objectMapper.readValue(jsonData, ZErrConfModel.class);
	}
}
