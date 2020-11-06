package zerr.configuration.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import zerr.simulator.hardware.memcontroller.ChannelMode;
import zerr.simulator.hardware.memcontroller.EccType;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
public class ControllerConfModel {
	private ModuleConfModel module;
	private EccType eccType;
	private ChannelMode channelMode;
}
