package zerr.simulator.report.ReportHeatMap;

import zerr.simulator.hardware.memcontroller.PhysicalAddress;

interface IHeatMap {
//	void addReadAccess(PhysicalAddress phy, long qtd);
//
//	void addWriteAccess(PhysicalAddress phy, long qtd);

	void addSoftError(PhysicalAddress phy, long qtd);

	void addHardError(PhysicalAddress phy, long qtd);
}
