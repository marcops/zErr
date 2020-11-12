package zerr.simulator;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import lombok.Getter;
import zerr.simulator.report.ErrorInformation;

@Getter
public final class Report {
	private static final Report INSTANCE = new Report();
	private ConcurrentHashMap<Long, ErrorInformation> errorInformation = new ConcurrentHashMap<>();
	private AtomicLong totalReadInstruction = new AtomicLong();
	private AtomicLong totalWriteInstruction = new AtomicLong();
	private long initTime = System.currentTimeMillis();
	
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
	
	public synchronized void addError(long pAddress, boolean hard) {
		if(!errorInformation.contains(pAddress))
			errorInformation.put(pAddress, new ErrorInformation());	
		
		if(hard) errorInformation.get(pAddress).getHardError().incrementAndGet();
		else errorInformation.get(pAddress).getSoftError().incrementAndGet();
	}
	
	public String getReport() {
		String ret = "\r\n---- SIMULATION FINISHED --- \r\n";
		
		ret += errorInformation.entrySet().stream()
		  .map(entry -> "\tpAddress[" + entry.getKey() + "] " + entry.getValue())
          .collect(Collectors.joining("\r\n"));
		
		ret += "\r\n\r\nTotal Read Instruction: " + totalReadInstruction;
		ret += "\r\nTotal Write Instruction: " + totalWriteInstruction;
		
		ret += "\r\nExecution Time: " + (System.currentTimeMillis()-initTime + "ms");
		return ret;
	}
}
