package zerr.simulator.hardware;

import java.util.HashMap;

import lombok.Builder;
import zerr.configuration.model.BankConfModel;
import zerr.util.BitSetFunction;

//@Data
@Builder
public final class Bank {
	private HashMap<Integer, Matrix> hashMatrix;
//	private Integer amount;

	public static Bank create(BankConfModel bank) {
		HashMap<Integer, Matrix> hash = new HashMap<>();
		for (int i = 0; i < bank.getAmount(); i++)
			hash.put(i, Matrix.create(bank.getMatrix()));
		
		return Bank.builder()
//				.amount(bank.getAmount())
				.hashMatrix(hash)
				.build();
	}

	public void exec(ChannelRequest request) {
		final int BYTE_SIZE = 8;
		for (int i = 0; i < hashMatrix.size(); i++) {
			hashMatrix.get(i).exec(request, BitSetFunction.split(request.getData(), i , BYTE_SIZE));
		}
	}

}
