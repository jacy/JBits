package com.jc.bits;

import static com.jc.bits.Bits.*;
import static com.jc.bits.TexasResult.RANK_FLUSH;
import static com.jc.bits.TexasResult.RANK_FOUR_OF_KIND;
import static com.jc.bits.TexasResult.RANK_FULL_HOUSE;
import static com.jc.bits.TexasResult.RANK_HIGHT_CARD;
import static com.jc.bits.TexasResult.RANK_ONE_PAIR;
import static com.jc.bits.TexasResult.RANK_STRAIGHT;
import static com.jc.bits.TexasResult.RANK_STRAIGHT_FLUSH;
import static com.jc.bits.TexasResult.RANK_THREE_OF_KIND;
import static com.jc.bits.TexasResult.RANK_TWO_PAIR;

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

	public static Optional<TexasResult> isFlush(int[] cards) {
		return IntStream.range(0, cards.length).boxed().filter(i -> countBit1(cards[i]) >= 5).findFirst().map(i -> new TexasResult(RANK_FLUSH, i + 1, keepNumbersOfBit1(cards[i], 5), cards[i]));
	}

	public static Optional<TexasResult> isFourKind(int[] cards) {
		int suits = combineSuits(cards, (x, y) -> x & y);
		if (suits > 0) {
			int score = score(cards, suits, 1);
			return Optional.of(new TexasResult(RANK_FOUR_OF_KIND, suits).score(score));
		}
		return Optional.empty();
	}

	public static Optional<TexasResult> isThreeKind(int[] cards) {
		int c = cards[0];
		int d = cards[1];
		int h = cards[2];
		int s = cards[3];
		Optional<Integer> first = IntStream.of(c & d & h, c & d & s, c & h & s, d & h & s).boxed().sorted(Comparator.reverseOrder()).filter(i -> i > 0).findFirst();
		return first.map(i -> new TexasResult(RANK_THREE_OF_KIND, keepNumbersOfBit1(i, 1)).score(score(cards, i, 2)));
	}

	public static Optional<TexasResult> isTwoPair(int[] suitResult) {
		return isPair(suitResult).flatMap(p1 -> {
			int[] suitAfterClear = clearHightBit(suitResult, p1.hight1);
			return isPair(suitAfterClear).map(p2 -> new TexasResult(RANK_TWO_PAIR, p1.hight1).score(score(suitResult, p1.hight1 | p2.hight1, 1)).hight2(p2.hight1));
		});
	}

	public static Optional<TexasResult> isFullHouse(int[] suitResult) {
		return isThreeKind(suitResult).flatMap(p1 -> {
			int[] suitAfterClear = clearHightBit(suitResult, p1.hight1);
			return isPair(suitAfterClear).map(p2 -> new TexasResult(RANK_FULL_HOUSE, p1.hight1).hight2(p2.hight1));
		});
	}

	public static Optional<TexasResult> isStraightFlush(int[] suitResult) {
		return isFlush(suitResult).flatMap(p1 -> {
			return isStraight(p1.hight2).map(p2 -> p2.suit(p1.suit).rank(RANK_STRAIGHT_FLUSH));
		});
	}

	public static Optional<TexasResult> isPair(int[] suitResult) {
		int c = suitResult[0];
		int d = suitResult[1];
		int h = suitResult[2];
		int s = suitResult[3];
		Optional<Integer> first = IntStream.of(c & d, c & h, c & s, d & h, d & s, h & s).boxed().sorted(Comparator.reverseOrder()).filter(i -> i > 0).findFirst();
		return first.map(i -> new TexasResult(RANK_ONE_PAIR, keepNumbersOfBit1(i, 1)).score(score(suitResult, i, 3)));
	}

	public static Optional<TexasResult> isStraight(int[] suitResult) {
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
			return Optional.of(new TexasResult(RANK_STRAIGHT, mask));
		}
		return isStraight(cards, mask >> 1);
	}

	public static Optional<TexasResult> isHightCard(int[] suitResult) {
		int suits = combineSuits(suitResult, (x, y) -> x | y);
		int hight = keepNumbersOfBit1(suits, 5);
		return Optional.of(new TexasResult(RANK_HIGHT_CARD, hight));
	}

	public static int score(int[] suitResult, int hight, int bit1Wanted) {
		int suits = combineSuits(suitResult, (x, y) -> x | y);
		int Mask1 = clearHightBit(suits, hight);
		return keepNumbersOfBit1(Mask1, bit1Wanted);
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
			return highBitPosition(current);
		}
		return faceFromMask(num, masks, index + 1);
	}

	public static TexasResult rankDescription(TexasResult result) {
		TexasResult description = new TexasResult(result.rank, faceFromMask(result.hight1));
		if (result.rank == RANK_FULL_HOUSE || result.rank == RANK_TWO_PAIR) {
			description.hight2(faceFromMask(result.hight2));
		} else if (result.rank == RANK_FLUSH || result.rank == RANK_STRAIGHT_FLUSH) {
			description.suit(result.suit);
		}
		return description;
	}
}