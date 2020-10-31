package zerr.simulator.hardware;

import java.util.HashMap;

import lombok.Builder;
import lombok.Data;
import zerr.configuration.model.ControllerConfModel;

@Data
@Builder
public final class Controller {
	private HashMap<Integer, Module> hashModule;

	public static Controller create(ControllerConfModel controller) {
		HashMap<Integer, Module> hashModule = new HashMap<>();
		for (int i = 0; i < controller.getModule().size(); i++) {
			hashModule.put(i, Module.create(controller.getModule().get(i)));
		}
		
		return Controller.builder()
				.hashModule(hashModule)
				.build();
	}
}
