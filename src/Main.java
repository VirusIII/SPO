import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        Scanner scan = new Scanner(new File("input.txt"));
        String input="";
        try {
            input = scan.useDelimiter("\\A").next();
        } catch (Exception e){
            System.out.println("Ошибка, использованы кириллические символы");
        }
        scan.close();
// Игнорируем регистр
        input = input.toUpperCase();
        input = input.replaceAll("[\r|\n]", " ");
        System.out.println("Лексический анализ");
        String lexemStr = LA.analyze(input);
        if (lexemStr.equals("")) {
            System.out.println("Ошибка на этапе лексического анализа.");
            return;
        }
        System.out.println("Получена кодированная строка лексем:");
        System.out.println(lexemStr);
        System.out.println();
        System.out.println("Синтаксический анализ");
        if (SA.analyze(lexemStr + "$")) {
            System.out.println("\nДопуск");
            String c = Poliz.buildPoliz(lexemStr.substring(0, lexemStr.length()-1));
            System.out.println(c);
            String tt=TT.BuildTriads(c);
            System.out.println("Таблица триад");
            System.out.println(tt);
            System.out.println("Код на языке ASSEMBLER");
            System.out.println(Assembling.GetAsm(tt));
        } else {
            System.out.println("Ошибка на этапе синтаксического анализа.");
        }
    }
}