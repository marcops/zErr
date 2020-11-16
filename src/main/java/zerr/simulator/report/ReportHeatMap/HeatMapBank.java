package zerr.simulator.report.ReportHeatMap;

import zerr.simulator.hardware.memcontroller.PhysicalAddress;

public class HeatMapBank extends HeatMap<HeatMapCell> {

	@Override
	public HeatMapCell getNew() {
		return new HeatMapCell();
	}

	@Override
	public long getPosition(PhysicalAddress phy) {
		return phy.getCellPosition();
	}

	@Override
	public boolean useMap() {
		return true;
	}
}
