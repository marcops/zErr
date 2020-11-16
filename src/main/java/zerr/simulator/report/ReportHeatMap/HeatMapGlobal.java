package zerr.simulator.report.ReportHeatMap;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Getter;
import zerr.simulator.hardware.memcontroller.PhysicalAddress;

@Getter
public final class HeatMapGlobal extends HeatMap<HeatMapModule> {
	
//	public void add(MemoryData memoryData) {
//		addReadAccess(memoryData.getAddress() , memoryData.getReadAccess());
//		addHardError(memoryData.getAddress() , memoryData.getHardError());
//		addWriteAccess(memoryData.getAddress() , memoryData.getWriteAccess());
//		addSoftError(memoryData.getAddress() , memoryData.getSoftError());
//	}
//	
	public void add(PhysicalAddress address, long soft, long hard) {
		addHardError(address, hard);
		addSoftError(address ,soft);
	}
	
	@Override
	public HeatMapModule getNew() {
		return new HeatMapModule();
	}

	@Override
	public long getPosition(PhysicalAddress phy) {
		return phy.getModule();
	}

	@Override
	public boolean useMap() {
		return true;
	}
	
	public void print() throws JsonGenerationException, JsonMappingException, IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.writeValue(new File("errors.json"), this);
	}

}
