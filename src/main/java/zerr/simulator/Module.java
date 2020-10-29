package zerr.simulator;

import java.util.HashMap;

import lombok.Builder;
import lombok.Data;
import zerr.configuration.model.ModuleConfModel;

//TODO JEDEC DDR4 SPD
@Data
@Builder
public final class Module {
	private HashMap<Integer, Rank> hashRank;
	private Channel channel;
	private Integer amount;

	public static Module create(ModuleConfModel module) {
		HashMap<Integer, Rank> hash = new HashMap<>();
		for (int i = 0; i < module.getRank().size(); i++)
			for (int j = 0; j < module.getAmount(); j++)
				hash.put(j, Rank.create(module.getRank().get(i)));
		
		return Module.builder()
				.hashRank(hash)
				.amount(module.getAmount())
				.channel(Channel.create(module.getChannel()))
				.build();
	}

}
