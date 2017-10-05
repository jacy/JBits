package com.jc.bits;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.IntBinaryOperator;
import java.util.stream.IntStream;

public class Texas {
	private static final List<Function<int[], Optional<TexasResult>>> functions = new ArrayList<>();
	static {
		functions.add(Texas::isStraightFlush);
		functions.add(Texas::isFourKind);
		functions.add(Texas::isFullHouse);
		functions.add(Texas::isFlush);
		functions.add(Texas::isStraight);
		functions.add(Texas::isThreeKind);
		functions.add(Texas::isTwoPair);
		functions.add(Texas::isPair);
		functions.add(Texas::isHightCard);
	}

	public static TexasResult rankDescription(TexasResult result) {
		switch (result.rank) {
		case TexasResult.RANK_FULL_HOUSE:
		case TexasResult.RANK_TWO_PAIR:
			return new TexasResult(result.rank, faceFromMask(result.hight1)).hight2(faceFromMask(result.hight2));
		case TexasResult.RANK_FLUSH:
		case TexasResult.RANK_STRAIGHT_FLUSH:
			return new TexasResult(result.rank, faceFromMask(result.hight1)).suit(result.suit);
		default:
			return new TexasResult(result.rank, faceFromMask(result.hight1));
		}
	}

	public static TexasResult rank(int[] cards) {
		int[] combineResult = combineResult(cards);
		for (Function<int[], Optional<TexasResult>> fun : functions) {
			Optional<TexasResult> result = fun.apply(combineResult);
			if (result.isPresent()) {
				return result.get();
			}
		}
		return null;
	}

	public static Optional<TexasResult> isFlush(int[] suitResult) {
		int suits = suitResult.length;
		for (int i = 0; i < suits; i++) {
			int suitCard = suitResult[i];
			int countBit1 = Bits.countBit1(suitCard);
			if (countBit1 >= 5) {
				int high5Cards = Bits.keepNumbersOfBit1(suitCard, 5);
				return Optional.of(new TexasResult(TexasResult.RANK_FLUSH, i + 1, high5Cards, suitCard));
			}
		}
		return Optional.empty();
	}

	public static Optional<TexasResult> isFourKind(int[] suitResult) {
		int suits = combineSuits(suitResult, (x, y) -> x & y);
		if (suits > 0) {
			int score = score(suitResult, suits, 1);
			return Optional.of(new TexasResult(TexasResult.RANK_FOUR_OF_KIND, suits).score(score));
		}
		return Optional.empty();
	}

	public static Optional<TexasResult> isThreeKind(int[] suitResult) {
		int c = suitResult[0];
		int d = suitResult[1];
		int h = suitResult[2];
		int s = suitResult[3];
		Optional<Integer> first = IntStream.of(c & d & h, c & d & s, c & h & s, d & h & s).boxed().sorted(Comparator.reverseOrder()).filter(i -> i > 0).findFirst();
		return first.map(i -> new TexasResult(TexasResult.RANK_THREE_OF_KIND, Bits.keepNumbersOfBit1(i, 1)).score(score(suitResult, i, 2)));
	}

	public static Optional<TexasResult> isTwoPair(int[] suitResult) {
		return isPair(suitResult).flatMap(p1 -> {
			int[] suitAfterClear = Bits.clearHightBit(suitResult, p1.hight1);
			return isPair(suitAfterClear).map(p2 -> new TexasResult(TexasResult.RANK_TWO_PAIR, p1.hight1).score(score(suitResult, p1.hight1 | p2.hight1, 1)).hight2(p2.hight1));
		});
	}

	public static Optional<TexasResult> isFullHouse(int[] suitResult) {
		return isThreeKind(suitResult).flatMap(p1 -> {
			int[] suitAfterClear = Bits.clearHightBit(suitResult, p1.hight1);
			return isPair(suitAfterClear).map(p2 -> new TexasResult(TexasResult.RANK_FULL_HOUSE, p1.hight1).hight2(p2.hight1));
		});
	}

	public static Optional<TexasResult> isStraightFlush(int[] suitResult) {
		System.out.println("isStraightFlush");
		return isFlush(suitResult).flatMap(p1 -> {
			return isStraight(p1.hight2).map(p2 -> p2.suit(p1.suit).rank(TexasResult.RANK_STRAIGHT_FLUSH));
		});
	}

	public static Optional<TexasResult> isPair(int[] suitResult) {
		int c = suitResult[0];
		int d = suitResult[1];
		int h = suitResult[2];
		int s = suitResult[3];
		Optional<Integer> first = IntStream.of(c & d, c & h, c & s, d & h, d & s, h & s).boxed().sorted(Comparator.reverseOrder()).filter(i -> i > 0).findFirst();
		return first.map(i -> new TexasResult(TexasResult.RANK_ONE_PAIR, Bits.keepNumbersOfBit1(i, 1)).score(score(suitResult, i, 3)));
	}

	public static Optional<TexasResult> isStraight(int[] suitResult) {
		System.out.println("isStraight");
		int suits = combineSuits(suitResult, (x, y) -> x | y);
		return isStraight(suits);
	}

	public static Optional<TexasResult> isStraight(int cards) {
		if ((Cards.CARD_ACE & cards) > 0) {
			cards |= 1; // both A,2,3,4,5 and 10,J,Q,K,A are consider as straight
		}
		return isStraight(cards, Cards.CARD_AKQJT);
	}

	public static Optional<TexasResult> isStraight(int cards, int mask) {
		if (mask < Cards.CARD_5432A) {
			return Optional.empty();
		}
		if ((mask & cards) == mask) {
			return Optional.of(new TexasResult(TexasResult.RANK_STRAIGHT, mask));
		}
		return isStraight(cards, mask >> 1);
	}

	public static Optional<TexasResult> isHightCard(int[] suitResult) {
		System.out.println("isHightCard");
		int suits = combineSuits(suitResult, (x, y) -> x | y);
		int hight = Bits.keepNumbersOfBit1(suits, 5);
		return Optional.of(new TexasResult(TexasResult.RANK_HIGHT_CARD, hight));
	}

	public static int score(int[] suitResult, int hight, int bit1Wanted) {
		int suits = combineSuits(suitResult, (x, y) -> x | y);
		int Mask1 = Bits.clearHightBit(suits, hight);
		return Bits.keepNumbersOfBit1(Mask1, bit1Wanted);
	}

	public static int combineSuits(int[] cards, IntBinaryOperator op) {
		return IntStream.of(cards).reduce(op).getAsInt();
	}

	public static int[] combineResult(int[] cards) {
		int[] combineBySuitResult = new int[4];
		IntStream.of(cards).forEach(card -> {
			int face = card >> 4;
			int suit = card & 0b1111;
			combineBySuitResult[suit - 1] |= faceToMask(face);
		});
		return combineBySuitResult;
	}

	private static int faceToMask(int face) {
		return 1 << face; // 2 => 10; 3 => 100; count of the bit 0 is the face value
	}

	public static int faceFromMask(int num) {
		if (num == 0) {
			return 0;
		}
		return faceFromMask(num, IntStream.of(Cards.FACES).map(Texas::faceToMask).toArray(), 0);
	}

	private static int faceFromMask(int num, int[] masks, int index) {
		if (masks.length == index) {
			return 0;
		}

		int current = masks[index];
		if ((num & current) > 0) {
			return Bits.highBitPosition(current);
		}
		return faceFromMask(num, masks, index + 1);
	}
}