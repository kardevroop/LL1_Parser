import com.google.common.collect.Table;

import java.io.*;
import java.util.*;

/**
 * Created by Devroop Kar on 30-03-2018.
 */
public class Compiler {
    private static HashMap<String, ArrayList<ArrayList<String>>> productions = new HashMap<>();
    private static ArrayList<String> terminals = new ArrayList<>();
    private static ArrayList<String> non_terminals = new ArrayList<>();
    private static final String INPUT_FILENAME = "file2.c";
    ArrayList<String> [][]parsing_table;

    public static void main(String args[]){
        generateTerminalList();
        generateNonTerminalList();
        generateProductions();
       /* Set set = productions.entrySet();
        Iterator it = set.iterator();
        while(it.hasNext()){
            Map.Entry me = (Map.Entry)it.next();
            System.out.println(me.getKey()+"---->"+me.getValue());
        }*/
        LL_1 parser = new LL_1(productions, terminals, non_terminals);
        parser.constructParsingTable();
        //parser.generateTokens(INPUT_FILENAME);
        Table<String, String, ArrayList<String>> table = parser.getParsing_table();
        //System.out.println(table);
        if(parser.parseString(INPUT_FILENAME))
            System.out.print("String parsed");
    }

    private static void generateProductions() {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(
                    new File("cfg2.txt"))));
            String s = br.readLine();;
            while(s != null){
                storeProduction(s);
                s = br.readLine();
            }
            br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void storeProduction(String s) {
        ArrayList<ArrayList<String>> proc = new ArrayList<>();
        ArrayList<String> sym = new ArrayList<>();
        StringTokenizer token = new StringTokenizer(s, " ->");
        while(token.hasMoreTokens()){
            String tok = token.nextToken();
            sym.add(tok);
        }
        String lhs = sym.get(0);
        if(lhs.equals("UnaryOp"))sym.add("-");
        if(lhs.equals("CHAR")){
            sym.add("-");sym.add(">");sym.add("|");
        }
        if(lhs.equals("ArithOp"))sym.add("-");
        sym.remove(0);
        if(lhs.equals("CHAR") || lhs.equals("Alpha") || lhs.equals("AlphaNum") || lhs.equals("Digit") || lhs.equals("Vd")){
            sym = getList(sym);
        }
        sym.add(0, lhs);
        if(needToUpdateProduction(lhs)) {
            proc = getLastProcList(lhs);
        }
            proc.add(sym);
            productions.put(lhs, proc);
        //productions.g
    }

    private static ArrayList<ArrayList<String>> getLastProcList(String lhs) {
        ArrayList<ArrayList<String>> arr = null;
        Set set = productions.entrySet();
        Iterator it = set.iterator();
        while(it.hasNext()){
            Map.Entry me = (Map.Entry)it.next();
            if(lhs.equals(me.getKey())){
                arr = (ArrayList<ArrayList<String>>) me.getValue();
                break;
            }
        }
        return arr;
    }

    private static boolean needToUpdateProduction(String lhs) {
        Set set = productions.entrySet();
        Iterator it = set.iterator();
        while(it.hasNext()){
            Map.Entry me = (Map.Entry)it.next();
            if(lhs.equals(me.getKey()))
                return true;
        }
        return false;
    }

    private static ArrayList<String> getList(ArrayList<String> sym) {
        ArrayList<String> arr = new ArrayList<>();
        String s = sym.get(0);
        StringTokenizer str = new StringTokenizer(s, "|");
        while (str.hasMoreTokens()){
            arr.add(str.nextToken());
        }
        return arr;
    }

    private static void generateNonTerminalList() {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(
                    new File("symbol2.txt"))));
            String s = br.readLine();
            while(s != null){
                if(!non_terminals.contains(s))
                    non_terminals.add(s);
                s = br.readLine();
            }
            br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void generateTerminalList() {
        try {
//            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(
//                    new File("C:\\Users\\Devroop Kar\\Documents\\Parser\\terminal2.txt"))));
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(
                    new File("terminal2.txt"))));
            String s = br.readLine();
            while(s != null){
                if(!terminals.contains(s))
                    terminals.add(s);
                s = br.readLine();
            }
            br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
