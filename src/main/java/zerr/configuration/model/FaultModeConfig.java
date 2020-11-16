package zerr.configuration.model;

import lombok.Data;
import zerr.simulator.faultinjection.FaultMode;

@Data
public class FaultModeConfig {
	private FaultMode type;
	private Integer distance;
}
