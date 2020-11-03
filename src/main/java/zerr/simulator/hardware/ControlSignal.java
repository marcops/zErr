package zerr.simulator.hardware;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ControlSignal {
	private boolean ras;
	private boolean cas;
	private boolean we;

	public static ControlSignal noOperation() {
		return ControlSignal.builder().ras(true).cas(true).we(true).build();
	}

	public static ControlSignal loadRow() {
		return ControlSignal.builder().ras(false).cas(true).we(true).build();
	}

	public static ControlSignal loadColumn() {
		return ControlSignal.builder().ras(false).cas(false).we(true).build();
	}

	public static ControlSignal setSenseAmpColumn() {
		return ControlSignal.builder().ras(false).cas(true).we(false).build();
	}

	public static ControlSignal writeCell() {
		return ControlSignal.builder().ras(false).cas(false).we(false).build();
	}

	public static ControlSignal dataOkToRead() {
		return ControlSignal.builder().ras(true).cas(false).we(true).build();
	}
	
	public boolean isDataOkToRead() {
		return ras && !cas && we;
	}

	public boolean isNoOperation() {
		return ras && cas && we;
	}

	public boolean isLoadRow() {
		return !ras && cas && we;
	}

	public boolean isLoadColumn() {
		return !ras && !cas && we;
	}

	public boolean isSetSenseAmpColumn() {
		return !ras && cas && !we;
	}

	public boolean isToWriteCell() {
		return !ras && !cas && !we;
	}
}
