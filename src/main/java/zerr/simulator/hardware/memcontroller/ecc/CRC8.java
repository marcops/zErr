package zerr.simulator.hardware.memcontroller.ecc;

public class CRC8 {
	private static final int POLY = 0x0D5;

	public static byte encode(final byte[] input, final int offset, final int len) {
		byte crc = 0;
		for (int i = 0; i < len; i++) {
			crc = encode(input[offset + i], crc);
		}
		return crc;
	}

	public static byte encode(final byte[] input) {
		return encode(input, 0, input.length);
	}

	private static final byte encode(final byte b, int bcrc) {
		int crc = bcrc;
		crc ^= b;
		for (int j = 0; j < 8; j++) {
			if ((crc & 0x80) != 0)
				crc = ((crc << 1) ^ POLY);
			else
				crc <<= 1;

		}
		return (byte) (crc &= 0xFF);
	}

	public static byte encode(final int b) {
		return encode((byte) b, 0);
	}

}