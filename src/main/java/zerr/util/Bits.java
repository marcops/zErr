package zerr.util;

import java.util.BitSet;

public class Bits extends BitSet {

	public Bits() { }

	public Bits(int size) {
		super(size);
	}

	public static Bits from(final byte msg) {
		return copyBitSet(BitSet.valueOf(new byte[] { msg }));
	}
	
	public static Bits from(final String s) {
		return copyBitSet(BitSet.valueOf(new long[] { Long.parseLong(s, 2) }));
	}

	private static Bits copyBitSet(BitSet bs) {
		Bits b = new Bits(bs.length());
		for(int i=0;i<bs.length();i++) {
			b.set(i, bs.get(i));
		}
		return b;
	}

	public static Bits from(int num) {
		char[] bits = Integer.toBinaryString(num).toCharArray();
		Bits bitSet = new Bits(bits.length);
		for (int i = 0; i < bits.length; i++)
			bitSet.set(i, bits[i] == '1');
		return bitSet;
	}

	public int toInt() {
		int bitInteger = 0;
		final int INT_SIZE = 32;
		for (int i = 0; i < INT_SIZE; i++)
			if (this.get(i)) bitInteger |= (1 << i);
		return bitInteger;
	}

	public Bits split(int position, int size) {
		Bits bit = new Bits(size);
		for (int i = 0; i < size; i++) bit.set(i, this.get(i+position));
		return bit;
	}
}
