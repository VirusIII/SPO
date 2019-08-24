import java.util.Stack;

public class TT {
    final static String[] singleOp = {"БП", "O", "R"};
    final static String[] operators = {"*", "D", "M", "+", "-", ":", "!", "=", "УЛП", "БП", "O", "R"};
    static String triadTable = ""; //таблица триад
    static int triN = 0;//номер триады
    static Stack<String> st = new Stack<>();

    static boolean IsOperator(String s) {
        for (int i = 0; i < operators.length; i++) {
            if (operators[i].equals(s))
                return true;
        }
        return false;
    }

    static boolean IsSingleOp(String s) {
        for (int i = 0; i < singleOp.length; i++) {
            if (s.equals(singleOp[i] + ""))
                return true;
        }
        return false;
    }

    public static String BuildTriads(String input) {
        String[] ops = input.split(" ");
        for (int i = 0; i < ops.length; i++) {
            if (IsOperator(ops[i])) {
                String triad = "T" + triN + " " +ops[i]+ " " + st.pop();
                if (!IsSingleOp(ops[i])) {
                    triad += " " + st.pop();
                }
                st.push("T" + triN);
                triadTable += triad + "\n";
                triN++;
            } else st.push(ops[i]);
        }
        return triadTable;
    }
}
