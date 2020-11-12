package zerr.simulator.report;

import java.util.concurrent.atomic.AtomicLong;

import lombok.Data;

@Data
public class ErrorInformation {
	private AtomicLong hardError = new AtomicLong();
	private AtomicLong softError = new AtomicLong();
	@Override
	public String toString() {
		String ret = "";
		if (hardError.get() > 0) ret += "hard=" + hardError;
		if (softError.get() > 0) ret += "soft=" + softError;
		return ret;
	}
	
}
