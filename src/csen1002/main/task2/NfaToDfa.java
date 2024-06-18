package csen1002.main.task2;

import java.util.*;

/**
 * Write your info here
 * 
 * @name Ahmed Mohamed Ahmed Azooz
 * @id 49-18699
 * @labNumber 16
 */

public class NfaToDfa {


	static class DFAstate implements Comparable<DFAstate>{
		TreeSet<Integer> states;
		public DFAstate(TreeSet<Integer> states){
			this.states = states;
		}

	public int compareTo(DFAstate state) {
			Iterator<Integer> iterator1 = this.states.iterator();
			Iterator<Integer> iterator2 = state.states.iterator();
			while(iterator1.hasNext() && iterator2.hasNext()){
				int next1 = iterator1.next();
				int next2 = iterator2.next();
				if(next1 != next2)
					return next1 - next2;
			}
			if(iterator1.hasNext())
				return 1;
			if(iterator2.hasNext())
				return -1;
			return 0;
		}
	}

  	static class DFA{
		ArrayList<DFAstate> states;
		ArrayList<DFAtransition> transitions;
		DFAstate start;
		ArrayList<DFAstate> acceptStates;
		public DFA(ArrayList<DFAstate> states, ArrayList<DFAtransition> transitions, DFAstate start, ArrayList<DFAstate> acceptStates) {
			this.states = states;
			this.transitions = transitions;
			this.start = start;
			this.acceptStates = acceptStates;
		}
	}

	static class DFAtransition{
		DFAstate start;
		String transition;
		DFAstate end;
		public DFAtransition(DFAstate start, String transition, DFAstate end) {
			this.start = start;
			this.transition = transition;
			this.end = end;
		}
	}

	DFA theDFA;
	String Alphabet = "";
	/**
	 * Constructs a DFA corresponding to an NFA
	 * 
	 * @param input A formatted string representation of the NFA for which an
	 *              equivalent DFA is to be constructed. The string representation
	 *              follows the one in the task description
	 */

	public NfaToDfa(String input) {
		// TODO Auto-generated constructor stub
		String[] parts = input.split("#");
		String[] Q = parts[0].split(";");
		String[] A = parts[1].split(";");
		String[] T = parts[2].split(";");
		int I = Integer.parseInt(parts[3]);
		String[] F = parts[4].split(";");
		HashSet<Integer> states = new HashSet<>();
        HashSet<String> alphabet = new HashSet<>(Arrays.asList(A));
        ArrayList<String> transitions = new ArrayList<>(Arrays.asList(T));
		HashSet<Integer> acceptStates = new HashSet<>();
		for (String state : Q) {
			states.add(Integer.parseInt(state));
		}
		for(String state: F)
			acceptStates.add(Integer.parseInt(state));
		HashMap<Integer, TreeSet<Integer>> epsilonClosures = new HashMap<>();
		for (int state : states) {
			epsilonClosures.put(state, epsilonClosure(transitions, state));
		}
		ArrayList<DFAstate> dfaStates = new ArrayList<>();
		ArrayList<DFAtransition> dfaTransitions = new ArrayList<>();
		getDFAstates(dfaStates, dfaTransitions, alphabet, transitions, epsilonClosures, new DFAstate(epsilonClosures.get(I)));


		ArrayList<DFAstate> acceptDFAStates = new ArrayList<>();
		for (DFAstate state : dfaStates) {
			for (int acceptState : acceptStates) {
				if (state.states.contains(acceptState)) {
					acceptDFAStates.add(state);
					break;
				}
			}
		}


		theDFA = new DFA(dfaStates, dfaTransitions, dfaStates.get(0), acceptDFAStates);
		Alphabet = parts[1];





		
		



	}

public static void getDFAstates(ArrayList<DFAstate> dfaStates, ArrayList<DFAtransition> dfaTransitions, HashSet<String> alphabet, ArrayList<String> transitions, HashMap<Integer, TreeSet<Integer>> epsilonClosures,
								DFAstate state){



		for(DFAstate state1: dfaStates){
			if(state1.compareTo(state) == 0)
				return;
		}
			dfaStates.add(state);
			for (String c : alphabet) {
				TreeSet<Integer> newState = new TreeSet<>();
				for (int state1 : state.states) {
					for (String transition : transitions) {
						String[] parts = transition.split(",");
						if (parts[0].equals(state1 + "") && parts[1].equals(c)) {
							newState.addAll(epsilonClosures.get(Integer.parseInt(parts[2])));
						}
					}
				}
				if(newState.isEmpty())
					newState.add(-1);
				DFAstate newState1 = new DFAstate(newState);
				dfaTransitions.add(new DFAtransition(state, c, newState1));
				getDFAstates(dfaStates, dfaTransitions, alphabet, transitions, epsilonClosures, newState1);
			}

	}


	/**
	 * @return Returns a formatted string representation of the DFA. The string
	 *         representation follows the one in the task description
	 */
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		Collections.sort(theDFA.states);
		theDFA.transitions.sort(new Comparator<DFAtransition>() {
			@Override
			public int compare(DFAtransition o1, DFAtransition o2) {
				if(o1.start.compareTo(o2.start) == 0){
					if(o1.transition.compareTo(o2.transition) == 0){
						return o1.end.compareTo(o2.end);
					}
					return o1.transition.compareTo(o2.transition);
				}
                return o1.start.compareTo(o2.start);
            }
		});

		String Q = "";
		for (DFAstate state : theDFA.states) {
			Q += toString(state) + ";";
		}
		Q = Q.substring(0, Q.length() - 1);
		String A = Alphabet;
		String T = "";
		for (DFAtransition transition : theDFA.transitions) {
			T += toString(transition.start) + "," + transition.transition + "," + toString(transition.end) + ";";
		}
		T = T.substring(0, T.length() - 1);
		String I = toString(theDFA.start);
		String F = "";
		Collections.sort(theDFA.acceptStates);
		for (DFAstate acceptState : theDFA.acceptStates) {
			F += toString(acceptState) + ";";
		}
		F = F.substring(0, F.length() - 1);
		return Q + "#" + A + "#" + T + "#" + I + "#" + F;




	}

	public static String toString(DFAstate state){
		String Q = "";
		for (int state1 : state.states) {
			Q += state1 + "/";
		}
		Q = Q.substring(0, Q.length() - 1);
		return Q;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new NfaToDfa("0;1;2;3;4;5;6;7;8;9;10#a;b#0,e,1;1,b,2;2,e,3;3,e,4;3,e,9;4,e,5;4,e,7;5,a,6;6,e,4;6,e,9;7,b,8;8,e,4;8,e,9;9,a,10#0#10");


	}



	public static TreeSet<Integer> epsilonClosure(ArrayList<String> transitions, int state){
		TreeSet<Integer> closure = new TreeSet<>();
		closure.add(state);
		boolean added = true;
		while (added) {
			added = false;
			for (String transition : transitions) {
				String[] parts = transition.split(",");
				if ((Integer.parseInt(parts[0]) == state && parts[1].equals("e")) || (closure.contains(Integer.parseInt(parts[0])) && parts[1].equals("e"))){
					if (!closure.contains(Integer.parseInt(parts[2]))) {
						closure.add(Integer.parseInt(parts[2]));
						added = true;
					}
				}
			}
		}
		return closure;
	}

}
