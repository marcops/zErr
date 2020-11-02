package zerr.simulator.hardware;

import java.util.HashMap;

import lombok.Builder;
import zerr.configuration.model.BankGroupConfModel;
import zerr.configuration.model.ChipConfModel;
import zerr.util.Bits;

@Builder
public final class Chip {
	private HashMap<Integer, BankGroup> hashBankGroup;
	
	public static Chip create(ChipConfModel chip) {
		HashMap<Integer, BankGroup> hash = new HashMap<>();
		BankGroupConfModel bankGroup = chip.getBankGroup();
		for (int j = 0; j < bankGroup.getAmount(); j++) {
			hash.put(j, BankGroup.create(chip.getBankGroup()));
		}
		
		return Chip.builder()
				.hashBankGroup(hash)
				.build();
	}

	public Bits exec(ChannelEvent request) {
		int bankGroup = request.getBankGroup().toInt();
		if(bankGroup < 0 || bankGroup >= hashBankGroup.size()) {
			System.err.println("FATAL: Wrong bank");
			System.exit(-1);
		}
		return hashBankGroup.get(bankGroup).exec(request);
	}
}
