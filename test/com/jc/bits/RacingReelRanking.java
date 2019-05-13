package com.jc.bits;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;

public class RacingReelRanking {

	public static void main(String[] args) {
		List<Machine> machines = new ArrayList<Machine>();
		machines.add(new Machine(1, 1, 30));
		machines.add(new Machine(2, 1, 30));
		machines.add(new Machine(3, 1, 30));
		machines.add(new Machine(4, 1, 13));
		machines.add(new Machine(5, 1, 3));
		machines.add(new Machine(6, 1, 13));
		machines.add(new Machine(7, 1, 13));
		machines.add(new Machine(8, 1, 13));
		machines.add(new Machine(9, 1, 13));
		machines.add(new Machine(10, 1, 3));

		new Ranker().ranking(machines).forEach(System.out::println);
	}

	static class Ranker {
		private int nextRank = 1;
		private int numberOfSameRank = 0;

		public List<MachineRank> ranking(List<Machine> machines) {
			return machines.stream().sorted(Machine.WINLOSE_DESC_ID_ASC).collect(groupingBy(Machine::winlose, LinkedHashMap::new, toList())).entrySet().stream().map(keyVaule -> {
				nextRank += numberOfSameRank;
				numberOfSameRank = keyVaule.getValue().size();
				return keyVaule.getValue().stream().map(m -> {
					return new MachineRank(nextRank, m);
				}).collect(toList());
			}).flatMap(List::stream).collect(toList());
		}
	}

	static class MachineRank {
		private int rank = 0;
		private Machine machine;

		public MachineRank(int rank, Machine machine) {
			this.rank = rank;
			this.machine = machine;
		}

		@Override
		public String toString() {
			return machine + " ranked " + this.rank;
		}

	}

	static class Machine {
		private Integer id = 0;
		private Integer startBalance;
		private Integer endBalance;
		public static final Comparator<Machine> WINLOSE_DESC_ID_ASC = comparing(Machine::winlose).reversed().thenComparing(Machine::getId);

		public Machine(Integer id, Integer startBalance, Integer endBalance) {
			this.id = id;
			this.startBalance = startBalance;
			this.endBalance = endBalance;
		}

		public Integer winlose() {
			return endBalance - startBalance;
		}

		public Integer getId() {
			return id;
		}

		@Override
		public String toString() {
			return "Machine(" + id + "," + startBalance + "," + endBalance + ")";
		}
	}
}
