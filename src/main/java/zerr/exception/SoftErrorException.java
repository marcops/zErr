package zerr.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import zerr.util.Bits;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SoftErrorException extends Exception {
	private Bits input;
	private Bits recovered;
}
