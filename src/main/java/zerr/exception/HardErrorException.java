package zerr.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import zerr.util.Bits;

@Data
@AllArgsConstructor
public class HardErrorException extends Exception {
	private final Bits input;
}
