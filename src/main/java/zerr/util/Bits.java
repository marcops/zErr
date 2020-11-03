package zerr.util;

import java.util.BitSet;

public class Bits extends BitSet {
	public Bits() {
	}

	public Bits(int size) {
		super(size);
	}

	public static Bits from(final byte msg) {
		return bitSet2Bits(BitSet.valueOf(new byte[] { msg }));
	}
	
	public static Bits[] from(final byte[] msgs) {
		Bits[] b = new Bits[msgs.length];
		for(int i = 0; i< msgs.length;i++)
			b[i] = from(msgs[i]);
		return b;
	}
	
	public static Bits[] from(final String s) {
		return from(s.getBytes());
	}
	
//	public static Bits fromBitArray(final String s) {
//		return bitSet2Bits(BitSet.valueOf(new long[] { Long.parseLong(s, 2) }));
//	}

	private static Bits bitSet2Bits(BitSet bs) {
		Bits b = new Bits(bs.length());
		for (int i = 0; i < bs.length(); i++) b.set(i, bs.get(i));
		return b;
	}

//	public static Bits from(int num) {
//		String bits = Integer.toBinaryString(num);
//		Bits bitSet = new Bits(bits.length());
//		for (int i = 0; i < bits.length(); i++) bitSet.set(i, bits.charAt((bits.length()-1)-i) == '1');
//		return bitSet;
//	}

	public int toInt() {
		if (this.toLongArray().length == 0) return 0;
		return (int) this.toLongArray()[0];
	}
	
	public long toLong() {
		if (this.toLongArray().length == 0) return 0;
		return this.toLongArray()[0];
	}

	public Bits subbit(int position, int size) {
		Bits bit = new Bits(size);
		for (int i = 0; i < size; i++)
			bit.set(i, this.get(i + position));
		return bit;
	}

	public void append(Bits bits) {
		int baseLenght = this.length(); 
		for (int i = 0; i < bits.length(); i++) {
			this.set(baseLenght + i, bits.get(i));
		}
	}
	
	public String toBitString(int length) {
		String value = "";
		for (int i = 0; i < length; i++) 
			value += this.get(i) ? "1" : "0";
		return value;
	}

	public static Bits from(long r) {
		return bitSet2Bits(BitSet.valueOf(new long[] { r }));
	}
}
