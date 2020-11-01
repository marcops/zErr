//package zerr.simulator.hardware;
//
//import java.util.HashMap;
//
//import lombok.Builder;
//import lombok.Data;
//import zerr.configuration.model.MatrixConfModel;
//import zerr.util.Bits;
//
//@Data
//@Builder
//public class Matrix {
//	private HashMap<Integer,Cell> hashCell;
//	private Integer amount;
//	
//	public static Matrix create(MatrixConfModel matrix) {
//		HashMap<Integer, Cell> hash = new HashMap<>();
//		for (int i = 0; i < matrix.getAmount(); i++)
//			hash.put(i, Cell.create(matrix.getCell()));
//		
//		return Matrix.builder()
//				.hashCell(hash)
//				.amount(matrix.getAmount())
//				.build();
//	}
//	
//	public Bits exec(ControlSignal controlSignal, Bits address, Bits data) {
//		//Process 8 bit - 1 byte (normally)
//		for (int i = 0; i < amount; i++) 
//			data.set(i, hashCell.get(i).exec(controlSignal, address, data.get(i)));
//		return data;
//	}
//}
