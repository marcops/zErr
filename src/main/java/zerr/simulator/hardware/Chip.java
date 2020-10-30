package zerr.simulator.hardware;

import java.util.HashMap;

import lombok.Builder;
import zerr.configuration.model.ChipConfModel;
import zerr.util.BitSetFunction;

//@Data
@Builder
public final class Chip {
	private HashMap<Integer, Bank> hashBank;
//	private Integer amount;
	
	public static Chip create(ChipConfModel chip) {
		HashMap<Integer, Bank> hash = new HashMap<>();
		for (int i = 0; i < chip.getBank().size(); i++)
			for (int j = 0; j < chip.getAmount(); j++)
				hash.put(j, Bank.create(chip.getBank().get(i)));
		
		return Chip.builder()
				.hashBank(hash)
//				.amount(chip.getAmount())
				.build();
	}

	public void exec(ChannelRequest request) {
		int bank = BitSetFunction.toInt(request.getBank());
		if(bank < 0 || bank >= hashBank.size()) {
			System.err.println("FATAL: Wrong bank");
			System.exit(-1);
		}
		
		hashBank.get(bank).exec(request);
		
	}
}
