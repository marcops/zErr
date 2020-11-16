package zerr.simulator.report.ReportHeatMap;

import zerr.simulator.hardware.memcontroller.PhysicalAddress;

public class HeatMapModule extends HeatMap<HeatMapRank> {
	@Override
	public HeatMapRank getNew() {
		return new HeatMapRank();
	}

	@Override
	public long getPosition(PhysicalAddress phy) {
		return phy.getRank();
	}

	@Override
	public boolean useMap() {
		return true;
	}

}
