package zerr.configuration.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
//@AllArgsConstructor
//@NoArgsConstructor
public class SimulatorConfModel {


}
