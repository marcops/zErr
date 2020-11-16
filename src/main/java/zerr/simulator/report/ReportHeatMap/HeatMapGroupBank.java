package zerr.simulator.report.ReportHeatMap;

import zerr.simulator.hardware.memcontroller.PhysicalAddress;

public class HeatMapGroupBank extends HeatMap<HeatMapBank> {

	@Override
	public HeatMapBank getNew() {
		return new HeatMapBank();
	}

	@Override
	public long getPosition(PhysicalAddress phy) {
		return phy.getBank();
	}

	@Override
	public boolean useMap() {
		return true;
	}
}