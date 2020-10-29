package zerr.simulator;

import java.util.HashMap;

import lombok.Builder;
import lombok.Data;
import zerr.configuration.model.HardwareConfModel;

@Data
@Builder
public final class Hardware {
	private HashMap<Integer, Module> hashModule;
	private Controller controller;

	public static Hardware create(HardwareConfModel hardware) {
		HashMap<Integer, Module> hash = new HashMap<>();
		for (int i = 0; i < hardware.getModule().size(); i++)
			hash.put(i, Module.create(hardware.getModule().get(i)));
		
		return Hardware.builder()
				.hashModule(hash)
				.controller(Controller.create(hardware.getController()))
				.build();
	}
	
}