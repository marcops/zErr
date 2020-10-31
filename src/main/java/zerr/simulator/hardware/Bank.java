package zerr.simulator.hardware;

import java.util.HashMap;

import lombok.Builder;
import zerr.configuration.model.BankConfModel;

//@Data
@Builder
public final class Bank {
	private HashMap<Integer, Matrix> hashMatrix;

	public static Bank create(BankConfModel bank) {
		HashMap<Integer, Matrix> hash = new HashMap<>();
		for (int i = 0; i < bank.getAmount(); i++)
			hash.put(i, Matrix.create(bank.getMatrix()));
		
		return Bank.builder()
				.hashMatrix(hash)
				.build();
	}

	public void exec(ChannelEvent request) {
		for (int i = 0; i < hashMatrix.size(); i++) {
			hashMatrix.get(i).exec(request.getControlSignal(), request.getAddress(),
					request.getData().split(i , hashMatrix.get(i).getAmount()));
		}
	}

}
