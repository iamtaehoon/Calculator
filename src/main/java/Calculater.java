import java.util.Iterator;
import java.util.LinkedList;

public class Calculater {

    public Boolean isPowerOn = false;
    public int currentValue = 0;


    public void pressPowerBtn() { //켜져있었으면 끄고 꺼져있었으면 켜라. 계산기 사용전 / 사용후
        isPowerOn = !isPowerOn;
    }

    public Integer inputFormula(String formula) { // 연산식을 계산기에 넣어라.
        if (!isPowerOn) {
            System.out.println("전원이 켜지지 않았습니다");
            return null;
        }
        //연산식이 이상한 경우 여기서 null 출력 "양식이 잘못되었습니다."

        // 정상로직
        System.out.println("입력한 formula = " + formula);
        formula = checkUsingCurrentValue(formula);

        return calculate(formula);

    }

    private String checkUsingCurrentValue(String formula) {
        char firstChar = formula.charAt(0);
        if ((firstChar == '+') || (firstChar == '-') || (firstChar == '*') || (firstChar == '/')) {
            formula = String.valueOf(currentValue) + formula;
        }
        // 이거 로직 마지막 수 처리하는거 만들고 나서 없애줘야함.

        // 만약 = 이 있으면 잘못된 양식이라고 반환.
        formula += "=";

        return formula;
    }

    private int calculate(String formula) {
        LinkedList<String> numberAry = new LinkedList<>();
        LinkedList<Character> symbolAry = new LinkedList<>(); // linked일 필요 없음.
        int start= 0;

        changeFormulaToQueue(formula, numberAry, symbolAry, start);

        // numberAry, symbolAry 잘 들어왔나 검증.
        System.out.print("numberAry: ");
        for (String num : numberAry) {
            System.out.print(num + ", ");
        }
        System.out.println("");

        System.out.print("charAry: ");
        for (Character character : symbolAry) {
            System.out.print(character+", ");
        }
        System.out.println("");


        for (int i = 0; i<symbolAry.size();i++) {

            Character symbol = symbolAry.get(i);

            if ((symbol == '*') || (symbol == '/')) {
                symbolAry.remove(i);

                System.out.println("numberAry = \n"+numberAry);
                String first = numberAry.remove(i);
                String second = numberAry.remove(i);
                System.out.println("i: "+i+"\nfirst = " + first +"\nsecond = "+ second);



                int first_parse_int;
                int second_parse_int;

                if (!first.matches("[+-]?\\d*(\\.\\d+)?")) { //숫자가 아니라면 계산 다시해줘야지
                    first_parse_int = calculate(first+'=');
                } else {
                    first_parse_int = Integer.parseInt(first);
                }
                if (!second.matches("[+-]?\\d*(\\.\\d+)?")) {
                    second_parse_int = calculate(second+'=');
                } else {
                    second_parse_int = Integer.parseInt(second);
                }

                if (symbol == '*') {
                    numberAry.add(i, String.valueOf(first_parse_int * second_parse_int));
                }
                if (symbol == '/') {
                    numberAry.add(i, String.valueOf(first_parse_int / second_parse_int));
                }

                i -= 1;


            }

        }
        System.out.println("곱셈 끝 symbolAry: "+symbolAry);
        System.out.println("곱셈 끝 numberAry = " + numberAry);

        while (symbolAry.size() > 0) {
            Character symbol = symbolAry.pop();
            String first = numberAry.pop();
            String second = numberAry.pop();

            System.out.println("first, symbol, second"+first+symbol+second);
            int first_parse_int;
            int second_parse_int;

            if (!first.matches("[+-]?\\d*(\\.\\d+)?")) {
                first_parse_int = calculate(first+'=');
            } else {
                first_parse_int = Integer.parseInt(first);
            }
            if (!second.matches("[+-]?\\d*(\\.\\d+)?")) {
                second_parse_int = calculate(second+'=');
            } else {
                second_parse_int = Integer.parseInt(second);
            }

            if (symbol == '+') {
                numberAry.add(0, String.valueOf(first_parse_int + second_parse_int));
            }
            if (symbol == '-') {
                numberAry.add(0, String.valueOf(first_parse_int - second_parse_int));
            }
        }

        System.out.println("덧셈 끝 symbolAry: "+symbolAry);
        System.out.println("덧셈 끝 numberAry = " + numberAry);



        // 수식을 우선순위 큐로 만든다.
        // * /, 그중에서 가장 앞에있는 순서대로 우선순위를 둔다
        // 우선순위에 따라 꺼내진 수식 앞뒤에 있는 숫자를 꺼낸다.
        // 숫자가 숫자로 변환가능하면 바로 변환해주고
        // 문자가 섞여있으면 caclulate 해준다.


        if (!numberAry.get(0).matches("[+-]?\\d*(\\.\\d+)?")) {
            System.out.println("들어옴!!");
            return calculate(numberAry.get(0)+'=');
        }
        return Integer.parseInt(numberAry.get(0));

    }

