package zerr.simulator.hardware;

import java.util.HashMap;

import lombok.Builder;
import zerr.configuration.model.BankConfModel;
import zerr.configuration.model.ChipConfModel;
import zerr.util.Bits;

@Builder
public final class Chip {
	private HashMap<Integer, Bank> hashBank;
	
	public static Chip create(ChipConfModel chip) {
		HashMap<Integer, Bank> hash = new HashMap<>();
		for (int i = 0; i < chip.getBank().size(); i++) {
			BankConfModel bank = chip.getBank().get(i);
			for (int j = 0; j < bank.getAmount(); j++) {
				hash.put((i * j) + j, Bank.create(bank));
			}
		}
		
		return Chip.builder()
				.hashBank(hash)
				.build();
	}

	public Bits exec(ChannelEvent request) {
		int bank = request.getBank().toInt();
		if(bank < 0 || bank >= hashBank.size()) {
			System.err.println("FATAL: Wrong bank");
			System.exit(-1);
		}
		
		return hashBank.get(bank).exec(request.getControlSignal(),request.getAddress(),request.getData());
		
	}
}
