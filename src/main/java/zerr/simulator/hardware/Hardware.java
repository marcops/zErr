package zerr.simulator.hardware;

import lombok.Builder;
import lombok.Data;
import zerr.configuration.model.HardwareConfModel;
import zerr.simulator.hardware.memcontroller.Controller;

@Data
@Builder
public final class Hardware {
	private Controller controller;

	public static Hardware create(HardwareConfModel hardware) {
		return Hardware.builder()
				.controller(Controller.create(hardware.getController()))
				.build();
	}

}