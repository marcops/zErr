package zerr.simulator.report.ReportHeatMap;

import zerr.simulator.hardware.memcontroller.PhysicalAddress;

public class HeatMapCell extends HeatMap<HeatMapRow> {

	@Override
	public HeatMapRow getNew() {
		return new HeatMapRow();
	}

	@Override
	public long getPosition(PhysicalAddress phy) {
		return phy.getRow();
	}

	@Override
	public boolean useMap() {
		return true;
	}
}
