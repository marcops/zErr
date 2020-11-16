package zerr.simulator;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicLong;

import lombok.Getter;
import zerr.simulator.hardware.memcontroller.PhysicalAddress;
import zerr.simulator.report.ReportHeatMap.HeatMapGlobal;
import zerr.simulator.report.memory.MemoryMap;

@Getter
public final class Report {
	private static final Report INSTANCE = new Report();
	private HashMap<Long, ReportError> failedPosition = new HashMap<>();
//	private ConcurrentHashMap<Long, ErrorTypeInformation> errorInformation = new ConcurrentHashMap<>();
	
	private AtomicLong totalReadInstruction = new AtomicLong();
	private AtomicLong totalWriteInstruction = new AtomicLong();
	private long initTime = System.currentTimeMillis();
	private MemoryMap memory = new MemoryMap();

//	private ReportGlobalHeatMap reportHeatMap = new ReportGlobalHeatMap();
	
	private ReportError getMemoryData(PhysicalAddress phy) {
		if (!failedPosition.containsKey(phy.getPAddress()))
			failedPosition.put(phy.getPAddress(), new ReportError(phy));
		return failedPosition.get(phy.getPAddress());
	}

	public synchronized void hardError(PhysicalAddress phy) {
		getMemoryData(phy).addHardError();
	}
	
	public synchronized void softError(PhysicalAddress phy) {
		getMemoryData(phy).addSoftError();
	}
	
	private Report() {
	}

	public static Report getInstance() {
		return INSTANCE;
	}

	public void addReadInstruction() {
		totalReadInstruction.incrementAndGet();
	}
	
	public void addWriteInstruction() {
		totalWriteInstruction.incrementAndGet();
	}
	
//	public synchronized void addError(long pAddress, boolean hard) {
//		if(!errorInformation.contains(pAddress))
//			errorInformation.put(pAddress, new ErrorTypeInformation());	
//		
//		if(hard) errorInformation.get(pAddress).getHardError().incrementAndGet();
//		else errorInformation.get(pAddress).getSoftError().incrementAndGet();
//	}
	
	public void getReport() {
		String ret = "\r\n---- SIMULATION FINISHED --- \r\n";
		
//		ret += errorInformation.entrySet().stream()
//		  .map(entry -> "\tpAddress[" + entry.getKey() + "] " + entry.getValue())
//          .collect(Collectors.joining("\r\n"));
//		ret += memory.getPrintFault();
		ret += "\r\n\r\nTotal Read Instruction: " + totalReadInstruction;
		ret += "\r\nTotal Write Instruction: " + totalWriteInstruction;
		
		ret += "\r\nExecution Time: " + (System.currentTimeMillis()-initTime + "ms");
		
//		ret += "\r\n---- HeatMap --- \r\n";
//		reportHeatMap.print();
//		ret += "\r\n---- HeatMap --- \r\n";
		System.out.println(ret);
		
	}

	public void calculateByGlobal() {
		HeatMapGlobal glb = new HeatMapGlobal();
		failedPosition.forEach((x,y)->{
			glb.add(y.getAddress(), y.getSoftError(),y.getHardError());
		});
		try {
			glb.print();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
