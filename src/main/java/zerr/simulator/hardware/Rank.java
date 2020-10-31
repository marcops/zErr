package zerr.simulator.hardware;

import java.util.HashMap;

import lombok.Builder;
import zerr.configuration.model.RankConfModel;

@Builder
public final class Rank {
	private HashMap<Integer, Chip> hashChip;

	public static Rank create(RankConfModel chip) {
		HashMap<Integer, Chip> hash = new HashMap<>();
		for (int i = 0; i < chip.getChip().size(); i++)
			for (int j = 0; j < chip.getAmount(); j++)
				hash.put(j, Chip.create(chip.getChip().get(i)));
		
		return Rank.builder()
				.hashChip(hash)
				.build();
	}

	public void exec(ChannelEvent request) {
		for (int i = 0; i < hashChip.size(); i++) {
			hashChip.get(i).exec(request);
		}
	}

}
