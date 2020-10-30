package zerr.util;

import java.util.BitSet;

public final class BitSetFunction {
	private BitSetFunction() {
	}

	public static BitSet from(final byte msg) {
		return BitSet.valueOf(new byte[] { msg });
	}
	
	public static BitSet from(final String s) {
		return BitSet.valueOf(new long[] { Long.parseLong(s, 2) });
	}

	public static BitSet from(int num) {
		char[] bits = Integer.toBinaryString(num).toCharArray();
		BitSet bitSet = new BitSet(bits.length);
		for (int i = 0; i < bits.length; i++)
			bitSet.set(i, bits[i] == '1');
		return bitSet;
	}

	public static int toInt(BitSet bitSet) {
		int bitInteger = 0;
		final int INT_SIZE = 32;
		for (int i = 0; i < INT_SIZE; i++)
			if (bitSet.get(i)) bitInteger |= (1 << i);
		return bitInteger;
	}

	public static BitSet split(BitSet data, int position, int size) {
		BitSet bit = new BitSet(size);
		for (int i = 0; i < size; i++) bit.set(i, data.get(i+position));
		return bit;
	}
}
