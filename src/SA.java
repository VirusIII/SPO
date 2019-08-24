import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class SA {
    static String input; // Входные данные
    static int i; // Указатель на текущий символ

    public SA(String input) {
        this.input = input;
    }

    public static boolean analyze(String _input) {
        input = _input;
        input = input.replaceAll(" [0-9]+ ", "C");
        input = input.replaceAll(" [A-Z]+ ", "i");
        return S();
    }

    // Названия нетерминалов сохранены как в грамматике
    static boolean S() {
        программа();
        if (input.charAt(i) == '$') {
            return true;
        }
        System.out.println("\nВстречен преждевременный обрыв файла");
        return false;
    }

    static void программа() {
        if (input.charAt(i) == '{') {
            i++;
            System.out.print("1, ");
            описания();
            if (input.charAt(i) == '.') {
                i++;
                операторы();
            } else {
                error(";");
                i++;
            }

            if (input.charAt(i) != '}')
                error("}");
            i++;
        } else
            error("{");
    }

    static void описания() {
        System.out.print("2, ");
        описание();
        A();
    }

    static void A() {
        if (input.charAt(i) == ';') {
            i++;
            System.out.print("3, ");
            описания();
        } else {
            System.out.print("4, ");
        }
    }

    static void операторы() {
        System.out.print("5, ");
        оператор();
        B();
    }

    static void B() {
        if (input.charAt(i) == ';') {
            i++;
            System.out.print("6, ");
            операторы();
        } else {
            System.out.print("7, ");
        }
    }

    static void описание() {
        if (input.charAt(i) == 'n') {
            i++;
            System.out.print("8, ");
            идентификаторы();
        } else {
            error("n");
            i++;
        }
    }

    static void идентификаторы() {
        if (input.charAt(i) == 'i') {
            i++;
            System.out.print("9, ");
            C();
        } else {
            error("i");
            i++;
        }
    }

    static void C() {
        if (input.charAt(i) == ',') {
            i++;
            System.out.print("10, ");
            идентификаторы();
        } else {
            System.out.print("11, ");
        }
    }

    static void оператор() {
        if (input.charAt(i) == 'i') {
            i++;
            if (input.charAt(i) == '=') {
                i++;
                System.out.print("12, ");
                выражение();
            } else {
                error("=");
                i++;
            }
        } else if (input.charAt(i) == 'w') {
            i++;
            System.out.print("13, ");
            выражение();
            if (input.charAt(i) == '{') {
                i++;
                операторы();
            } else {
                error("{");
                i++;
            }
            if (input.charAt(i) != '}') {
                error("{");
            }
            i++;
        } else if (input.charAt(i) == 'r') {
            i++;
            System.out.print("14, ");
            if (input.charAt(i) != 'i') {
                error("i");
                i++;
            }
        } else if (input.charAt(i) == 'o') {
            i++;
            System.out.print("15, ");
            выражение();
        } else {
            error("i/w/r/o");
            i++;
        }
    }

    static void выражение() {
        System.out.print("16, ");
        фактор();
        G();
    }

    static void G() {
        if (input.charAt(i) == '+') {
            i++;
            System.out.print("17, ");
            выражение();
        } else if (input.charAt(i) == '-') {
            i++;
            System.out.print("18, ");
            выражение();
        } else System.out.print("19, ");
    }

    static void фактор() {
        System.out.print("20, ");
        первичное();
        E();
    }

    static void E() {
        if (input.charAt(i) == '*') {
            i++;
            System.out.print("21, ");
            фактор();
        } else if (input.charAt(i) == 'd') {
            i++;
            System.out.print("22, ");
            фактор();
        } else if (input.charAt(i) == 'm') {
            i++;
            System.out.print("23, ");
            фактор();
        } else {
            System.out.print("24, ");
        }
    }

    static void первичное() {
        if (input.charAt(i) == 'i') {
            i++;
            System.out.print("25, ");
        } else if (input.charAt(i) == 'C') {
            i++;
            System.out.print("26, ");
        } else if (input.charAt(i) == '-') {
            i++;
            System.out.print("26, ");
            if (input.charAt(i) != 'C') {
                error("C");
            }
            i++;
        } else if (input.charAt(i) == '(') {
            i++;
            System.out.print("27, ");
            F();
            if (!(input.charAt(i) == ')'))
                error("C");
            i++;
        }
    }

    static void F() {
        if (input.charAt(i) == 'C') {
            i++;
            System.out.print("29, ");
        } else {
            System.out.print("28, ");
            сравнение();
        }
    }

    static void сравнение() {
        System.out.print("30, ");
        выражение();
        J();
    }

    static void J() {
        if (input.charAt(i) == ':') {
            i++;
            System.out.print("31, ");
            выражение();
            J();
        } else if (input.charAt(i) == '!') {
            i++;
            System.out.print("32, ");
            выражение();
            J();
        } else
            System.out.print("33, ");
    }




    static void error(String awaited) {
        System.out.println();
        System.out.println("Ошибка. Ожидалось: " + awaited);
        System.out.println("Встреченно: " + input.charAt(i));
        System.out.println("Ошибка возникла в");
        System.out.print(input.substring(0, i));
        System.out.print(" [" + input.charAt(i) + "] ");
        System.out.println(input.substring(i + 1));
        System.out.println("----------------");
    }
}