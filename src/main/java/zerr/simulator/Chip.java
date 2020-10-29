package zerr.simulator;

import java.util.HashMap;

import lombok.Builder;
import lombok.Data;
import zerr.configuration.model.ChipConfModel;

@Data
@Builder
public final class Chip {
	private HashMap<Integer, Bank> hashBank;
	private Integer amount;

	public static Chip create(ChipConfModel chip) {
		HashMap<Integer, Bank> hash = new HashMap<>();
		for (int i = 0; i < chip.getBank().size(); i++)
			for (int j = 0; j < chip.getAmount(); j++)
				hash.put(j, Bank.create(chip.getBank().get(i)));
		
		return Chip.builder()
				.hashBank(hash)
				.amount(chip.getAmount())
				.build();
	}
}
