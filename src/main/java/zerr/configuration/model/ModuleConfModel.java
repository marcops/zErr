package zerr.configuration.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
public class ModuleConfModel {
	private String name;
	private Integer amount;
	private List<RankConfModel> rank;
	private ChannelConfModel channel;
}
