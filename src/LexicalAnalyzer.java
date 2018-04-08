import java.io.*;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Created by Devroop Kar on 08-04-2018.
 */
public class LexicalAnalyzer {

    private  String FILENAME = "";
    private  HashMap<String, ArrayList<String>> tokenClass = new HashMap<>(); //to store all the token types
    private  HashMap<String, String> SYMBOL_TABLE = new HashMap<>();          //not needed
    //private HashMap<Integer, PositionPair> POSITION_TABLE = new HashMap<>(); //to store all the row-column positions of each lexemes
    private  HashMap<Integer, ArrayList<String>> SYMBOL_TABLE_ = new HashMap<>(); //storing all tabular information
    private  int lineNumber = 0;  //row count
    private int columnNumber = 0;   //column count
    static boolean flag = false;

    ArrayList<String> arr = new ArrayList<>();

    public LexicalAnalyzer(String s) {
        this.FILENAME = s;
        typeInit();
        generateTokens();
    }

    private void generateTokens() {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(FILENAME))));
            String str ;
            while((str=br.readLine())!=null){
                lexAnalyze(str);
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private void lexAnalyze(String s) {
        int i=0;
        int columnNumber = 0;
        Random rnd = new Random(); //generating random numbers for TOKEN ID
        int TOKEN_ID = 0;
        for(i=0;i<s.length();){
            //System.out.println("la\n");
            switch(s.charAt(i)){    //checking each character to create valid tokens
                case 'e':   //letter e is the beginning of else statement or any variable name
                    TOKEN_ID = rnd.nextInt(1000);
                    if (flag){  //comment line check
                        i++;
                    } else if(s.charAt(i+1)=='l' && s.charAt(i+2)=='s' && s.charAt(i+3)=='e' && s.charAt(i+4)==' '){
//                        TOKEN_ID = rnd.nextInt(1000);
                        String type = returnTokenType("else");
                        //ArrayList<String> arr = new ArrayList<>();
                        //arr.add("else");
                        arr.add(type);
//                        SYMBOL_TABLE.put("else", type);
//                        SYMBOL_TABLE_ .put(TOKEN_ID, arr);
                        //POSITION_TABLE.put(TOKEN_ID, new PositionPair(lineNumber, i+1));
                        i = i+4;
                    }else {
                        StringBuffer sbf = new StringBuffer();
                        while(i<s.length() && (s.charAt(i)!=' ' && s.charAt(i)!=',' && s.charAt(i)!=';' && s.charAt(i)!='='
                                && s.charAt(i)!='<' && s.charAt(i)!='>' && s.charAt(i)!='+' && s.charAt(i)!='-'
                        && s.charAt(i)!='\'')){
                            sbf.append(s.charAt(i));
                            i++;
                        }
                        String str = sbf.toString();
                        //SYMBOL_TABLE.put(str, returnTokenType(str));
                        //ArrayList<String> arr = new ArrayList<>();
                        //arr.add(str);
                         arr.add(returnTokenType(str));
                        //SYMBOL_TABLE_.put(TOKEN_ID, arr);
                        //POSITION_TABLE.put(TOKEN_ID, new PositionPair(lineNumber, columnNumber+1));
                    }
                    break;
                case 'f': //f means a for loop or a float type or a variable name
                    TOKEN_ID = rnd.nextInt(1000);
                    if(flag)i++;
                    else if(s.charAt(i+1)=='o' && s.charAt(i+2)=='r' && (s.charAt(i+3)=='(' || s.charAt(i+3)==' ')){
//                        SYMBOL_TABLE.put("for",returnTokenType("for"));
//                        ArrayList<String> arr = new ArrayList<>();
                       // arr.add("for");
                        arr.add(returnTokenType("for"));
                      //  SYMBOL_TABLE_.put(TOKEN_ID, arr);
                        //POSITION_TABLE.put(TOKEN_ID, new PositionPair(lineNumber, i+1));
                        i = i+3;
                    }else {
                        StringBuffer sbf = new StringBuffer();
                        columnNumber = i;
                        while (i<s.length() && (s.charAt(i)!=' ' && s.charAt(i)!=';') && s.charAt(i)!=',' &&
                                s.charAt(i)!='=' && s.charAt(i)!='<' && s.charAt(i)!='>' && s.charAt(i)!='+' && s.charAt(i)!='-'
                                && s.charAt(i)!='\''){
                            sbf.append(s.charAt(i));
                            i++;
                        }
                        String str = sbf.toString();
                        if (str.equals("float")) {
//                            SYMBOL_TABLE.put("float", returnTokenType("float"));
//                            ArrayList<String> arr = new ArrayList<>();
//                            arr.add("float");
                            arr.add(returnTokenType("float"));
                            //SYMBOL_TABLE_.put(TOKEN_ID, arr);
                            //POSITION_TABLE.put(TOKEN_ID, new PositionPair(lineNumber, columnNumber+1));
                        }else {
//                            SYMBOL_TABLE.put(str, returnTokenType(str));
//                            ArrayList<String> arr = new ArrayList<>();
                            arr.add(str);arr.add(returnTokenType(str));
                            //SYMBOL_TABLE_.put(TOKEN_ID, arr);
                            //POSITION_TABLE.put(TOKEN_ID, new PositionPair(lineNumber, columnNumber+1));
                        }
                    }
                    break;
                case 'i':   //if statement or int type or variable name
                    TOKEN_ID = rnd.nextInt(1000);
                    //System.out.println("code\n");
                    if (flag)i++;
                    else if(s.charAt(i+1)=='f' && (s.charAt(i+2)=='(' || s.charAt(i+2)==' ')){
//                        String type = returnTokenType("if");
//                        SYMBOL_TABLE.put("if", returnTokenType(type));
//                        ArrayList<String> arr = new ArrayList<>();
//                        arr.add("if");
                        arr.add(returnTokenType("if"));
                        //SYMBOL_TABLE_.put(TOKEN_ID, arr);
                        //POSITION_TABLE.put(TOKEN_ID, new PositionPair(lineNumber, i+1));
                        i=i+2;
                    }else if (s.charAt(i+1)=='n' && s.charAt(i+2)=='t' && s.charAt(i+3)==' '){
//                        SYMBOL_TABLE.put("int", returnTokenType("int"));
//                        ArrayList<String> arr = new ArrayList<>();
//                        arr.add("int");
                        arr.add(returnTokenType("int"));
                        //SYMBOL_TABLE_.put(TOKEN_ID, arr);
                        //POSITION_TABLE.put(TOKEN_ID, new PositionPair(lineNumber, i+1));
                        i=i+3;
                    }else {
                        StringBuffer sbf = new StringBuffer();
                        columnNumber = i;
                        /*while (i<s.length() && s.charAt(i)!=' '){
                            sbf.append(s.charAt(i));
                            i++;
                        }*/
                        while(i<s.length() && (s.charAt(i)!=' ' && s.charAt(i)!=',' && s.charAt(i)!=';' && s.charAt(i)!='='
                                && s.charAt(i)!='<' && s.charAt(i)!='>' && s.charAt(i)!='+' && s.charAt(i)!='-' && s.charAt(i)!='\'')){
                            sbf.append(s.charAt(i));
                            i++;
                        }
                        String str = sbf.toString();
                        //SYMBOL_TABLE.put(str, returnTokenType(str));
                        //ArrayList<String> arr = new ArrayList<>();
                        //arr.add(str);
                        arr.add(returnTokenType(str));
                        //SYMBOL_TABLE_.put(TOKEN_ID, arr);
                        //POSITION_TABLE.put(TOKEN_ID, new PositionPair(lineNumber, columnNumber+1));
                    }
                    break;
                case 'r':   //return statement or variable name
                    TOKEN_ID = rnd.nextInt(1000);
                    if(!flag) {
                        StringBuffer sbf = new StringBuffer();
                        columnNumber = i;
                        while (i < s.length() && (s.charAt(i) != ' ' && s.charAt(i) != ';')) {
                            sbf.append(s.charAt(i));
                            i++;
                        }
                        String str = sbf.toString();
                        if (str.equals("return")) {
//                            SYMBOL_TABLE.put(str, returnTokenType(str));
//                            ArrayList<String> arr = new ArrayList<>();
//                            arr.add(str);
                            arr.add(returnTokenType(str));
                         //   SYMBOL_TABLE_.put(TOKEN_ID, arr);
                            //POSITION_TABLE.put(TOKEN_ID, new PositionPair(lineNumber, columnNumber+1));
                        } else {
                            StringBuffer sbf2 = new StringBuffer();
                            columnNumber = i;
                            while (i<s.length() && (s.charAt(i) != ' ' && s.charAt(i) != ',' && s.charAt(i) != ';' && s.charAt(i) != '='
                                    && s.charAt(i)!='<' && s.charAt(i)!='>' && s.charAt(i)!='+' && s.charAt(i)!='-'
                            && s.charAt(i)!='\'')) {
                                sbf.append(s.charAt(i));
                                i++;
                            }
                            String str2 = sbf.toString();
                            arr.add(returnTokenType(str2));
                        }
                    }else i++;
                    break;
                case 'v':   //void type or variable name
                    TOKEN_ID = rnd.nextInt(1000);
                    if(!flag) {
                        StringBuffer sb = new StringBuffer();
                        columnNumber = i;
                        while (i < s.length() && ((s.charAt(i) != ')') && (s.charAt(i) != ' ' && s.charAt(i)!= '='
                                && s.charAt(i)!='<' && s.charAt(i)!='>' && s.charAt(i)!='+' && s.charAt(i)!='-'
                        && s.charAt(i)!='\''))) {
                            sb.append(s.charAt(i));
                            i++;
                        }
                        String st = sb.toString();
                        if (st.equals("void")) {
//                            SYMBOL_TABLE.put("void", returnTokenType("void"));
//                            ArrayList<String> arr1 = new ArrayList<>();
//                            arr1.add("void");
                            arr.add(returnTokenType("void"));
                            //SYMBOL_TABLE_.put(TOKEN_ID, arr1);
                            //POSITION_TABLE.put(TOKEN_ID, new PositionPair(lineNumber, columnNumber+1));
                        } else {
//                            SYMBOL_TABLE.put(st, returnTokenType(st));
//                            ArrayList<String> arr1 = new ArrayList<>();
//                            arr1.add(st);
                            arr.add(returnTokenType(st));
                            //SYMBOL_TABLE_.put(TOKEN_ID, arr1);
                            //POSITION_TABLE.put(TOKEN_ID, new PositionPair(lineNumber, columnNumber+1));
                        }
                    }else i++;
                    break;
                case '(':
                    TOKEN_ID = rnd.nextInt(1000);
                    if(!flag) {
//                        SYMBOL_TABLE.put("(", returnTokenType("("));
//                        ArrayList<String> arr2 = new ArrayList<>();
//                        arr2.add("(");
                        arr.add(returnTokenType("("));
                    //    SYMBOL_TABLE_.put(TOKEN_ID, arr2);
                        //POSITION_TABLE.put(TOKEN_ID, new PositionPair(lineNumber, i+1));
                        i++;
                    }else i++;
                    break;
                case ')':
                    TOKEN_ID = rnd.nextInt(1000);
                    if(!flag) {
//                        SYMBOL_TABLE.put(")", returnTokenType(")"));
//                        ArrayList<String> ar = new ArrayList<>();
//                        ar.add(")");
                        arr.add(returnTokenType(")"));
                     //   SYMBOL_TABLE_.put(TOKEN_ID, ar);
                        //POSITION_TABLE.put(TOKEN_ID, new PositionPair(lineNumber, i+1));
                        i++;
                    }else i++;
                    break;
                case '+':
                    TOKEN_ID = rnd.nextInt(1000);
                    if(!flag) {
//                        SYMBOL_TABLE.put("+", returnTokenType("+"));
//                        ArrayList<String> arl = new ArrayList<>();
//                        arl.add("+");
                        arr.add(returnTokenType("+"));
                    //    SYMBOL_TABLE_.put(TOKEN_ID, arl);
                        //POSITION_TABLE.put(TOKEN_ID, new PositionPair(lineNumber, i+1));
                        i++;
                    }else i++;
                    break;
                case '-':
                    TOKEN_ID = rnd.nextInt(1000);
                    if(!flag) {
//                        SYMBOL_TABLE.put("-", returnTokenType("-"));
//                        ArrayList<String> arrayList = new ArrayList<>();
//                        arrayList.add("-");
                        arr.add(returnTokenType("-"));
                    //    SYMBOL_TABLE_.put(TOKEN_ID, arrayList);
                        //POSITION_TABLE.put(TOKEN_ID, new PositionPair(lineNumber, i+1));
                        i++;
                    }else i++;
                    break;
                case '*':
                    TOKEN_ID = rnd.nextInt(1000);
                    if (flag){
                        if (i < s.length()-1 && s.charAt(i + 1) == '/'){
                            flag = false;
                        }
                        i++;
                    }else {
//                        SYMBOL_TABLE.put("*", returnTokenType("*"));
//                        ArrayList<String> a = new ArrayList<>();
//                        a.add("*");
                        arr.add(returnTokenType("*"));
                       // SYMBOL_TABLE_.put(TOKEN_ID, a);
                        //POSITION_TABLE.put(TOKEN_ID, new PositionPair(lineNumber, i+1));
                        i++;
                    }
                    break;
                case '/':
                    TOKEN_ID = rnd.nextInt(1000);
                    if(!flag) {
                        if (i < s.length()-1 && s.charAt(i + 1) == '/') {
                            while (i < s.length()) {
                                i++;
                            }
                        } else if (i < s.length()-1 && s.charAt(i + 1) == '*') {
                            flag = true;
                            i++;
                        } else {
//                            SYMBOL_TABLE.put("/", returnTokenType("/"));
//                            ArrayList<String> arr3 = new ArrayList<>();
//                            arr3.add("/");
                            arr.add(returnTokenType("/"));
                            //SYMBOL_TABLE_.put(TOKEN_ID, arr3);
                           //POSITION_TABLE.put(TOKEN_ID, new PositionPair(lineNumber, i+1));
                            i++;
                        }
                    }else i++;
                    break;
                case '{':
                    TOKEN_ID = rnd.nextInt(1000);
                    if(!flag) {
//                        SYMBOL_TABLE.put("{", returnTokenType("{"));
//                        ArrayList<String> arr4 = new ArrayList<>();
//                        arr4.add("{");
                        arr.add(returnTokenType("{"));
                     //   SYMBOL_TABLE_.put(TOKEN_ID, arr4);
                        //POSITION_TABLE.put(TOKEN_ID, new PositionPair(lineNumber, i+1));
                        i++;
                    }else i++;
                    break;
                case '\t':  //tab symbol is ignored
                    //if(s.charAt(i+1) >= 'a' && s.charAt(i+1) <= 'z')
                    //    continue;
                    i++;
                    break;
                case '}':
                    TOKEN_ID = rnd.nextInt(1000);
                    if(!flag) {
                   //     SYMBOL_TABLE.put("}", returnTokenType("}"));
                   //     ArrayList<String> arr5 = new ArrayList<>();
                   //     arr5.add("}");
                        arr.add(returnTokenType("}"));
                        //SYMBOL_TABLE_.put(TOKEN_ID, arr5);
                       // POSITION_TABLE.put(TOKEN_ID, new PositionPair(lineNumber, i+1));
                        i++;
                    }else i++;
                    break;
                case '=':
                    TOKEN_ID = rnd.nextInt(1000);
                    if(!flag) {
                     //   SYMBOL_TABLE.put("=", returnTokenType("="));
                     //   ArrayList<String> arr6 = new ArrayList<>();
                     //   arr6.add("=");
                        arr.add(returnTokenType("="));
                     //   SYMBOL_TABLE_.put(TOKEN_ID, arr6);
                       // POSITION_TABLE.put(TOKEN_ID, new PositionPair(lineNumber, i+1));
                        i++;
                    }else i++;
                    break;
                case '>':
                    TOKEN_ID = rnd.nextInt(1000);
                    if(!flag) {
                       // SYMBOL_TABLE.put(">", returnTokenType(">"));
                        //ArrayList<String> arr7 = new ArrayList<>();
                        //arr7.add(">");
                        arr.add(returnTokenType(">"));
                        //SYMBOL_TABLE_.put(TOKEN_ID, arr7);
                      //  POSITION_TABLE.put(TOKEN_ID, new PositionPair(lineNumber, i+1));
                        i++;
                    }else i++;
                    break;
                case '<':
                    TOKEN_ID = rnd.nextInt(1000);
                    if(!flag) {
                        //SYMBOL_TABLE.put("<", returnTokenType("<"));
                        //ArrayList<String> arr8 = new ArrayList<>();
                        //arr8.add("<");
                        arr.add(returnTokenType("<"));
                        //SYMBOL_TABLE_.put(TOKEN_ID, arr8);
                     //   POSITION_TABLE.put(TOKEN_ID, new PositionPair(lineNumber, i+1));
                        i++;
                    }else i++;
                    break;
                case ';':
                    TOKEN_ID = rnd.nextInt(1000);
                    if(!flag) {
                        //SYMBOL_TABLE.put(";", returnTokenType(";"));
                       // ArrayList<String> arr9 = new ArrayList<>();
                        //arr9.add(";");
                        arr.add(returnTokenType(";"));
                        //SYMBOL_TABLE_.put(TOKEN_ID, arr9);
                     //   POSITION_TABLE.put(TOKEN_ID, new PositionPair(lineNumber, i+1));
                        i++;
                    }else i++;
                    break;
                case ',':
                    TOKEN_ID = rnd.nextInt(1000);
                    if(!flag) {
//                        SYMBOL_TABLE.put(",", returnTokenType(","));
//                        ArrayList<String> arr10 = new ArrayList<>();
                        //arr10.add(",");
                        arr.add(returnTokenType(","));
                        //SYMBOL_TABLE_.put(TOKEN_ID, arr10);
                      //  POSITION_TABLE.put(TOKEN_ID, new PositionPair(lineNumber, i+1));
                        i++;
                    }else i++;
                    break;
                case ' ':
                    i++;
                    break;
                case '#':   //to account for #include directives
                    TOKEN_ID = rnd.nextInt(1000);
                    if(!flag) {
                        SYMBOL_TABLE.put("#", returnTokenType("#"));
                        ArrayList<String> arr11 = new ArrayList<>();
                        arr11.add("#");
                        arr11.add(returnTokenType("#"));
                        SYMBOL_TABLE_.put(TOKEN_ID, arr11);
                     //   POSITION_TABLE.put(TOKEN_ID, new PositionPair(lineNumber, i+1));
                        i++;
                    }else i++;
                    break;
                case '"':
                    StringBuffer sbuf8 = new StringBuffer();
                    columnNumber = i;
                    i++;
                    while (i < s.length() && s.charAt(i) != '"') {
                        sbuf8.append(s.charAt(i));
                        i++;
                    }
                    SYMBOL_TABLE.put(sbuf8.toString(), "Literal");
                    ArrayList<String> arr13 = new ArrayList<>();
                    arr13.add(sbuf8.toString());
                    arr13.add("Literal");
                    SYMBOL_TABLE_.put(TOKEN_ID, arr13);
                  // POSITION_TABLE.put(TOKEN_ID, new PositionPair(lineNumber, columnNumber+1));
                    i++;
                    break;
                case '\'':
                arr.add(returnTokenType("'"));
                i++;
                break;
                case '&':
                    arr.add(returnTokenType("&"));
                    i++;
                    break;
                case '|': arr.add(returnTokenType("|"));
                i++;
                break;
                default: //any variable declaration or numeric constant assignment
                    TOKEN_ID = rnd.nextInt(1000);
                    //SYMBOL_TABLE.put(String.valueOf(s.charAt(i)), returnTokenType(String.valueOf(s.charAt(i))));
                    //i++;
                    if(!flag) {
                        StringBuffer sbuf = new StringBuffer();
                        columnNumber = i;
                        while (i<s.length() && (s.charAt(i) != ' ' && s.charAt(i) != ',' && s.charAt(i) != ';' && s.charAt(i) != '='
                        && s.charAt(i)!='<' && s.charAt(i)!='>' && s.charAt(i)!='+' && s.charAt(i)!='-' && s.charAt(i)!='\'')) {
                            sbuf.append(s.charAt(i));
                            i++;
                        }
                        String st = sbuf.toString();
                        SYMBOL_TABLE.put(sbuf.toString(), returnTokenType(sbuf.toString()));
                        //ArrayList<String> arr12 = new ArrayList<>();
                        //arr.add(sbuf.toString());
                        arr.add(returnTokenType(sbuf.toString()));
                        //SYMBOL_TABLE_.put(TOKEN_ID, arr12);
                     //   POSITION_TABLE.put(TOKEN_ID, new PositionPair(lineNumber, columnNumber+1));
                        //i++;
                    }else i++;
                    break;
            }
        }
    }

    private String returnTokenType(String s) {
        Set entrySet = tokenClass.entrySet();
        Iterator it = entrySet.iterator();
        while (it.hasNext()){
            Map.Entry me = (Map.Entry)it.next();
            String key = me.getKey().toString();
            ArrayList<String> values = (ArrayList<String>) me.getValue();
            if(key.equals("Keyword")){
                if (values.contains(s))
                    return s;
            }else if (key.equals("arith-op")){
                if (values.contains(s))
                    return s;
            }else if (key.equals("rel-op")){
                if (values.contains(s))
                    return s;
            }else if (key.equals("assign-op")){
                if (values.contains(s))
                    return s;
            }else if (key.equals("Punctuation")){
                if (values.contains(s))
                    return s;
            }
        }
        if (s.equals("&"))
            return "&";
        if (s.equals("|"))
            return "|";
        if (Pattern.compile("-?([0-9]+|[0-9]*\\.[0-9]+([eE][-+]?[0-9]+)?)").matcher(s).matches())
            return "Number";
        if (Pattern.compile("(_{0,1}[a-zA-z]+[a-zA-z0-9]*)|(_[0-9]+)").matcher(s).matches())
            return "varname";
        return "invalid";
    }

    private void typeInit() {
        ArrayList<String> type = new ArrayList<>();
        String tokenTypeName = "Keyword";
        type.add("else");type.add("if");type.add("for");type.add("int");type.add("float");type.add("void");type.add("return");type.add("main");
        type.add("char");
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
        type.add(";");type.add(",");type.add("(");type.add(")");type.add("{");type.add("}");type.add("'");
        tokenClass.put(tokenTypeName, type);
        //type.clear();
    }

    public ArrayList<String> getTokenList(){
        return arr;
    }
}
