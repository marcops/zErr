package zerr.simulator.hardware;

import lombok.Builder;
import lombok.Data;
import zerr.configuration.model.ControllerConfModel;

@Data
@Builder
public final class Controller {
	private Module module;

	public static Controller create(ControllerConfModel controller) {
		return Controller.builder()
				.module(Module.create(controller.getModule()))
				.build();
	}
}
