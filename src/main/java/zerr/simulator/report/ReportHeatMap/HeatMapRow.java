package zerr.simulator.report.ReportHeatMap;

import zerr.simulator.hardware.memcontroller.PhysicalAddress;

public class HeatMapRow extends HeatMap<HeatMapColumn> {

	@Override
	public HeatMapColumn getNew() {
		return new HeatMapColumn();
	}

	@Override
	public long getPosition(PhysicalAddress phy) {
		return phy.getColumn();
	}

	@Override
	public boolean useMap() {
		return true;
	}
}
