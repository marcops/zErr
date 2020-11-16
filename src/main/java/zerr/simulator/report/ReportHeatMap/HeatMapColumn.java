package zerr.simulator.report.ReportHeatMap;

import zerr.simulator.hardware.memcontroller.PhysicalAddress;

public class HeatMapColumn extends HeatMap<HeatMapColumn> {

	@Override
	public HeatMapColumn getNew() {
		return null;
	}

	@Override
	public long getPosition(PhysicalAddress phy) {
		return 0;
	}

	@Override
	public boolean useMap() {
		return false;
	}
}