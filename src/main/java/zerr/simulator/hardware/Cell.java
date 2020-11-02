package zerr.simulator.hardware;

import lombok.Builder;
import lombok.Data;
import zerr.configuration.model.CellConfModel;
import zerr.util.Bits;

@Data
@Builder
public final class Cell {

	private Bits cell;
	private Bits senseAmplifier;
	
	private Integer columnsLength;
	private Integer rowLength;
	
	private Integer currentRow;
	private Integer currentColumn;
	

	private void loadRow(Integer row) {
		currentRow = row * columnsLength;
		senseAmplifier = cell.subbit(currentRow, columnsLength);
//		System.out.println("senserow["+currentRow+"]=" + senseAmplifier.toBitString(64));
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
		cell.set(currentRow + currentColumn, bit);
//		System.out.println("wcell["+currentRow+","+currentColumn+"]=" + cell.toBitString(64));
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
				.cell(new Bits(cell.getRow() * cell.getColumns()))
				.build();
	}

	private boolean loadColumn() {
//		System.out.println("rcell["+currentRow+","+currentColumn+"]=" + cell.toBitString(64));
		return senseAmplifier.get(currentColumn);
	}
	
	private boolean loadColumn(Integer address) {
		currentColumn = address;
		return senseAmplifier.get(address);
	}

	
}
