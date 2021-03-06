package zerr.configuration.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import zerr.simulator.faultinjection.FaultType;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
public class FaultInjectionConfModel {
	private FaultType type;
	//every milli
	private Long every;
	private FaultModeConfig mode;
}
