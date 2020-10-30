package zerr.simulator.hardware;

import java.util.BitSet;
import java.util.HashMap;

import lombok.Builder;
import zerr.configuration.model.MatrixConfModel;
import zerr.util.BitSetFunction;

//@Data
@Builder
public class Matrix {
	private HashMap<Integer,Cell> hashCell;
	private Integer amount;

	private int currentRow;
	private int currentColumn;
	
	public static Matrix create(MatrixConfModel matrix) {
		HashMap<Integer, Cell> hash = new HashMap<>();
		for (int i = 0; i < matrix.getAmount(); i++)
			hash.put(i, Cell.create(matrix.getCell()));
		
		return Matrix.builder()
				.hashCell(hash)
				.amount(matrix.getAmount())
				.build();
	}
	
	public void exec(ChannelRequest request, BitSet data) {
		if(isSetRowWrite(request)) currentRow = BitSetFunction.toInt(request.getAddress());
		if(isSetColumnWrite(request)) currentColumn = BitSetFunction.toInt(request.getAddress());
		if(isSetData(request)) setDataOnMatrix(data);
	}

	private void setDataOnMatrix(BitSet data) {
		for (int i = 0; i < amount; i++) {
			hashCell.get(i).set(currentRow, currentColumn, data.get(i));
		}
	}

	private static boolean isSetData(ChannelRequest request) {
		return request.isRas() && !request.isCas() && request.isWe();
	}
	
	private static boolean isSetColumnWrite(ChannelRequest request) {
		return !request.isRas() && !request.isCas() && request.isWe();
	}

	private static boolean isSetRowWrite(ChannelRequest request) {
		return !request.isRas() && request.isCas() && request.isWe();
	}

}
