package zerr.simulator.hardware;

import java.util.BitSet;

import lombok.Builder;
import zerr.configuration.model.CellConfModel;

//@Data
@Builder
public final class Cell {

	private BitSet cell;
//	private Integer row;
//	private Integer columns;

	public boolean get(Integer row, Integer column) {
		return cell.get(getPosition(row, column));
	}

	public boolean set(Integer row, Integer column, boolean value) {
		cell.set(getPosition(row, column), value);
		return value;
	}

	private static int getPosition(Integer row, Integer column) {
		return (row * column) + column;
	}

	public static Cell create(CellConfModel cell) {
		return Cell.builder()
//				.row(cell.getRow())
//				.columns(cell.getColumns())
				.cell(new BitSet(cell.getRow() * cell.getColumns()))
				.build();
	}
}
