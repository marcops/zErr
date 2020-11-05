package zerr.simulator.hardware;

import java.util.HashMap;

import lombok.Builder;
import lombok.Data;
import zerr.configuration.model.BankConfModel;
import zerr.util.Bits;

@Data
@Builder
public final class Bank {
	private HashMap<Integer,Cell> hashCell;
	
	public static Bank create(BankConfModel bank) {
		HashMap<Integer, Cell> hash = new HashMap<>();
		for (int i = 0; i < Bits.ONE_BYTE; i++)
			hash.put(i, Cell.create(bank.getCell()));
		
		return Bank.builder()
				.hashCell(hash)
				.build();
	}
	
	public Bits exec(ControlSignal controlSignal, Bits address, Bits data) {
		//Process 8 bit - 1 byte (normally)
		for (int i = 0; i < hashCell.size(); i++) 
			data.set(i, hashCell.get(i).exec(controlSignal, address, data.get(i)));
		return data;
	}
}
