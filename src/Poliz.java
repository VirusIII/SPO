import java.util.Stack;

public class Poliz {
// Однооперандные команды
    final static char[] singleOp = { 'c', 'v', 'p', 'a', 'i', 'o' };
    static String priorityTable[][] = {
            { "(", "B", "W", "C", "V", "A", "I", "O"},
            { ")", "E", "D","{"},
            { ":", "!","="},
            { "+", "-" },
            { "*", "d","m"},
            { "r", "o" }
    };

    static int getPriority(String op) {
        for (int i = 0; i < priorityTable.length; i++) {
            for (int j = 0; j < priorityTable[i].length; j++) {
                if (op.equalsIgnoreCase(priorityTable[i][j]))
                    return i;
            }
        }
        return -1;
    }

    public static String buildPoliz(String input) {
        System.out.println("ПОЛИЗ:");
        input = input.toUpperCase();
        return new Poliz().getPoliz(input);
    }

    int pointerNumber = 0; // Указатель для циклов и условий
    static Stack<String> workSt; // Рабочий стек
    static String output; //выход стр
    static String input; //выход стр
    static int i=0; //указатель на текущий символ

//Вывод имени переменной в выходную стр.
    void nameToOut() {
        String name = input.substring(i-1).split(" ")[1];
//Переменные выводятся сразу в выводную стр.
        output += name + " ";
//Сдвиг указателя на длину имени и два пробела
        i += name.length() + 1;
    }

// Вытолкнуть всё исключительно
    void popUntil(String symbol) {

        while (!workSt.isEmpty() && !workSt.peek().equals(symbol)) {
            String p = workSt.pop();
            if (!p.equals("W") && !p.equals("F"))
                output += p + " ";
        }
    }

// Вытолкнуть для точки с зяпятой
    void popSemicolum() {
        while (!workSt.isEmpty()
                && !workSt.peek().equals("(")
                && !workSt.peek().equals("B")
                && !workSt.peek().equals("W")
                && !workSt.peek().equals("F")) {
            output += workSt.pop() + " ";
        }
        if (workSt.isEmpty())
            return;
//полиз циклов бля
        String top = workSt.peek();
        if (top.equals("W")) {
            pointerNumber--;
            output += "M" + pointerNumber + " БП ";
            pointerNumber++;
            output += "M" + pointerNumber + ": ";
            pointerNumber--;
            workSt.pop();
        }
        else if (top.equals("F")) {
            output += "M" + pointerNumber + ": ";
            pointerNumber--;
            workSt.pop();
        }
        else if (isSingleOp(top)) {
            output += top;
            workSt.pop();
        }
    }

// Вытолкнуть для зяпятой
    void popComa() {
        while (!workSt.isEmpty()
                && !workSt.peek().equals("C")
                && !workSt.peek().equals("V")
                && !workSt.peek().equals("I")
                ) {
            output += workSt.pop() + " ";
        }
        if (!workSt.isEmpty()) {
            output += workSt.peek() + " ";
        }
    }

    boolean isSingleOp(String op) {
        for (int i = 0; i < singleOp.length; i++) {
            if (op.equalsIgnoreCase(singleOp[i] + ""))
                return true;
        }
        return false;
    }

// Основной метод - ввод: выражения, вывод: полиз
    private String getPoliz(String _input) {
        input = _input.substring(_input.indexOf(".")+1);
        System.out.println("Исходная строка:");
        System.out.println(input);
        System.out.println("Результат:");
        if (!input.endsWith(".")) {
            input += ".";
        }
        workSt = new Stack<String>(); // Рабочий стек
        output = "";
        while (i < input.length()) {
            String c = input.charAt(i++) + "";

            if ( (c.equals("=") && input.charAt(i) == '=')
                    || c.equals("<") || c.equals(">")) {
                c += input.charAt(i++);
            }
            if (c.equals("$")) {
                continue;
            }
            if (c.equals(".")) {
                popSemicolum();
                return output;
            }
// Пробел - признак начала имени переменной
            if (c.equals(" ")) {
                nameToOut();
                continue;
            }
// Особенности обработки скобок
            if (c.equals(")")) {
                popUntil("(");
                workSt.pop();
                continue;
            }
// Особенности обработки скобок
            if (c.equals("E")) {
                popUntil("B");
                workSt.pop();
                continue;
            }
// Особенности обработки точки с запятой
            if (c.equals(";")) {
                popSemicolum();
                continue;
            }
// Особенности обработки запятой
            if (c.equals(",")) {
                popComa();
                continue;
            }
            if (c.equals("W")) {
                _while();
                continue;
            } else if (c.equals("{")) {
                _do();
                continue;
            } else if (c.equals("}")) {
                _end();
                continue;
            }
            int priority = getPriority(c);
// Если приоритет входного знака равен нулю или больше приоритета знака,
// находящегося в вершине стека, то новый знак добавляется к вершине стека
            if (priority == 0 || (!workSt.isEmpty() && priority > getPriority(workSt.peek()))) {
                workSt.push(c);
                continue;
            }
// В противном случае из стека выталкивается и
// переписывается в выходную строку знак, находящийся в вершине, а также
// следующие за ним знаки с приоритетами большими или равными приоритету
// входного знака. После этого входной знак добавляется к вершине стека.
            else {
                while (!workSt.isEmpty() && getPriority(workSt.peek()) >= priority) {
                    output += workSt.pop()+" ";
                }
                workSt.push(c);
                continue;
            }
// Так говорил Лебедев.
        }
        return "";
    }

    void _while() {
        pointerNumber++;
        output += " M" + pointerNumber + " ";
        workSt.push("УЛП");
    }

    void _do() {
        popUntil("УЛП");
        output +=workSt.peek()+" ";
//pointerNumber--;
    }

    void _end(){
        popUntil("УЛП");
        workSt.pop();
        output +="W1 БП";
    }

    void _if() {
        workSt.push("F");
    }

    void _then() {
        pointerNumber++;
        output += "M" + pointerNumber + " УПЛ ";
        popUntil("F");
    }
}