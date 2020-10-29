package zerr.simulator;

import java.util.HashMap;

import lombok.Builder;
import lombok.Data;
import zerr.configuration.model.MatrixConfModel;

@Data
@Builder
public class Matrix {
	private HashMap<Integer,Cell> hashCell;
	private Integer amount;

	public static Matrix create(MatrixConfModel matrix) {
		HashMap<Integer, Cell> hash = new HashMap<>();
		for (int i = 0; i < matrix.getAmount(); i++)
			hash.put(i, Cell.create(matrix.getCell()));
		
		return Matrix.builder()
				.hashCell(hash)
				.amount(matrix.getAmount())
				.build();
	}

}
