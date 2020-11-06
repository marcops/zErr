package zerr.simulator.hardware.memory;

import lombok.Builder;
import lombok.Data;
import zerr.configuration.model.CellConfModel;
import zerr.util.Bits;

@Data
@Builder
public final class Cell {

	private Bits icell;
	private Bits senseAmplifier;
	
	private Integer columnsLength;
	private Integer rowLength;
	
	private Integer currentRow;
	private Integer currentColumn;
	

	private void loadRow(Integer row) {
		currentRow = row * columnsLength;
		senseAmplifier = icell.subbit(currentRow, columnsLength);
	}
	
	public boolean exec(ControlSignal sinal, Bits address, boolean bit) {
		if(sinal.isLoadRow()) loadRow(address.toInt());
		if(sinal.isLoadColumn()) return loadColumn(address.toInt());
		if(sinal.isSetSenseAmpColumn()) return setSenseAmp(address.toInt() , bit);
		if(sinal.isToWriteCell()) return writeCell();
		if(sinal.isDataOkToRead()) return loadColumn();
		return bit;
	}
	
	private boolean writeCell() {
		boolean bit = senseAmplifier.get(currentColumn);
		icell.set(currentRow + currentColumn, bit);
		return bit;
	}

	private boolean setSenseAmp(int address, boolean bit) {
		currentColumn = address;
		senseAmplifier.set(address, bit);
		return bit;
	}

	public static Cell create(CellConfModel cell) {
		return Cell.builder()
				.rowLength(cell.getRow())
				.columnsLength(cell.getColumns())
				.senseAmplifier(new Bits(cell.getColumns()))
				.icell(new Bits(cell.getRow() * cell.getColumns()))
				.build();
	}

	private boolean loadColumn() {
		return senseAmplifier.get(currentColumn);
	}
	
	private boolean loadColumn(Integer address) {
		currentColumn = address;
		return senseAmplifier.get(address);
	}

	
}
