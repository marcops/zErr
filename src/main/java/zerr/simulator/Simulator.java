package zerr.simulator;

import zerr.configuration.ConfigurationService;
import zerr.configuration.model.ZErrConfModel;
import zerr.test.HelloWordApp;

public class Simulator {
	private ZErrConfModel zErrConfiguration;

	public void run() throws Exception {
		zErrConfiguration = new ConfigurationService().load("128_MT9VDDT1672A.json");
		
		Hardware hwd = Hardware.create(zErrConfiguration.getHardware());
		System.out.println(hwd);
		OperationalSystem os = OperationalSystem.create(hwd);
		
		
		HelloWordApp hl = new HelloWordApp(os);
		hl.exec();
	}

}
