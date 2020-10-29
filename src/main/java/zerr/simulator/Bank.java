package zerr.simulator;

import java.util.HashMap;

import lombok.Builder;
import lombok.Data;
import zerr.configuration.model.BankConfModel;

@Data
@Builder
public final class Bank {
	private HashMap<Integer, Matrix> hashMatrix;
	private Integer amount;

	public static Bank create(BankConfModel bank) {
		HashMap<Integer, Matrix> hash = new HashMap<>();
		for (int i = 0; i < bank.getAmount(); i++)
			hash.put(i, Matrix.create(bank.getMatrix()));
		
		return Bank.builder()
				.amount(bank.getAmount())
				.hashMatrix(hash)
				.build();
	}

}
