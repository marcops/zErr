package zerr.configuration.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import zerr.simulator.faultinjection.EccMode;
import zerr.simulator.hardware.memcontroller.EccType;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
public class EccConfModel {
	private List<EccType> type;
	private Integer after;
	private EccMode mode;
}
