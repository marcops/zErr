package zerr.simulator;

import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import lombok.Getter;
import zerr.simulator.report.ErrorInformation;

@Getter
public final class Report {
	private static final Report INSTANCE = new Report();
	private ConcurrentHashMap<Long, ErrorInformation> errorInformation = new ConcurrentHashMap<>();
	private Report() {
	}

	public static Report getInstance() {
		return INSTANCE;
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
		return ret;
	}
}
