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

	public synchronized void addError(long vAddress, boolean hard) {
		if(!errorInformation.contains(vAddress))
			errorInformation.put(vAddress, new ErrorInformation());	
		
		if(hard) errorInformation.get(vAddress).getHardError().incrementAndGet();
		else errorInformation.get(vAddress).getSoftError().incrementAndGet();
	}
	
	public String getReport() {
		String ret = "\r\n---- SIMULATION FINISHED --- \r\n";
		ret += errorInformation.entrySet().stream()
		  .map(entry -> "\tvAddress[" + entry.getKey() + "] " + entry.getValue())
          .collect(Collectors.joining("\r\n"));
		return ret;
	}
}
