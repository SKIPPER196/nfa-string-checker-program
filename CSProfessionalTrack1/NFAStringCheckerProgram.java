package CSProfessionalTrack1;
import java.util.*;

public class NFAStringCheckerProgram {
	public enum State {
		q0,
		q1,
		q2
	}
	
	public static class NFA {
		private Set<State> currentStates;
		private final Map<State, Map<Character, Set<State>>> transitionTable;
		
		public NFA() {
			this.currentStates = new HashSet<>();
			this.currentStates.add(State.q0);
			this.transitionTable = new HashMap<>();

			// Create NFA transition table for all states
			for(State state : State.values()) {
				transitionTable.put(state, new HashMap<>());
			}

			// Define state transitons
			transitionTable.get(State.q0).put('a', Set.of(State.q0, State.q1));
			transitionTable.get(State.q0).put('b', Set.of(State.q0));
			transitionTable.get(State.q1).put('a', Set.of(State.q1));
			transitionTable.get(State.q1).put('b', Set.of(State.q2));
			transitionTable.get(State.q2).put('a', Set.of(State.q2));
			transitionTable.get(State.q2).put('b', Set.of(State.q2));
		}
		
		public boolean process(String input) {
			// Queue-based simulation
			Queue<State> queue = new LinkedList<>();
			queue.add(State.q0); // Begin with starting state
			
			for(char symbol : input.toCharArray()) {
				Queue<State> nextQueue = new LinkedList<>();
				Set<State> visited = new HashSet<>(); // Avoids processing duplicates

				// Process all states at current level
				while(!queue.isEmpty()) {
					State currentState = queue.poll();
					
					// Get transitions for current state and symbol
					Map<Character, Set<State>> stateTransitions = transitionTable.get(currentState);

					if(stateTransitions.containsKey(symbol)) {
						Set<State> nextStates = stateTransitions.get(symbol);

						// Add all next states to the next queue if not visited
						for(State nextState : nextStates) {
							if(!visited.contains(nextState)) {
								visited.add(nextState);
								nextQueue.add(nextState);
							}
						}
					}
				}

				// Move to next level
				queue = nextQueue;

				// If no states remain, NFA has died
				if(queue.isEmpty()) {
					return false;
				}
			}

			// Checks if end on accepting state/s
			while(!queue.isEmpty()) {
				if(queue.poll() == State.q2) {
					return true;
				}
			}

			return false;
		}
	}

	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		NFA nfa = new NFA();
		
		System.out.println("---[NFA String Checker Program(Queue-based)]---");
		
		while(true) {
			System.out.print("\nInput string (a, b). Input 'x' to exit: ");
			String input = scan.nextLine().toLowerCase();
			
			if(!input.equals("x")) {
				if(input.matches("[ab]+")) {
					if(nfa.process(input)) {
						System.out.println("The string is accepted.");
					} else {
						System.out.println("The string is rejected.");
					}
				} else {
					System.out.println("Invalid input. Please input valid string.");
				}
			} else {
				System.out.print("Program exited.");
				break;
			}
		}
		
		scan.close();
	}
}