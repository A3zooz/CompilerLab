package csen1002.main.task5;

import java.util.*;


/**
 * Write your info here
 * 
 * @name Ahmed Mohamed Ahmed Azooz
 * @id 49-18699	
 * @labNumber 16
 */

public class CfgLeftRecElim {

	/**
	 * Constructs a Context Free Grammar
	 * 
	 * @param cfg A formatted string representation of the CFG. The string
	 *            representation follows the one in the task description
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
	public CfgLeftRecElim(String cfg) {
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
		for(Rule r: R){
			res += r.left+ "/";
			for(String s: r.right)
				res += s + ",";
			res = res.substring(0, res.length()-1);
			res += ";";

		}
		res = res.substring(0, res.length()-1);
		return res;
	}


	/**
	 * Eliminates Left Recursion from the grammar
	 */
	public void eliminateLeftRecursion() {
		// TODO Auto-generated method stub
		LinkedList<Rule> temp3 = new LinkedList<Rule>();
		for(int i = 0; i < R.size(); i++){
			Rule r = R.get(i);
			for(int j = 0; j < i; j++){
				Rule r2 = R.get(j);
				for(String s: new LinkedList<>(r.right)){
					if(s.charAt(0) == r2.left.charAt(0)){
						int k = r.right.indexOf(s);
						if(k != -1){
						for(String s2: r2.right){
							r.right.add(k++, s2 + s.substring(1));
						}
						r.right.remove(s);
					}
					}
				}

			}
			LinkedList<String> alpha = new LinkedList<String>();
			LinkedList<String> beta = new LinkedList<String>();
			for(String s : r.right){
				if(s.startsWith(r.left)){
					alpha.add(s.substring(1));
				}else{
					beta.add(s);
				}
			}
			if(alpha.size() > 0){
				String newVar = r.left + "'";
				V.add(newVar);
				LinkedList<String> temp5 = new LinkedList<String>();
				for(String s: beta){
					temp5.add(s + newVar);
				}
				
				Rule r3 = new Rule(r.left, temp5);
				R.set(i, r3);
				temp5 = new LinkedList<String>();
				for(String s: alpha){
					temp5.add(s + newVar);
				}
				temp5.add("e");
				temp3.add(new Rule(newVar, temp5));
				

			}
		}


			

			R.addAll(temp3)	;

		}

		
		
		

	

}



