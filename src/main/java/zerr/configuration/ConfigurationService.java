package zerr.configuration;

import java.nio.file.Files;
import java.nio.file.Paths;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Data;
import zerr.configuration.model.ZErrConfModel;

@Data
public class ConfigurationService {
	
	private static final ConfigurationService INSTANCE = new ConfigurationService();
	private ZErrConfModel zErrConfModel;
	
	private ConfigurationService() {}
	public static ConfigurationService getInstance() {
		return INSTANCE;
	}
	
	public ZErrConfModel load(String configuration) throws Exception {
		byte[] jsonData = Files.readAllBytes(
					Paths.get(getClass()
						.getClassLoader()
						.getResource("configs/"+configuration).toURI()));

		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
		zErrConfModel = objectMapper.readValue(jsonData, ZErrConfModel.class);
		return zErrConfModel;
	}
}
