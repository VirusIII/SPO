import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class LA {
    static ArrayList<String> idTable = new ArrayList<String>(); // Таблица имён
    static ArrayList<String> nTable = new ArrayList<String>(); // Таблица числовых констант
// Таблица лексем
    static final String[] lexemTable = { "BEGIN", "DO","END", ";", ":=", "WHILE", "-", "+", "*", "DIV", "MOD","INT",",","OUTPUT","INPUT","==","<>",".","(",")"};
// Таблица КОДОВ лексем
    static final String[] codedTable = { "{","{", "}", ";", "=", "w", "-", "+", "*","d","m","n",",","o","r",":", "!",".","(",")"};

//Анализ программы
    public static String analyze(String input) {
        boolean gotErrors = false;
        System.out.println("Встреченные лексемы");
        String outStr = "";
        while (input.length() > 0) {
            if (input.startsWith(" ")) {
                input = input.substring(1);
                continue;
            }
            if (input.startsWith(":=")) {
                System.out.println("L4\t:=\t=");
                outStr += '=';
                input = input.substring(2);
                continue;
            }
            int wordInd = input.indexOf(' '); // конец слова
// текущее слово
            String currentWord = wordInd > -1 ? input.substring(0, wordInd) : input;

// Поиск лексемы в таблице
            boolean found = false;
            for (int i = 0; i < lexemTable.length; i++) {
                if (lexemTable[i].equals(currentWord) ||
                        lexemTable[i].equals(input.charAt(0) + "")) {
                    System.out.print("L" + (i+1));
                    System.out.print("\t" + lexemTable[i]);
                    System.out.println("\t" + codedTable[i]);
                    outStr += codedTable[i];
                    input = input.substring(lexemTable[i].length());
                    found = true;
                    break;
                }
            }
            if (found)
                continue;
// Если встретили число любой длины
            Matcher m = Pattern.compile("^(\\d+)(.*)").matcher(input);
            if (m.find()) {
// Извленкаем это число из строки
                String number = m.group(1);
// Добавляем в таблицу констант
                addToTable(nTable, number);
                System.out.print("C-");
                System.out.print(nTable.indexOf(number));
                System.out.println("\t" + number);
// И удаляем его из входной строки
                input = m.group(2);
                outStr += " " + number + " ";
                continue;
            }
// Если встретили имя переменной
            m = Pattern.compile("^(\\w+)(.*)").matcher(input);
            if (m.find()) {
// Извленкаем это имя из строки
                String var = m.group(1);
// Добавляем в таблицу переменных
                addToTable(idTable, var);
                System.out.print("I-");
                System.out.print(idTable.indexOf(var));
                System.out.println("\t" + var);
// И удаляем её из входной строки
                input = m.group(2);
                outStr += " " + var + " ";
                continue;
            }
            System.out.println("Ошибка в лексеме " + currentWord);
            gotErrors = true;
            input = input.substring(currentWord.length());
        }
        if (!gotErrors)
            return outStr;
        else
            return "";
    }

    static void addToTable(ArrayList<String> table, String item) {
        if (!table.contains(item)) {
            table.add(item);
        }
    }
}