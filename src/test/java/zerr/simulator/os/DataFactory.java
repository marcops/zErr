package zerr.simulator.os;

import zerr.configuration.ConfigurationService;
import zerr.configuration.model.ZErrConfModel;
import zerr.simulator.hardware.Hardware;

public class DataFactory {

	public static Hardware create4bytesHardware() throws Exception {
		return loadConfig("test4bytes.json");
	}
	
	public static Hardware loadConfig(String file) throws Exception {
		ZErrConfModel zErrConfiguration = new ConfigurationService().load(file);
		return Hardware.create(zErrConfiguration.getHardware());
	}
}
