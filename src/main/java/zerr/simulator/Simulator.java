package zerr.simulator;

import lombok.extern.slf4j.Slf4j;
import zerr.configuration.ConfigurationService;
import zerr.configuration.model.ZErrConfModel;
import zerr.simulator.hardware.Hardware;
import zerr.simulator.os.OperationalSystem;
import zerr.test.HelloWordApp;

@Slf4j
public final class Simulator {
	private Simulator() {}
	
	public static void run() throws Exception {
		ZErrConfModel zErrConfiguration = new ConfigurationService().load("2mod4bytesCRC8.json");
		Hardware hwd = Hardware.create(zErrConfiguration.getHardware());
		log.info(hwd.toString());

		OperationalSystem os = OperationalSystem.create(hwd);
		
		HelloWordApp hl = new HelloWordApp(os);
		hl.exec();
		
		os.shutdown();
	}

}
