package zerr.simulator;

import java.util.BitSet;

import lombok.Builder;
import lombok.Data;
import zerr.configuration.model.CellConfModel;

//TODO REMOVER DATA
@Data
@Builder
public final class Cell {

	private BitSet cell;
	private Integer row;
	private Integer columns;

	public boolean get(Integer row, Integer column) {
		return cell.get(getPosition(row, column));
	}

	public boolean set(Integer row, Integer column) {
		return cell.get(getPosition(row, column));
	}

	private static int getPosition(Integer row, Integer column) {
		return (row * column) + column;
	}

	public static Cell create(CellConfModel cell) {
		return Cell.builder()
				.row(cell.getRow())
				.columns(cell.getColumns())
				.cell(new BitSet(cell.getRow() * cell.getColumns()))
				.build();
	}
}
