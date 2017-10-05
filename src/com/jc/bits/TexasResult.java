package com.jc.bits;


public class TexasResult implements Comparable<TexasResult> {
	public int rank;
	public int suit;
	public int hight1;
	public int hight2;
	public int score;

	public static final int RANK_STRAIGHT_FLUSH = 8;
	public static final int RANK_FOUR_OF_KIND = 7;
	public static final int RANK_FULL_HOUSE = 6;
	public static final int RANK_FLUSH = 5;
	public static final int RANK_STRAIGHT = 4;
	public static final int RANK_THREE_OF_KIND = 3;
	public static final int RANK_TWO_PAIR = 2;
	public static final int RANK_ONE_PAIR = 1;
	public static final int RANK_HIGHT_CARD = 0;

	public TexasResult(int rank, int suit, int hight1, int hight2) {
		this(rank, suit, hight1);
		this.hight2 = hight2;
	}

	public TexasResult(int rank, int suit, int hight1) {
		this(rank, hight1);
		this.suit = suit;
	}

	public TexasResult(int rank, int hight1) {
		this.rank = rank;
		this.hight1 = hight1;
	}

	public TexasResult(int rank) {
		this.rank = rank;
	}

	public TexasResult rank(int rank) {
		this.rank = rank;
		return this;
	}

	public TexasResult suit(int suit) {
		this.suit = suit;
		return this;
	}

	public TexasResult score(int score) {
		this.score = score;
		return this;
	}

	public TexasResult hight2(int hight2) {
		this.hight2 = hight2;
		return this;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + hight1;
		result = prime * result + hight2;
		result = prime * result + rank;
		result = prime * result + score;
		result = prime * result + suit;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TexasResult other = (TexasResult) obj;
		if (hight1 != other.hight1)
			return false;
		if (hight2 != other.hight2)
			return false;
		if (rank != other.rank)
			return false;
		if (score != other.score)
			return false;
		if (suit != other.suit)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "TexasResult [rank=" + rank + ", suit=" + suit + ", hight1=" + hight1 + ", hight2=" + hight2 + ", score=" + score + "]";
	}

	@Override
	public int compareTo(TexasResult that) {
		int compareRank = this.rank - that.rank;
		if (compareRank != 0) {
			return compareRank;
		}
		int compareHight1 = this.hight1 - that.hight1;
		if (compareHight1 != 0) {
			return compareHight1;
		}
		int compareHight2 = this.hight2 - that.hight2;
		if (compareHight2 != 0) {
			return compareHight2;
		}
		return this.score - that.score;
	}

}