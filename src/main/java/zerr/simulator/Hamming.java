package zerr.simulator;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class Hamming {
	private static final int VAR_2 = 2;
	private static final int VAR_10 = 10;

	private Hamming() {
	}
//
//	public static void debug(int[] a) {
//		for (int i = 0; i < a.length; i++) {
//			System.out.print(a[a.length - i - 1]);
//		}
//		System.out.println();
//	}

	public static int[] encode(int[] a) {
		// We will return the array 'b'.
		int[] b;
		int i = 0;
		int j = 0;
		int k = 0;
		int parityCount = countParitySize(a);

		b = new int[a.length + parityCount];

		// Initialize this array with '2' to indicate an 'unset' value in parity bit
		// locations:

		for (i = 1; i <= b.length; i++) {
			if ((int)Math.pow(VAR_2, j) == i) {
				// Found a parity bit location.
				// Adjusting with (-1) to account for array indices starting from 0 instead of
				// 1.

				b[i - 1] = VAR_2;
				j++;
			} else {
				b[k + j] = a[k++];
			}
		}
		for (i = 0; i < parityCount; i++) {
			// Setting even parity bits at parity bit locations:

			b[((int) Math.pow(VAR_2, i)) - 1] = getParity(b, i);
		}
		return b;
	}

	private static int countParitySize(int[] a) {
		int i = 0;
		int parityCount = 0;
		while (i < a.length) {
			// 2^(parity bits) must equal the current position
			// Current position is (number of bits traversed + number of parity bits + 1).
			// +1 is needed since array indices start from 0 whereas we need to start from
			// 1.

			if ((int)Math.pow(VAR_2, parityCount) == i + parityCount + 1) {
				parityCount++;
			} else {
				i++;
			}
		}
		return parityCount;
	}

	static int getParity(int[] b, int power) {
		int parity = 0;
		for (int i = 0; i < b.length; i++) {
			if (b[i] != VAR_2) {
				// If 'i' doesn't contain an unset value,
				// We will save that index value in k, increase it by 1,
				// Then we convert it into binary:

				int k = i + 1;
				String s = Integer.toBinaryString(k);

				// Nw if the bit at the 2^(power) location of the binary value of index is 1
				// Then we need to check the value stored at that location.
				// Checking if that value is 1 or 0, we will calculate the parity value.

				int x = ((Integer.parseInt(s)) / ((int) Math.pow(VAR_10, power))) % VAR_10;
				if (x == 1 && b[i] == 1)
					parity = (parity + 1) % VAR_2;
			}
		}
		return parity;

	}

	public static int[] receive(int[] a, int parityCount) {
		int errorLocation = getErrorPosition(a, parityCount);
		if (errorLocation > a.length) {
			log.info("MULTIPLES ERRORS" + errorLocation);
			errorLocation = 0;
		}
		int[] ham = a.clone();
		if (errorLocation != 0)
			ham = fixError(a, errorLocation);
		// else

		int power = parityCount - 1;
		for (int i = ham.length; i > 0; i--) {
			if ((int)Math.pow(VAR_2, power) != i) {
			} else {
				power--;
			}
		}
		return ham;
	}

	private static int[] fixError(int[] a, int errorLocation) {
		log.info("Error is at location " + errorLocation + ".");
		a[errorLocation - 1] = (a[errorLocation - 1] + 1) % VAR_2;
		return a;
	}

	public static int getErrorPosition(int[] a, int parityCount) {
		int[] parity = new int[parityCount];
		// 'parity' array will store the values of the parity checks.
		String syndrome = "";
		// 'syndrome' string will be used to store the integer value of error location.

		for (int power = 0; power < parityCount; power++) {
			// We need to check the parities, the same no of times as the no of parity bits
			// added.

			for (int i = 0; i < a.length; i++) {
				// Extracting the bit from 2^(power):

				int k = i + 1;
				String s = Integer.toBinaryString(k);
				int bit = ((Integer.parseInt(s)) / ((int) Math.pow(VAR_10, power))) % VAR_10;
				if (bit == 1 && a[i] == 1)
					parity[power] = (parity[power] + 1) % VAR_2;
			}
			syndrome = parity[power] + syndrome;
		}
		// This gives us the parity check equation values.
		// Using these values, we will now check if there is a single bit error and then
		// correct it.

		return Integer.parseInt(syndrome, VAR_2);

	}

	public static boolean hasError(int[] block) {
		// TODO FIX PARITY
		return getErrorPosition(block, 6) != 0;
	}
}