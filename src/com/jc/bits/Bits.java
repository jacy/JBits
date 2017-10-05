package com.jc.bits;

import java.util.Arrays;
import java.util.stream.IntStream;

public class Bits {
	private static final int[] LOG2_MASK = new int[] { 16, 0xFFFF0000, 8, 0xFF00, 4, 0xF0, 2, 0xC, 1, 0x2 };

	public static int highBitPosition(int num) {
		if (num <= 0) {
			throw new RuntimeException("must be postive");
		}
		return log2(num, 0, LOG2_MASK);
	}

	private static int log2(int num, int count, int[] mask) {
		if (mask.length == 0) {
			return count;
		}
		int S = mask[0];
		int B = mask[1];
		int[] magic = Arrays.copyOfRange(mask, 2, mask.length);
		if ((num & B) == 0) {
			return log2(num, count, magic);
		} else {
			return log2((num >> S), (count | S), magic);
		}
	}

	public static int clearHightBit(int num, int hight) {
		return num & (~hight);
	}

	public static int[] clearHightBit(int[] cards, int hight) {
		return IntStream.of(cards).map(card -> clearHightBit(card, hight)).toArray();
	}

	public static int countBit1(int n) {
		if (n < 0) {
			throw new RuntimeException("must be postive");
		}
		return countBit1(n, 0);
	}

	private static int countBit1(int num, int count) {
		if (num == 0) {
			return count;
		}
		return countBit1((num & (num - 1)), count + 1);
	}

	public static int keepNumbersOfBit1(int num, int bit1Wanted) {
		int coutOfBit1 = countBit1(num);
		if (bit1Wanted >= coutOfBit1) {
			return num;
		}
		return keepNumbersOfBit1(num, bit1Wanted, coutOfBit1);
	}

	private static int keepNumbersOfBit1(int num, int bit1Wanted, int coutOfBit1) {
		if (num == 0 || coutOfBit1 == 0 || bit1Wanted == coutOfBit1) {
			return num;
		}
		return keepNumbersOfBit1(num & (num - 1), bit1Wanted, coutOfBit1 - 1);
	}

}