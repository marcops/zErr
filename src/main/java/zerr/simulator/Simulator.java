package zerr.simulator;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import zerr.configuration.ConfigurationService;
import zerr.configuration.model.ZErrConfModel;
import zerr.simulator.hardware.Hardware;
import zerr.simulator.os.OperationalSystem;
import zerr.test.HelloWordApp;

@Slf4j
@Builder
public final class Simulator {
//	private ZErrConfModel zErrConfiguration;
//	private Hardware hardware;
	private OperationalSystem operationalSystem;
	private FaultInjection faultInjection;
	
	//"2mod4bytesECC.json"
	public static Simulator create(String configFile) throws Exception {
		ZErrConfModel zerr = new ConfigurationService().load(configFile);
		log.info("Config loaded = " + zerr.toString());
		
		Hardware hwd = Hardware.create(zerr.getHardware());
		FaultInjection fault = FaultInjection.create(zerr.getSimulator(), hwd);
		return Simulator.builder()
//				.zErrConfiguration(zerr)
//				.hardware(hwd)
				.operationalSystem(OperationalSystem.create(hwd))
				.faultInjection(fault)
				.build();
	}
	
	public void run() throws InterruptedException {
		faultInjection.start();
		try {
			HelloWordApp hl = new HelloWordApp(operationalSystem);
			hl.exec();
		} finally {
			operationalSystem.shutdown();
			faultInjection.shutdown();
		}
		
		log.info(Report.getInstance().getReport());
	}

}
