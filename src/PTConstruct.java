import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

public class PTConstruct {
	ArrayList<String> statement;
	
	public PTConstruct(ArrayList<String> s){
		this.statement = s;
	}
	public ParseTree construct(){
		//if(statement.get(0).toLowerCase().equals("select")){
			ParseTree t;
			t = select();
			return t;
		//}
	}
	
	//zy: add the order_index
	public ParseTree select(){
		int count = 0;
		int from_index = 0, where_index = 0, order_index = 0;
		for(String s : this.statement){
	    	if(s.toLowerCase().equals("from")){
	    		from_index = count;
	    	}
	    	if(s.toLowerCase().equals("where")){
	    		where_index = count;
	    	}
	    	if(s.toLowerCase().equals("order")){
	    		order_index = count;
	    	}
	    	count += 1;
	    }
		//construct attributes list
		List<ParseTree> attributes = new ArrayList<ParseTree>();
		
		// zy: avoid disruption of ",", "(", ")"
		List valid = Arrays.asList(",", "(", ")");
		for(int i = 1; i < from_index; i++){
			if(!valid.contains(this.statement.get(i))){
				attributes.add(new ParseTree(this.statement.get(i)));
				//System.out.println("arraylist element:"+this.statement.get(i));
			}
		}
		
		ParseTree attri_list = new ParseTree("attri_list", attributes);
		
		//construct tables list
		List<ParseTree> tables = new ArrayList<ParseTree>();
		for(int i = from_index + 1; i < where_index; i++){
			if(!this.statement.get(i).equals(",")){
				tables.add(new ParseTree(this.statement.get(i)));
			}
		}
		ParseTree table_list = new ParseTree("table_list", tables);
		
		//construct conditions list
		List<ParseTree> conditions = new ArrayList<ParseTree>();
		
		
		List valid1 = Arrays.asList("AND","OR");
		
		// index for the condition loop
		int searchindex = 0;
		if (order_index == 0){
			searchindex = this.statement.size();
		}
		else{
			searchindex = order_index;
		}
		
		// zy: indicate the index of the last "AND", "OR"
		int last =  where_index ; 
		
		// zy: when meet "AND" "OR" extract concessitive 3 elements as a tree of subcondition, "AND""OR" goes to condition tree
		for(int i = where_index + 1; i < searchindex; i++){
			if(valid1.contains(this.statement.get(i))){
				List<ParseTree> subcondition = new ArrayList<ParseTree>(); 
				for (int j = last+1; j < i; j++ ){
					
					subcondition.add(new ParseTree(this.statement.get(j)));
					System.out.println("subcondtion:"+ this.statement.get(j));
					
				}
				last = i;
				ParseTree subcondition_list = new ParseTree("subcondition_list", subcondition);
				conditions.add(subcondition_list);
				conditions.add(new ParseTree(this.statement.get(i)));
				
				
			}
		}
		
		// add the last subcondition after the last AND OR
		List<ParseTree> subcondition = new ArrayList<ParseTree>(); 
		for (int k = last+1; k< searchindex-1; k++){
			subcondition.add(new ParseTree(this.statement.get(k)));
			System.out.println("subcondtion:"+ this.statement.get(k));		
		}
		ParseTree subcondition_list = new ParseTree("subcondition_list", subcondition);
		conditions.add(subcondition_list);
		
		// construct condition_list ParseTree, if 5 children, then 2 AND OR, 2 subcondition
		ParseTree condition_list = new ParseTree("condition_list", conditions);
		
		//construct whole ParseTree
		List<ParseTree> root_list = new ArrayList<ParseTree>();
		root_list.add(attri_list);
		root_list.add(table_list);
		root_list.add(condition_list);
		ParseTree root = new ParseTree("select", root_list);
		return root;
	}
}