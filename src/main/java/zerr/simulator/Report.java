package zerr.simulator;

import java.util.concurrent.atomic.AtomicLong;

import lombok.Getter;

@Getter
public final class Report {
	private static final Report INSTANCE = new Report();

	private Report() {
	}

	public static Report getInstance() {
		return INSTANCE;
	}

	private AtomicLong hardError  = new AtomicLong();
	private AtomicLong softError = new AtomicLong();

	public void increaseHardError() {
		System.out.println("caa");
		hardError.incrementAndGet();
	}

	public void increaseSoftError() {
		softError.incrementAndGet();
	}
	
	public String getReport() {
		return " ---- SIMULATION FINISHED --- \r\n"
				+"hardError: " + hardError + "\r\n"
				+ "softError: " + softError + "\r\n";
		
	}
}
