package com.jc.bits;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;

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
		ranking(machines);
	}

	public static List<MachineRank> ranking(List<Machine> machines) {
		return machines.stream().sorted(Machine.WINLOSE_DESC_ID_ASC).collect(groupingBy(Machine::winlose, LinkedHashMap::new, toList())).entrySet().stream().collect(
			ArrayList::new, 
			(accumulated, element) -> {
				int rank = getLast(accumulated).map(MachineRank::nextRank).orElse(1);
				accumulated.addAll(element.getValue().stream().map(m -> {
					return new MachineRank(rank, m, element.getValue().size());
				}).peek(System.out::println).collect(toList()));
			}, 
			ArrayList::addAll
		);
	}

	public static Optional<MachineRank> getLast(ArrayList<MachineRank> accumulated) {
		return accumulated.stream().reduce((first, second) -> second);
	}

	static class MachineRank {
		private int rank;
		private Machine machine;
		private int numberOfSameRank;

		public MachineRank(int rank, Machine machine, int numberOfSameRank) {
			this.rank = rank;
			this.machine = machine;
			this.numberOfSameRank = numberOfSameRank;
		}

		public int nextRank() {
			return rank + numberOfSameRank;
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