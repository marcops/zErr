package zerr.simulator.hardware.memory;

import java.util.HashMap;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import zerr.configuration.model.BankGroupConfModel;
import zerr.configuration.model.ChipConfModel;
import zerr.util.Bits;

@Builder
@Data
@Slf4j
public final class Chip {
	private HashMap<Integer, BankGroup> hashBankGroup;
	
	public static Chip create(ChipConfModel chip) {
		HashMap<Integer, BankGroup> hash = new HashMap<>();
		BankGroupConfModel bankGroup = chip.getBankGroup();
		for (int i = 0; i < bankGroup.getAmount(); i++)
			hash.put(i, BankGroup.create(chip.getBankGroup()));
		
		return Chip.builder()
				.hashBankGroup(hash)
				.build();
	}

	public Bits exec(ChannelEvent request) {
		int bankGroup = request.getBankGroup().toInt();
		if(bankGroup < 0 || bankGroup >= hashBankGroup.size()) {
			log.error("FATAL: Wrong bank");
			return null;
		}
		return hashBankGroup.get(bankGroup).exec(request);
	}
}
