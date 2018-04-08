import java.util.*;

/**
 * Created by Devroop Kar on 02-04-2018.
 */
public class demo {
    public static void main(String args[]){
        Stack<String> st = new Stack<>();
        st.push("1");
        st.push("2");
        st.push("3");
        st.push("4");
        st.push("5");
        st.push("6");
        //arr = st;
        Stack<String> arr = (Stack<String>) st.clone();
        arr.sort(Collections.reverseOrder());
        System.out.println(st);
    }
}
