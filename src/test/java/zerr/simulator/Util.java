package zerr.simulator;

import zerr.configuration.ConfigurationService;
import zerr.configuration.model.ZErrConfModel;
import zerr.simulator.hardware.Hardware;

public class Util {

	public static Hardware loadConfig(String file) throws Exception {
		ZErrConfModel zErrConfiguration = new ConfigurationService().load(file);
		return Hardware.create(zErrConfiguration.getHardware());
	}
}
