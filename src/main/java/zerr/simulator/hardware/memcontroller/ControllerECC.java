package zerr.simulator.hardware.memcontroller;

import java.util.List;

import lombok.Builder;
import lombok.Data;
import zerr.configuration.model.EccConfModel;
import zerr.exception.HardErrorException;
import zerr.exception.SoftErrorException;
import zerr.simulator.Report;
import zerr.simulator.faultinjection.EccMode;
import zerr.simulator.hardware.memcontroller.ecc.CRC8;
import zerr.simulator.hardware.memcontroller.ecc.Hamming;
import zerr.util.Bits;

@Builder
@Data
public final class ControllerECC {
	private EccConfModel eccConfModel;
	private EccType currentEccType;
	private int position;

	public static ControllerECC create(EccConfModel model) {
		return ControllerECC.builder().eccConfModel(model).currentEccType(getEccType(model.getType(), 0)).position(0)
				.build();
	}

	public Bits encode(Bits data) {
		if (currentEccType == EccType.CRC8)
			return CRC8.encode(data);
		if (currentEccType == EccType.HAMMING_SECDEC)
			return Hamming.encode(data);
		return data;
	}

	public Bits decode(Bits data) throws HardErrorException, SoftErrorException {
		try {
			if (currentEccType == EccType.CRC8)
				return CRC8.decode(data);
			if (currentEccType == EccType.HAMMING_SECDEC)
				return Hamming.decode(data);
			return data;
		} catch (HardErrorException e) {
			checkNext();
			throw e;
		} catch (SoftErrorException e) {
			checkNext();
			throw e;
		}

	}

	private void checkNext() {
		if (eccConfModel.getMode() != null && eccConfModel.getMode() == EccMode.NUMBER_OF_FAILS) {
			if (Report.getInstance().getTotalReadInstruction().get()
					+ Report.getInstance().getTotalWriteInstruction().get() > eccConfModel.getAfter()) {
				System.out.println("position ecc " +position);
				if (position < eccConfModel.getType().size() - 1)
					position++;
				if (position != eccConfModel.getType().size() - 1) {
					currentEccType = getEccType(eccConfModel.getType(), position);
					System.out.println("moved " + currentEccType.name());
				}
			}
		}

	}

	private static EccType getEccType(List<EccType> ecc, int pos) {
		return ecc.get(pos);
	}

}
