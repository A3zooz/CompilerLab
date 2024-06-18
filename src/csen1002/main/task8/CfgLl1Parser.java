package csen1002.main.task8;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Write your info here
 * 
 * @name Ahmed Mohamed Ahmed Azooz
 * @id 49-18699
 * @labNumber 16
 */

public class CfgLl1Parser {

	LinkedList<String> V = new LinkedList<String>();
	LinkedList<String> T = new LinkedList<String>();
	LinkedList<Rule> R = new LinkedList<Rule>();
	HashMap<String, LinkedList<String>> first = new HashMap<>();
	HashMap<String, LinkedList<String>> follow = new HashMap<>();


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
	 * @param cfg A formatted string representation of the CFG, the First sets of
	 *            each right-hand side, and the Follow sets of each variable. The
	 *            string representation follows the one in the task description
	 */
	public CfgLl1Parser(String input) {
		// TODO Auto-generated constructor stub
		String V = input.split("#")[0];
		String T = input.split("#")[1];
		String R = input.split("#")[2];
        Collections.addAll(this.V, V.split(";"));
        Collections.addAll(this.T, T.split(";"));
		for(String s : R.split(";")){
			String left = s.split("/")[0];
            LinkedList<String> right = new LinkedList<String>(Arrays.asList(s.split("/")[1].split(",")));
			this.R.add(new Rule(left, right));
		}
		String first = input.split("#")[3];
		for(String s : first.split(";")){
			String left = s.split("/")[0];
			LinkedList<String> right = new LinkedList<String>(Arrays.asList(s.split("/")[1].split(",")));
			this.first.put(left, right);
		}
		String follow = input.split("#")[4];
		for(String s : follow.split(";")){
			String left = s.split("/")[0];
			LinkedList<String> right = new LinkedList<String>(Arrays.asList(s.split("/")[1].split("")));
			this.follow.put(left, right);
		}
	}

	/**
	 * @param input The string to be parsed by the LL(1) CFG.
	 * 
	 * @return A string encoding a left-most derivation.
	 */
	public String parse(String input) {
		// TODO Auto-generated method stub
		
		Table table = generateParseTable();
		LinkedList<String> stack = new LinkedList<String>();
		stack.add("$");
		stack.add(V.get(0));
		LinkedList<String> output = new LinkedList<String>();
		input += "$";
		output.add(V.get(0));
		int index = 0;
		while(!stack.isEmpty()){
			String output1 = output.get(output.size()-1);
			String top = stack.removeLast();
			String next = input.substring(index, index+1);
			if(T.contains(top) || top.equals("$")){
				if(top.equals(next)){
					index++;
					continue;
				}else{
					output.add("ERROR");
					break;
				}
			}else{
				String rule = table.get(top, next);
				if(rule == null){
					output.add("ERROR");
					break;
				}
				output1 = output1.replaceFirst(top, rule.equals("e") ? "" : rule);
				if(!rule.equals("e")){
					for(int i = rule.length()-1; i >= 0; i--){
						stack.add(rule.substring(i, i+1));
					}
				}
				
			}
			output.add(output1);

				

		}
		return String.join(";", output);
				

	}

	public Table generateParseTable(){
		Table table = new Table();

		for(Rule r : R){
			String var = r.left;
			LinkedList<String> f = first.get(var);
			for(int i = 0; i < f.size(); i++){
				String term = f.toArray()[i].toString();
				if(!term.equals("e"))
					for(int j = 0; j < term.length(); j++)
						table.add(var, term.substring(j,j+1), r.right.get(i));
			}
			if(f.contains("e")){
				LinkedList<String> fo = follow.get(var);
				for(int i = 0; i < fo.size(); i++){
					String term = fo.toArray()[i].toString();
					if(!term.equals("e"))
						table.add(var, term, "e");
				}
			}

		}
		return table;


		

	}

	static class TableEntry{
		String var;
		String term;
		String rule;
		public TableEntry(String var, String term, String rule) {
			this.var = var;
			this.term = term;
			this.rule = rule;
		}
	}
	static class Table{
		LinkedList<TableEntry> table = new LinkedList<TableEntry>();
		public void add(String var, String term, String rule){
			table.add(new TableEntry(var, term, rule));
		}
		public String get(String var, String term){
			for(TableEntry t : table)
				if(t.var.equals(var) && t.term.equals(term))
					return t.rule;
			return null;
		}
	}

}
