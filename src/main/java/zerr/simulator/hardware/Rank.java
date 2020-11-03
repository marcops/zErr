package zerr.simulator.hardware;

import java.util.HashMap;

import lombok.Builder;
import lombok.Data;
import zerr.configuration.model.ChipConfModel;
import zerr.configuration.model.RankConfModel;
import zerr.util.Bits;

@Builder
@Data
public final class Rank {
	private HashMap<Integer, Chip> hashChip;
	private Integer chipDataSize;
	
	public static Rank create(RankConfModel rank, int chipDataSize) {
		HashMap<Integer, Chip> hash = new HashMap<>();
		ChipConfModel chip = rank.getChip();
		for (int j = 0; j < chip.getAmount(); j++)
			hash.put(j, Chip.create(chip));
		
		return Rank.builder()
				.hashChip(hash)
				.chipDataSize(chipDataSize)
				.build();
	}

	public Bits exec(ChannelEvent request) {
		Bits bReceived = new Bits();
		for (int i = 0; i < hashChip.size(); i++) {
			ChannelEvent chipRequest = request.toBuilder()
					.data(request.getData().subbit(i*chipDataSize, chipDataSize)).build();
			Bits r = hashChip.get(i).exec(chipRequest);
			bReceived.append(r);
		}
		return bReceived;
	}

}
