package csen1002.main.task4;
import java.lang.reflect.Array;
import java.util.*;

/**
 * Write your info here
 * 
 * @name Ahmed Mohamed Ahmed Azooz
 * @id 49-18699
 * @labNumber 16
 */

public class CfgEpsUnitElim {

	/**
	 * Constructs a Context Free Grammar
	 * 
	 * @param cfg A formatted string representation of the CFG. The string
	 *             representation follows the one in the task description
	 */
	LinkedList<String> V = new LinkedList<String>();
	LinkedList<String> T = new LinkedList<String>();
	LinkedList<Rule> R = new LinkedList<Rule>();

	static class Rule{
		String left;
		LinkedList<String> right;
		public Rule(String left, LinkedList<String> right) {
			this.left = left;
			this.right = right;
		}

		public boolean equals(Object o){
			if(o instanceof Rule){
				Rule r = (Rule) o;
				return r.left.equals(this.left) && r.right.equals(this.right);
			}
			return false;
		}
	}
	
	public CfgEpsUnitElim(String cfg) {
		// TODO Auto-generated constructor stub
		String V = cfg.split("#")[0];
		String T = cfg.split("#")[1];
		String R = cfg.split("#")[2];
		for(String s : V.split(";"))
			this.V.add(s);
		for(String s : T.split(";"))
			this.T.add(s);
		for(String s : R.split(";")){
			String left = s.split("/")[0];
			LinkedList<String> right = new LinkedList<String>();
			for(String r : s.split("/")[1].split(","))
				right.add(r);
			this.R.add(new Rule(left, right));
		}
			
		

	}

	/**
	 * @return Returns a formatted string representation of the CFG. The string
	 *         representation follows the one in the task description
	 */
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		String res = "";
		for(String s : V)
			res += s + ";";
		res = res.substring(0, res.length()-1);
		res+= "#";
		for(String s : T)
			res += s + ";";
		res = res.substring(0, res.length()-1);
		res+= "#";
		Collections.sort(R, new Comparator<Rule>() {
			@Override
			public int compare(Rule r1, Rule r2) {
				if(V.indexOf(r1.left) < V.indexOf(r2.left))
					return -1;
				else
					return 1;
			}
		});
		for(Rule r: R){
			res += r.left+ "/";
			Collections.sort(r.right);
			for(String s: r.right)
				res += s + ",";
			res = res.substring(0, res.length()-1);
			res += ";";

		}
		res = res.substring(0, res.length()-1);
		return res;


	}

	/**
	 * Eliminates Epsilon Rules from the grammar
	 */
	public void eliminateEpsilonRules() {
		// TODO Auto-generated method stub
		ArrayList<Rule> hasEpsilon = new ArrayList<Rule>();
		int i = 0;
		while(i < R.size()){
			Rule r = R.get(i);
			if(r.right.contains("e") && r.left != "S"){
				hasEpsilon.add(r);
				r.right.remove("e");
				i = 0;
				for(int j = 0; j < R.size(); j++){
					Rule r2 = R.get(j);
					if(r2.right.contains(r.left) && !hasEpsilon.contains(r2)){
						r2.right.add("e"); 
					}
					int size = r2.right.size();
					int k = 0;
					LinkedList<String> temp = new LinkedList<String>();
					while(k < size){
						String s = r2.right.get(k);
						if(s.contains(r.left) && s.length() > 1){
							temp.addAll(StringCombinations(s, r.left.charAt(0)));
							
							temp.remove("");
							removeDuplicates(temp);
							
						}
						k++;


					}

					r2.right.addAll(temp);
					removeDuplicates(r2.right);
				}
			}
			i++;
		}
	}

	/**
	 * Eliminates Unit Rules from the grammar
	 */
	public void eliminateUnitRules() {
		// TODO Auto-generated method stub

		for(int i = 0; i < R.size(); i++){
			Rule r = R.get(i);
			if(r.right.contains(r.left)){
				r.right.remove(r.left);
			}
		}
		boolean changed = false;
		do{
			changed = false;
			for(int i = 0; i < R.size(); i++){
				Rule r = R.get(i);
				LinkedList<String> temp = new LinkedList<String>();
				for(int j = 0; j < r.right.size(); j++){
					String s = r.right.get(j);
					if(s.length() == 1 && V.contains(s)){
						changed = true;
						r.right.remove(s);
						for(int k = 0; k < R.size(); k++){
							Rule r2 = R.get(k);
							if(r2.left.equals(s)){
								temp.addAll(r2.right);
							}
						}
					}
				}
				r.right.addAll(temp);
				removeDuplicates(r.right);
			}
		}while(changed);
		
			








	}

	public void removeDuplicates(LinkedList<String> list){
		HashSet<String> set = new HashSet<String>(list);
		list.clear();
		list.addAll(set);
	}
	
	public LinkedList<String> StringCombinations(String s, char c){

		HashSet<String> result = new HashSet<String>();
		for(int i = 0; i < s.length(); i++){
			if(s.charAt(i) == c){
				String temp = s.substring(0, i) + s.substring(i+1);
				result.add(temp);
				result.addAll(StringCombinations(temp, c));
			}
		}


		return new LinkedList<>(result);





	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		CfgEpsUnitElim cfg = new CfgEpsUnitElim("S;A;B;C#a;b;c;d;x#S/aAb,xB;A/Bc,C,c,d;B/CACA,e;C/A,b,e");
		cfg.eliminateEpsilonRules();
		cfg.eliminateUnitRules();
		System.out.println(cfg);
	}
}
