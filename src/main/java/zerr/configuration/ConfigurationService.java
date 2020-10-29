package zerr.configuration;

import java.nio.file.Files;
import java.nio.file.Paths;

import com.fasterxml.jackson.databind.ObjectMapper;

import zerr.configuration.model.ZErrConfModel;

public class ConfigurationService {
	
	public ZErrConfModel load(String configuration) throws Exception {
		byte[] jsonData = Files.readAllBytes(
					Paths.get(getClass()
						.getClassLoader()
						.getResource(configuration).toURI()));

		return new ObjectMapper().readValue(jsonData, ZErrConfModel.class);
	}
}
