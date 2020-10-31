package zerr.simulator.hardware;

import lombok.Builder;
import zerr.configuration.model.CellConfModel;
import zerr.util.Bits;

@Builder
public final class Cell {

	private Bits cell;
	private Bits senseAmplifier;
	private Integer columnsLenght;
	
	private Integer currentRow;
	private Integer currentColumn;

	private void loadRow(Integer row) {
		currentRow = row * columnsLenght;
		senseAmplifier = cell.split(currentRow, columnsLenght);
	}
	
	public boolean exec(ControlSignal sinal, Bits address, boolean bit) {
		if(sinal.isLoadRow()) loadRow(address.toInt());
		if(sinal.isLoadColumn()) return loadColumn(address.toInt());
		if(sinal.isSetSenseAmpColumn()) return setSenseAmp(address.toInt() , bit);
		if(sinal.isToWriteCell()) return writeCell();
		return false;
	}
	
	private boolean writeCell() {
		boolean bit = senseAmplifier.get(currentColumn);
		cell.set(currentRow + currentColumn, bit);
		return bit;
	}

	private boolean setSenseAmp(int address, boolean bit) {
		currentColumn = address;
		senseAmplifier.set(address, bit);
		return bit;
	}

	public static Cell create(CellConfModel cell) {
		return Cell.builder()
				.columnsLenght(cell.getColumns())
				.senseAmplifier(new Bits(cell.getColumns()))
				.cell(new Bits(cell.getRow() * cell.getColumns()))
				.build();
	}

	private boolean loadColumn(Integer address) {
		currentColumn = address;
		return senseAmplifier.get(address);
	}

	
}
