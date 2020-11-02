package zerr.simulator.hardware;

import java.util.HashMap;

import lombok.Builder;
import lombok.Data;
import zerr.configuration.model.BankConfModel;
import zerr.configuration.model.BankGroupConfModel;
import zerr.util.Bits;

@Builder
@Data
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
			System.err.println("FATAL: Wrong bank");
			System.exit(-1);
		}
		return hashBank.get(bank).exec(request.getControlSignal(),request.getAddress(),request.getData());
	}
}
