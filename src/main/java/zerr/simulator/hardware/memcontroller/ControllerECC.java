package zerr.simulator.hardware.memcontroller;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import zerr.exception.HardErrorException;
import zerr.exception.SoftErrorException;
import zerr.simulator.Report;
import zerr.simulator.hardware.memcontroller.ecc.CRC8;
import zerr.simulator.hardware.memcontroller.ecc.Hamming;
import zerr.util.Bits;

@Builder
@Data
@Slf4j
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

	public Bits decode(Bits data) {
		try {
			return decodeDataThrow(data);
		} catch (HardErrorException e) {
			Report.getInstance().increaseHardError();
			log.debug("HardError", e);
			return new Bits();
		} catch (SoftErrorException e) {
			Report.getInstance().increaseSoftError();
			log.debug("SoftError", e);
			return e.getRecovered();
		}

	}

	private Bits decodeDataThrow(Bits data) throws HardErrorException, SoftErrorException {
		if (eccType == EccType.CRC8)
			return CRC8.decode(data);
		if (eccType == EccType.HAMMING_SECDEC)
			return Hamming.decode(data);
		return data;

	}

}