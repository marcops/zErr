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
		//2mod4bytesCRC8.json
		//2mod4bytesECCDualChannel
		ZErrConfModel zErrConfiguration = new ConfigurationService().load("2mod4bytesCRC8.json");
		Hardware hwd = Hardware.create(zErrConfiguration.getHardware());
		log.info("Hardware loaded = " + hwd.toString());
		
		OperationalSystem os = OperationalSystem.create(hwd);
		FailInjection fail = FailInjection.create(os);
		fail.start();
		
		HelloWordApp hl = new HelloWordApp(os);
		hl.exec();
		
		os.shutdown();
		fail.shutdown();
		
		log.info(Report.getInstance().getReport());
	}

}
