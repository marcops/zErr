package zerr.simulator;

import lombok.Getter;

@Getter
public final class Report {
	private static final Report INSTANCE = new Report();

	private Report() {
	}

	public static Report getInstance() {
		return INSTANCE;
	}

	private Integer hardError = 0;
	private Integer softError = 0;

	public void increaseHardError() {
		hardError++;
	}

	public void increaseSoftError() {
		softError++;
	}
	
	public String getReport() {
		return " ---- SIMULATION FINISHED --- \r\n"
				+"hardError: " + hardError + "\r\n"
				+ "softError: " + softError + "\r\n";
		
	}
}
