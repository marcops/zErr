package zerr.simulator.hardware;

import java.util.HashMap;

import lombok.Builder;
import lombok.Data;
import zerr.configuration.model.HardwareConfModel;

@Data
@Builder
public final class Hardware {
	private HashMap<Integer, Module> hashModule;
	private HashMap<Integer, Controller> hashController;

	public static Hardware create(HardwareConfModel hardware) {
		HashMap<Integer, Controller> hashController = new HashMap<>();
		
		HashMap<Integer, Module> hashModule = new HashMap<>();
		for (int i = 0; i < hardware.getModule().size(); i++) {
			Controller controller = Controller.create(hardware.getController());
			hashController.put(i, controller);
			hashModule.put(i, Module.create(hardware.getModule().get(i),controller));
		}
		
		return Hardware.builder()
				.hashModule(hashModule)
				.hashController(hashController)
				.build();
	}
	
}