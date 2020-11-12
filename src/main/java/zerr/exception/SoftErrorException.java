package zerr.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import zerr.util.Bits;

@Data
@AllArgsConstructor
public class SoftErrorException extends Exception {
	private final Bits input;
	private final Bits recovered;
}