    private void changeFormulaToQueue(String formula, LinkedList<String> numberAry, LinkedList<Character> symbolAry, int start) {
        for (int i = 0; i < formula.length(); i++) {
            if (formula.charAt(i) == '(') { //뭉탱이로 묶어주기.
                int bundleCount = 1;
                while (bundleCount > 0) {
                    i += 1;
                    if (formula.charAt(i) == '(') {
                        bundleCount += 1;
                        continue;
                    }
                    if (formula.charAt(i) == ')') {
                        bundleCount -= 1;
                        continue;
                    }
                }

                //여기서 따로 로직을 돌려 () 묶음을 만들기 -> (43-3*5) => 43-3*5 로
                //짝이 맞는 ) 를 찾아야 함. (()+2)
                //i값 역시 조작해주기


                continue;
            }
            if ((formula.charAt(i) == '+') || (formula.charAt(i) == '-') || (formula.charAt(i) == '*') || (formula.charAt(i) == '/') || (formula.charAt(i) == '=')) {
                // 앞에 뭉탱이로 묶어서 숫자 큐에 넣기.
                // 해당 문자 역시 symbol큐에 넣기.

                // 인덱스 start<=<i 는 숫자 큐에 넣기.
                if ( (formula.charAt(start) == '(') && (formula.charAt(i-1) == ')') ) {
//                    System.out.println("숫자 큐에 넣을 거.");
//                    System.out.println(formula.substring(start+1,i-1));
                    numberAry.add(formula.substring(start + 1, i - 1));
                }
                else {
//                    System.out.println("숫자 큐에 넣을 거.");
//                    System.out.println(formula.substring(start, i));
                    numberAry.add(formula.substring(start, i));
                }

                if (formula.charAt(i) == '=') {
                    break; //식이 끝났다.
                }
                // i는 심볼큐에 넣기.
//                System.out.println("문자 큐에 넣을 거");
//                System.out.println(formula.charAt(i));
                symbolAry.add(formula.charAt(i));
                start = i + 1;
            }

        }
    }

}








// 사용하지 않는 로직.
//        numberAry = formula.split("\\+|-|\\*|/"); // +,-,*,/ 를 기준으로 나눠준다.
//        for (String s : numberAry) {
//            System.out.println("나눠진 숫자 문자열 = " + s);
//        }



//        while (symbolAry.size() != 0) {
//            Character symbol = symbolAry.pop();
//
//            String first = numberAry.get(0);// get이 아니라 pop 해야함.
//            String second = numberAry.get(1);
//
//            if (first.contains("\\+|-|\\*|/|\\(|\\)")) {
//                int first_parse_int = calculate(first);
//            } else {
//                int first_parse_int = Integer.parseInt(first);
//            }
//            if (second.contains("\\+|-|\\*|/|\\(|\\)")) {
//                int second_parse_int = calculate(second);
//            } else {
//                int second_parse_int = Integer.parseInt(second);
//            }
//        }