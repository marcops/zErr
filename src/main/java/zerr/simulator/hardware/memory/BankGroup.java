package zerr.simulator.hardware.memory;

import java.util.HashMap;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import zerr.configuration.model.BankConfModel;
import zerr.configuration.model.BankGroupConfModel;
import zerr.util.Bits;

@Builder
@Data
@Slf4j
public final class BankGroup {
	private HashMap<Integer, Bank> hashBank;
	
	public static BankGroup create(BankGroupConfModel bankGroup) {
		HashMap<Integer, Bank> hash = new HashMap<>();
		BankConfModel bank = bankGroup.getBank();
		for (int j = 0; j < bank.getAmount(); j++) {
			hash.put(j, Bank.create(bank));
		}
		
		return BankGroup.builder()
				.hashBank(hash)
				.build();
	}

	public Bits exec(ChannelEvent request) {
		int bank = request.getBank().toInt();
		if(bank < 0 || bank >= hashBank.size()) {
			log.error("FATAL: Wrong bank");
			return null;
		}
		return hashBank.get(bank).exec(request.getControlSignal(),request.getAddress(),request.getData());
	}
}
