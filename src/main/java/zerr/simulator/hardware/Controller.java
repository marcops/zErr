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
		HashMap<Integer, Module> hash = new HashMap<>();
		for (int i = 0; i < controller.getModule().getAmount(); i++)
			hash.put(i, Module.create(controller.getModule()));
		
		return Controller.builder()
				.hashModule(hash)
				.build();
	}
}
