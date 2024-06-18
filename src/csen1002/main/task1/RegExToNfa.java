package csen1002.main.task1;

import java.util.*;

public class RegExToNfa {


    @Override
    public String toString() {

        String Q = "";
        for (Integer state : theNFA.states) {
            Q += state + ";";
        }
        Q = Q.substring(0, Q.length() - 1);
        String A = Alphabet;
        String T = "";
        ArrayList<String> transitions = theNFA.transition;
        HashSet<String> set = new HashSet<>(transitions);
        transitions.clear();
        transitions.addAll(set);
        transitions.sort((s1, s2) -> {
            String[] s3 = s1.split(",");
            String[] s4 = s2.split(",");
            if (Integer.parseInt(s3[0]) != Integer.parseInt(s4[0])) {
                return Integer.parseInt(s3[0]) - Integer.parseInt(s4[0]);
            }
            if (s3[1].charAt(0) != s4[1].charAt(0)) {
                return s3[1].charAt(0) - s4[1].charAt(0);
            }
            return Integer.parseInt(s3[2].substring(0,s3[2].length() - 1)) - Integer.parseInt(s4[2].substring(0,s4[2].length() - 1));
        });
        for (String transition : transitions) {
            T += transition;
        }



        String I = theNFA.start+"";
        String F = "";
        for (Integer acceptState : theNFA.acceptStates) {
            F += acceptState+",";
        }
        F = F.substring(0, F.length()-1);
        return Q+"#"+A+"#"+T.substring(0,T.length() - 1)+"#"+I+"#"+F;


    }
    /**
     * Write your info here
     *
     * @name Ahmed Mohamed Ahmed Azooz
     * @id 49-18699
     * @labNumber 16
     */


    static class NFA{
        HashSet<Integer> states;
        int start;
        HashSet<Integer> acceptStates;

        ArrayList<String> transition;


        public NFA(int start, int end, String transition){
            this.start = start;
            states = new HashSet<>();
            states.add(start);
            states.add(end);
            this.acceptStates = new HashSet<Integer>();
            this.acceptStates.add(end);
            this.transition = new ArrayList<>();
            this.transition.add(transition);
        }
        public NFA(int start, int end, ArrayList<String> transition, HashSet<Integer> states, HashSet<Integer> acceptStates){
            this.start = start;
            this.states = states;
            this.acceptStates = acceptStates;
            this.transition = transition;
        }
    }
    NFA theNFA;
    String Alphabet = "";
    public RegExToNfa(String regEx) {
        String R = regEx.split("#")[1];
        String A = regEx.split("#")[0];
        Stack<NFA> stack = new Stack<>();
        int state = 0;
        for(char c: R.toCharArray()){
            if(A.contains(c+"") || c == 'e'){
                int state1 = state++;
                int state2 = state++;
                stack.push(new NFA(state1, state2, state1+","+c+","+state2+";"));
            }
            if(c == '|'){
                NFA nfa2 = stack.pop();
                NFA nfa1 = stack.pop();
                HashSet<Integer> states = new HashSet<>();
                states.addAll(nfa1.states);
                states.addAll(nfa2.states);
                int newStart = state++;
                int newEnd = state++;
                states.add(newStart);
                states.add(newEnd);
                ArrayList<String> transition = new ArrayList<>();
                transition.addAll(nfa1.transition);
                transition.addAll(nfa2.transition);
                transition.add(newStart+",e,"+nfa1.start+";");
                transition.add(newStart+",e,"+nfa2.start+";");
                for (Integer acceptState : nfa1.acceptStates) {
                    transition.add(acceptState + ",e," + newEnd + ";");
                }

                for (Integer acceptState : nfa2.acceptStates) {
                    transition.add(acceptState + ",e," + newEnd + ";");
                }
                HashSet<Integer> acceptStates = new HashSet<>();
                acceptStates.add(newEnd);

                NFA finalNFA = new NFA(newStart, newEnd, transition, states,acceptStates);
                stack.push(finalNFA);





            }
            if(c == '*'){
                NFA nfa = stack.pop();
                int newStart = state++;
                int newEnd = state++;
                HashSet<Integer> states = new HashSet<>(nfa.states);
                states.add(newStart);
                states.add(newEnd);
                ArrayList<String> transition = new ArrayList<>(nfa.transition);
                transition.add(newStart+",e,"+nfa.start+";");
                transition.add(newStart+",e,"+newEnd+";");
                for (Integer acceptState : nfa.acceptStates) {
                    transition.add(acceptState + ",e," + nfa.start + ";");
                }
                for (Integer acceptState : nfa.acceptStates) {
                    transition.add(acceptState + ",e," + newEnd + ";");
                }

                transition.add(newStart+",e,"+newEnd+";");
                HashSet<Integer> acceptStates = new HashSet<>();
                acceptStates.add(newEnd);
                NFA finalNFA = new NFA(newStart, newEnd, transition, states,acceptStates);
                stack.push(finalNFA);


            }
            if(c == '.'){
                NFA nfa2 = stack.pop();
                NFA nfa1 = stack.pop();
                HashSet<Integer> states = new HashSet<>();
                states.addAll(nfa1.states);
                states.addAll(nfa2.states);
                ArrayList<String> transition = new ArrayList<>();
                transition.addAll(nfa1.transition);
                transition.addAll(nfa2.transition);
                for (Integer acceptState : nfa1.acceptStates) {
                    for(String transition1: nfa2.transition){
                        int state1 = Integer.parseInt(transition1.split(",")[0]);
                        int state2 = Integer.parseInt(transition1.split(",")[2].substring(0,transition1.split(",")[2].length()-1));
                        char c1 = transition1.split(",")[1].charAt(0);
                        if(state1 == nfa2.start){
                            transition.add(acceptState+","+c1+","+state2+";");
                            transition.remove(transition1);
                            states.remove(nfa2.start);
                        }
                    }
                }
                HashSet<Integer> acceptStates = new HashSet<>(nfa2.acceptStates);
                NFA finalNFA = new NFA(nfa1.start, state, transition, states,acceptStates);
                stack.push(finalNFA);




            }

        }
        theNFA = stack.pop();
        Alphabet = A;






    }


    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String regEx = sc.nextLine();
        RegExToNfa regExToNfa = new RegExToNfa(regEx);
        System.out.println(regExToNfa);
    }
}
