import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Test {
    public static void main(String[] args) throws InterruptedException {
        Calculater c = new Calculater();
        c.pressPowerBtn(); //전원 킴.


        System.out.println(c.inputFormula("11+3/23*421-6+(32*4)"));
        System.out.println("-------------\n\n");

        System.out.println(c.inputFormula("11+3/23*421-6+32*4"));
        System.out.println("-------------\n\n");

        System.out.println(c.inputFormula("200"));
        System.out.println("-------------\n\n");

        System.out.println(c.inputFormula("(32*4)"));
        System.out.println("-------------\n\n");

        System.out.println(c.inputFormula("(((32*4)))"));
        System.out.println("-------------\n\n");

        System.out.println(c.inputFormula("+4"));
        System.out.println("-------------\n\n");

        c.pressPowerBtn(); //전원 끔 이 밑으로는 해도 안됨.

        System.out.println(c.inputFormula("(((32*4)))"));
        System.out.println("-------------\n\n");

//        Scanner sc = new Scanner(System.in);
//        formula = sc.nextLine();
//        while (true) {
//            System.out.println("연산을 입력하세요.");
//            if (formula.equals("end")) {
//                System.out.println("연산이 종료되었습니다.");
//                break;
//            }
    }
}
