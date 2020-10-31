package zerr.simulator.hardware;

import java.util.HashMap;

import lombok.Builder;
import zerr.configuration.model.ChipConfModel;

@Builder
public final class Chip {
	private HashMap<Integer, Bank> hashBank;
	
	public static Chip create(ChipConfModel chip) {
		HashMap<Integer, Bank> hash = new HashMap<>();
		for (int i = 0; i < chip.getBank().size(); i++)
			for (int j = 0; j < chip.getAmount(); j++)
				hash.put(j, Bank.create(chip.getBank().get(i)));
		
		return Chip.builder()
				.hashBank(hash)
				.build();
	}

	public void exec(ChannelEvent request) {
		int bank = request.getBank().toInt();
		if(bank < 0 || bank >= hashBank.size()) {
			System.err.println("FATAL: Wrong bank");
			System.exit(-1);
		}
		
		hashBank.get(bank).exec(request);
		
	}
}
