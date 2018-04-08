import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

import java.io.*;
import java.util.*;

/**
 * Created by Devroop Kar on 31-03-2018.
 */
public class LL_1 {
    private HashMap<String, ArrayList<ArrayList<String>>> productions = new HashMap<>();
    private ArrayList<String> terminals = new ArrayList<>();
    private ArrayList<String> non_terminals = new ArrayList<>();
    //private ArrayList<String> [][]parsing_table;
    private Table<String,String,ArrayList<String>> parsing_table;

    private HashMap<String, HashSet<String>> first = new HashMap<>();
    private HashMap<String, HashSet<String>> follow = new HashMap<>();

    private String FILENAME= "file.c";
    private HashMap<String, ArrayList<String>> tokenClass = new HashMap<>();

    public LL_1(HashMap<String, ArrayList<ArrayList<String>>> productions,
                ArrayList<String> terminals, ArrayList<String> non_terminals) {
        this.productions = productions;
        this.terminals = terminals;
        this.non_terminals = non_terminals;
        parsing_table = HashBasedTable.create();
        typeInit();
    }

    private void typeInit() {
        ArrayList<String> type = new ArrayList<>();
        String tokenTypeName = "Keyword";
        type.add("else");type.add("if");type.add("for");type.add("int");type.add("float");type.add("void");type.add("return");type.add("main");
        tokenClass.put(tokenTypeName, type);
        tokenTypeName = "arith-op";
        //type.clear();
        type = new ArrayList<>();
        type.add("+");type.add("-");type.add("*");type.add("/");
        tokenClass.put(tokenTypeName, type);
        tokenTypeName = "rel-op";
        //type.clear();
        type = new ArrayList<>();
        type.add("<");type.add(">");type.add("<=");type.add(">=");type.add("==");
        tokenClass.put(tokenTypeName, type);
        //type.clear();
        type = new ArrayList<>();
        tokenTypeName = "assign-op";
        type.add("=");
        tokenClass.put(tokenTypeName, type);
        //type.clear();
        type = new ArrayList<>();
        tokenTypeName = "Punctuation";
        type.add(";");type.add(",");type.add("(");type.add(")");type.add("{");type.add("}");
        tokenClass.put(tokenTypeName, type);
        //type.clear();
    }

    private boolean isTerminal(String symbol) {
        for(int i=0;i<terminals.size();i++){
            if(symbol.equals(terminals.get(i)))
                return true;
        }
        return false;
    }

    /*private void Follow(){}*/

    public void constructParsingTable(){
        try{
            ArrayList<String> proc_follow, first_follow;
            //BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File("C:\\Users\\Devroop Kar\\Documents\\Parser\\first_follow.txt"))));
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File("first_follow.txt"))));
            String s ;
            while((s = br.readLine())!=null){
                proc_follow = proc_listing(s);
                String symbol = proc_follow.get(0);
                proc_follow.remove(0);
                first_follow = Compute_First(proc_follow.get(proc_follow.size()-1),symbol);
                proc_follow.remove(proc_follow.size()-1);
                for(int i=0;i<first_follow.size();i++){
                    parsing_table.put(symbol, first_follow.get(i), proc_follow);
                }
            }
            //System.out.println(parsing_table);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private ArrayList<String> Compute_First(String s, String symbol) {
        ArrayList<String> arr = new ArrayList<>();
        int flag = 0;
        if(s.indexOf("}")!= s.lastIndexOf("}"))flag = 1;
        StringTokenizer str = new StringTokenizer(s, "{} ");
        while(str.hasMoreTokens()){
            arr.add(str.nextToken());
        }
        if(symbol.equals("Body") && flag==1) {
            arr.add("}");
            flag = 0;
        }
        return arr;
    }

    private ArrayList<String> proc_listing(String s) {
        ArrayList<String> arr = new ArrayList<>();
        StringTokenizer str = new StringTokenizer(s,"\t~");
        while(str.hasMoreTokens()){
            arr.add(str.nextToken());
        }
        return arr;
    }

    private ArrayList<String> generateTokens(String s){
        /**
         * using lexical analyzer
         */
        FILENAME = s;
        ArrayList<String> arr = new ArrayList<>();
        LexicalAnalyzer la = new LexicalAnalyzer(FILENAME);
        arr = la.getTokenList();
        return  arr;
    }


    public boolean parseString(String s){
        ArrayList<String> tokens = generateTokens(s);
        ArrayList<String> production = null;
        boolean flag = false;
        Collections.reverse(tokens);
        Stack<String> string_token = new Stack<>();
        Stack<String> parser = new Stack<>();
        Stack<String> print_input;
        for(int i=0;i<tokens.size();i++){
            string_token.push(tokens.get(i));
        }
        parser.push(non_terminals.get(0));          //start symbol is on parsing stack
        try{
            while(!string_token.empty() && !parser.empty()){
                constructParsingTable();
                System.out.println("$"+parser+"\t\t\t\t\t\t\t"+TopDownStack(string_token));
                String symbol = parser.peek();
                String token = string_token.peek();
                if(isTerminal(symbol) && isTerminal(token) && symbol.equals(token)){
                    parser.pop();
                    string_token.pop();
                    System.out.println("$"+parser+"\t\t\t\t\t\t\t"+TopDownStack(string_token));
                    if (string_token.empty() && parser.empty())break;
                } else if(!parsing_table.contains(symbol, token)) throw new Exception();
                else {
                    parser.pop();
                    //string_token.pop();
                    if(symbol.equals("ID") && token.equals("varname"))
                        production = new ArrayList<>(Arrays.asList("varname","S1"));
                    else if(symbol.equals("S1") && token.equals("="))
                        production = new ArrayList<>(Arrays.asList("=","Init"));
                    else if (symbol.equals("Init") && token.equals("Number"))
                        production = new ArrayList<>(Arrays.asList("Constant","Ending"));
                    else
                        production = parsing_table.get(symbol, token);
                    Collections.reverse(production);
                    if (!flag && !production.get(0).equals("$")) {
                        for (int i = 0; i < production.size(); i++) {
                            parser.push(production.get(i));
                        }
                    } else if (flag) {
                        parser.push("$");
                        flag = false;
                    }
                }
                System.out.println("$"+parser+"\t\t\t\t\t\t\t"+TopDownStack(string_token));
                String id = parser.peek();
                if(id.equals(string_token.peek())){
                    parser.pop();
                    string_token.pop();
                    System.out.println("$"+parser+"\t\t\t\t\t\t\t"+TopDownStack(string_token));
                    if (string_token.empty() && parser.empty())break;
                }
                if (string_token.empty() && !parser.empty()){
                    flag = true;
                    string_token.push("$");
                    System.out.println("$"+parser+"\t\t\t\t\t\t\t"+TopDownStack(string_token));
                }
            }
            return true;
        }catch (Exception e){
            System.out.println("\n\nParsing Error...");
            return false;
        }
    }

    private String TopDownStack(Stack<String> parser) {
        StringBuffer sbf = new StringBuffer();
        for(int i=parser.size()-1;i>=0;i--){
            sbf.append(parser.get(i)).append(" ");
        }
        sbf.append("$");
        return sbf.toString();
    }

    public Table<String, String, ArrayList<String>> getParsing_table() {
        return parsing_table;
    }
}
