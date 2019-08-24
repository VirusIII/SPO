import java.util.regex.Pattern;

public class Assembling {
    static String asm = "";
    static String[] tab;
    static String[] str;
    static String ja = "";

    public static String GetAsm(String table) {
        tab = table.split("\n");
        String regexp = "T[0-9]{1,}";
        String t = " ";
        boolean fl = false;
        for (int i = 0; i < tab.length; i++) {
            str = tab[i].split(" ");
            if (str.length == 4) {
                if ((Pattern.matches(regexp, str[3]) && Pattern.matches(regexp, str[2])) & !str[1].equals(":") & !str[1].equals("!")) {
                    t += str[3] + " ";
                }
            }
        }
        for (int i = 0; i < tab.length; i++) {
            str = tab[i].split(" ");
            if (str[1].equals("!") | str[1].equals(":")) {
                cons(str);
                if (!fl) {
                    fl = true;
                    int j = i;
                    String[] str1 = str;
                    while ((j < tab.length) && !Pattern.matches("M[0-9]{1,}", str1[3])) {
                        j++;
                        str1 = tab[j].split(" ");
                    }
                    if (Pattern.matches("M[0-9]{1,}", str1[3])) {
                        asm += str1[3].replace("M", "W") + ":\n";
                        ja = str1[3];
                    }
                }
            }
            boolean op1 = false;
            if (str.length == 4) op1 = Pattern.matches(regexp, str[3]);
            boolean op2 = Pattern.matches(regexp, str[2]);
            if (str[1].equals("УЛП")) {
                fl = false;
                continue;
            } else if (str[1].equals("БП")) {
                asm += "JMP " + str[2] + "\n" + str[2].replace("W", "M") + ":";
                continue;
            } else if ((!op1) & (!op2)) {
                asm += "MOV AX, " + str[3] + "\n" + act(str[1], str[2]);
            } else if ((!op1) && (op2)) {
                if (str[1].equals("D") || str[1].equals("M") || str[1].equals("-")) {
                    asm += "MOV DX, AX\nMOV AX, " + str[3] + "\n" + act(str[1], "DX");
                } else {
                    asm += act(str[1], str[3]);
                }
            } else if ((op1) & (!op2)) {
                asm += act(str[1], str[2]);
            } else {
                asm += "MOV DX, AX\nPOP AX\n" + act(str[1], "DX");
            }
            if (t.contains(" " + str[0] + " ")) {
                asm += "PUSH AX\n";

            }

        }

        return asm;
    }

    static String act(String act, String op2) {
        //{"*", "D", "M", "+", "-", ":", "!", "=", "O", "R"}
        String str = "";
        if (act.equals("*")) {
            if (Pattern.matches("[A-Z]{1,}", op2)) {
                str = "MUL " + op2 + "\n";
            } else {
                str += "MOV BL, " + op2 + "\nMUL BL\n";
            }
        } else if (act.equals("D")) {
            if (Pattern.matches("[A-Z]{1,}", op2)) {
                str += "DIV" + op2 + "\n";
            } else {
                str += "MOV bl, " + op2 + "\nDIV BL\n";
            }
        } else if (act.equals("M")) {
            str = "MOV CX,AX\n ";
            if (Pattern.matches("[A-Z]{1,}", op2)) {
                str += "DIV" + op2 + "\n";
            } else {
                str += "MOV BL, " + op2 + "\nDIV BL\n";
            }
            str += "MUL " + op2 + "\nMOV BX, AX\nMOV AX, CXSUB AX, BX\n";
            str += "MUL " + op2 + "\nMOV BX, AX\nMOV AX, CXSUB AX, BX\n";
        } else if (act.equals("+")) {
            str = "ADD AX, " + op2 + "\n";
        } else if (act.equals("-")) {
            str = "SUB AX, " + op2 + "\n";
        } else if (act.equals(":")) {
            str = "CMP AX, " + op2 + "\nJNE " + ja + "\n";
        } else if (act.equals("!")) {
            str = "CMP AX, " + op2 + "\nJE " + ja + "\n";
        } else if (act.equals("=")) {
            str = "MOV " + op2 + ", AX\n";
        } else if (act.equals("R")) {
            str = "INPUT " + op2 + "\n";
        } else if (act.equals("O")) {
            str = "PUSH AX\nMOV " + op2 + ", AX\nMOV DL, AH\nMOV AH, 2\nINT 21H\nMOV DL, AL\nINT 21H\n";
        }

        return str;
    }

    static String cons(String[] str) {
        if ((str[1].equals("!") || str[1].equals(":")) & (Pattern.matches("T[0-9]{1,}", str[2]) || Pattern.matches("T[0-9]{1,}", str[3]))) {
            str[3] = cons(tab[Integer.parseInt(str[3].substring(1))].split(" "));
        }
        return str[2];
    }
}
