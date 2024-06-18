package csen1002.main.task6;

import java.util.*;

/**
 * Write your info here
 * 
 * @name Ahmed Mohamed Ahmed Azooz
 * @id 49-18699
 * @labNumber 16
 */

public class CfgFirstFollow {
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


	}
	/**
	 * Constructs a Context Free Grammar
	 * 
	 * @param cfg A formatted string representation of the CFG. The string
	 *            representation follows the one in the task description
	 */
	public CfgFirstFollow(String cfg) {
		// TODO Auto-generated constructor stub
		String V = cfg.split("#")[0];
		String T = cfg.split("#")[1];
		String R = cfg.split("#")[2];
        Collections.addAll(this.V, V.split(";"));
        Collections.addAll(this.T, T.split(";"));
		for(String s : R.split(";")){
			String left = s.split("/")[0];
            LinkedList<String> right = new LinkedList<String>(Arrays.asList(s.split("/")[1].split(",")));
			this.R.add(new Rule(left, right));
		}
	}

	/**
	 * Calculates the First Set of each variable in the CFG.
	 * 
	 * @return A string representation of the First of each variable in the CFG,
	 *         formatted as specified in the task description.
	 */
	public String first() {
		// TODO Auto-generated method stub
		boolean flag = true;
		HashMap<String, TreeSet<String>> first = new HashMap<>();
		for(String v : V)
			first.put(v, new TreeSet<String>());
		for(String t : T)
			first.put(t, new TreeSet<String>(Collections.singletonList(t)));
		while(flag){
			flag = false;
			for(Rule r: R){
				TreeSet<String> f = new TreeSet<String>();
				for(String s: r.right){
					if(containsEpsilon(s, first, T)){
						if(!first.get(r.left).contains("e")){
							first.get(r.left).add("e");
							flag = true;
						}
					}

					for(int k = 0; k < s.length(); k++){
						String B = s.substring(0, k);
						if(containsEpsilon(B, first, T)){
							String yes = s.substring(k, k+1);
							if(T.contains(yes))
								f.add(yes);
							else if(V.contains(yes))
								f.addAll(first.get(yes));
							f.remove("e");
							flag = first.get(r.left).addAll(f) || flag;
						}
					}
				}
			}
		}
		StringBuilder res = new StringBuilder();
		for(String v : V){
			res.append(v).append("/");
			for(String s : first.get(v))
				res.append(s);
			res.append(";");
		}
		return res.substring(0, res.length()-1);
		

	}

	/**
	 * Calculates the Follow Set of each variable in the CFG.
	 * 
	 * @return A string representation of the Follow of each variable in the CFG,
	 *         formatted as specified in the task description.
	 */
	public String follow() {
		// TODO Auto-generated method stub
		HashMap<String, TreeSet<String>> follow = new HashMap<>();
		for(String v: V){
			if(!v.equals("S"))
				follow.put(v, new TreeSet<String>());
			else
				follow.put(v, new TreeSet<String>(List.of("$")));
		}
		boolean changed = true;
		while(changed){
			changed = false;
			for(Rule r: R){
				for(String s: r.right){
					for(int i = 0; i < s.length(); i++){
						String c = s.substring(i, i+1);
						if(V.contains(c)){		
							String beta = s.substring(i+1);
							TreeSet<String> f = getFirstString(beta);
							boolean hasEpsilon = f.contains("e");
							f.remove("e");
							boolean isSubset = follow.get(c).containsAll(f);
							if(!isSubset){
								changed = true;
								follow.get(c).addAll(f);
							}
							if(hasEpsilon || i + 1 == s.length()){
								boolean isSubset2 = follow.get(c).containsAll(follow.get(r.left));
								if(!isSubset2){
									changed = true;
									follow.get(c).addAll(follow.get(r.left));
								}
							}


						}

						}
				}
			}
		}
		StringBuilder res = new StringBuilder();
		for(String v : V){
			res.append(v).append("/");
			for(String s : follow.get(v))
				res.append(s);
			res.append(";");
		}
		return res.substring(0, res.length()-1);
	}

	public static boolean containsEpsilon(String s, HashMap<String, TreeSet<String>> first, LinkedList<String> T){
		if(s.equals("e"))
			return true;
		for(int i = 0; i < s.length(); i++){
			if(!first.get(s.substring(i, i+1)).contains("e"))
				return false;
			else if(T.contains(s.substring(i, i+1)))
				return false;
		}
		return true;
	}

	public  TreeSet<String> getFirstString(String s){
		TreeSet<String> f = new TreeSet<String>();
		HashMap<String, TreeSet<String>> first = getFirst();
		for(int i = 0; i < s.length(); i++){
			String c = s.substring(i, i+1);
			TreeSet<String> f2 = first.get(c) != null? first.get(c): new TreeSet<String>();
			f.addAll(f2);
			if(f2.contains("e"))
				f.remove("e");
			else
				break;


			
		}
		if(containsEpsilon(s, first, T))
			f.add("e");
		return f;
	}
	public  HashMap<String,TreeSet<String>> getFirst(){
		boolean flag = true;
		HashMap<String, TreeSet<String>> first = new HashMap<>();
		for(String v : V)
			first.put(v, new TreeSet<String>());
		for(String t : T)
			first.put(t, new TreeSet<String>(Collections.singletonList(t)));
		while(flag){
			flag = false;
			for(Rule r: R){
				TreeSet<String> f = new TreeSet<String>();
				for(String s: r.right){
					if(containsEpsilon(s, first, T)){
						if(!first.get(r.left).contains("e")){
							first.get(r.left).add("e");
							flag = true;
						}
					}

					for(int k = 0; k < s.length(); k++){
						String B = s.substring(0, k);
						if(containsEpsilon(B, first, T)){
							String yes = s.substring(k, k+1);
							if(T.contains(yes))
								f.add(yes);
							else if(V.contains(yes))
								f.addAll(first.get(yes));
							f.remove("e");
							flag = first.get(r.left).addAll(f) || flag;
						}
					}
				}
			}
		}
		return first;
	}
}
