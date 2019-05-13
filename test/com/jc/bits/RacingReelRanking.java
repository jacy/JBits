package com.jc.bits;

import static java.util.Comparator.comparing;
import static java.util.Comparator.reverseOrder;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
		return machines.stream().collect(groupingBy(Machine::winlose)).entrySet().stream().sorted(Map.Entry.comparingByKey(reverseOrder())).collect(
				ArrayList::new, 
				(accumulated, element) -> {
					int rank = lastElement(accumulated).map(MachineRank::nextRank).orElse(1);
					accumulated.addAll(element.getValue().stream().sorted(comparing(Machine::getId)).map(m -> {
						return new MachineRank(rank, m, element.getValue().size());
					}).peek(System.out::println).collect(toList()));
				}, ArrayList::addAll);
	}

	public static Optional<MachineRank> lastElement(ArrayList<MachineRank> accumulated) {
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