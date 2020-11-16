package zerr.simulator.hardware.memcontroller;

import lombok.Builder;
import lombok.Data;
import zerr.exception.HardErrorException;
import zerr.exception.SoftErrorException;
import zerr.simulator.hardware.memcontroller.ecc.CRC8;
import zerr.simulator.hardware.memcontroller.ecc.Hamming;
import zerr.util.Bits;

@Builder
@Data
//@Slf4j
public final class ControllerECC {
	private EccType eccType;

	public ControllerECC(EccType eccType) {
		this.eccType = eccType;
	}

	public Bits encode(Bits data) {
		if (eccType == EccType.CRC8)
			return CRC8.encode(data);
		if (eccType == EccType.HAMMING_SECDEC)
			return Hamming.encode(data);
		return data;
	}

//	public Bits decode(long pAddress, Bits data) {
//		try {
//			return decodeDataThrow(data);
//		} catch (HardErrorException e) {
//			Report.getInstance().addError(pAddress , true);
//			log.debug("HardError", e);
//			return new Bits();
//		} catch (SoftErrorException e) {
//			Report.getInstance().addError(pAddress , false);
//			log.debug("SoftError", e);
//			return e.getRecovered();
//		}
//
//	}

	public Bits decode(Bits data) throws HardErrorException, SoftErrorException {
		if (eccType == EccType.CRC8)
			return CRC8.decode(data);
		if (eccType == EccType.HAMMING_SECDEC)
			return Hamming.decode(data);
		return data;

	}

}
