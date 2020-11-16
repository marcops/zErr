package zerr.simulator.report.ReportHeatMap;

import zerr.simulator.hardware.memcontroller.PhysicalAddress;

public class HeatMapRank extends HeatMap<HeatMapGroupBank> {
	@Override
	public HeatMapGroupBank getNew() {
		return new HeatMapGroupBank();
	}

	@Override
	public long getPosition(PhysicalAddress phy) {
		return phy.getBankGroup();
	}

	@Override
	public boolean useMap() {
		return true;
	}
}
