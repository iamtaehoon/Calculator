import java.util.Iterator;
import java.util.LinkedList;

public class Calculater {

    private Boolean isPowerOn = false;
    private int currentValue = 0;
    private String standardOfNumbers = "[+-]?\\d*(\\.\\d+)?";

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

    //핵심 비즈니스 로직 - 재귀로 만듬.//
    private int calculate(String formula) {
        formula += "="; // 연산의 마무리를 알리기 위함.

        LinkedList<String> numberAry = new LinkedList<>();
        LinkedList<Character> symbolAry = new LinkedList<>();
        int start= 0;

        changeFormulaToQueue(formula, numberAry, symbolAry, start);

        //곱셈, 나눗셈 먼저 순서대로 진행
        caculateMultipleAndDivide(numberAry, symbolAry);
        calculatePlusAndMinus(numberAry, symbolAry);
        
        return calculateLastValue(numberAry);

    }

    private int calculateLastValue(LinkedList<String> numberAry) {
        if (!numberAry.get(0).matches(standardOfNumbers)) {
            return calculate(numberAry.get(0));
        }
        return Integer.parseInt(numberAry.get(0));
    }

    private void calculatePlusAndMinus(LinkedList<String> numberAry, LinkedList<Character> symbolAry) {

        while (symbolAry.size() > 0) {
            Character symbol = symbolAry.pop();

            int first = makeIntFromString(numberAry.pop());
            int second = makeIntFromString(numberAry.pop());
            String result = calculateFirstAndSecond(first, symbol, second);
            numberAry.add(0, result);
        }
    }

    private int makeIntFromString(String item) {
        if (item.matches(standardOfNumbers)) {
            return Integer.parseInt(item);
        }
        return calculate(item);
    }

    private void caculateMultipleAndDivide(LinkedList<String> numberAry, LinkedList<Character> symbolAry) {
        for (int i = 0; i< symbolAry.size(); i++) {

            Character symbol = symbolAry.get(i);

            if (symbolIsMultipleOrDivide(symbol)) {

                symbolAry.remove(i);
                int first = makeIntFromString(numberAry.remove(i));
                int second = makeIntFromString(numberAry.remove(i));
                String result = calculateFirstAndSecond(first, symbol, second);
                numberAry.add(i, result);

                i -= 1;
            }

        }
//        System.out.println("곱셈 끝 symbolAry: "+symbolAry);
//        System.out.println("곱셈 끝 numberAry = " + numberAry);
    }

    private String calculateFirstAndSecond(int first, Character symbol, int second) {
        if (symbol == '+') {
            return String.valueOf(first+second);
        }
        if (symbol == '-') {
            return String.valueOf(first-second);
        }
        if (symbol == '*') {
            return String.valueOf(first*second);
        }
        if (symbol == '/') {
            return String.valueOf(first/second);
        }
        throw new IllegalArgumentException("연산에 이상한 기호가 들어갔습니다.");
    }

    private boolean symbolIsMultipleOrDivide(Character symbol) {
        return (symbol == '*') || (symbol == '/');
    }


    private void changeFormulaToQueue(String formula, LinkedList<String> numberAry, LinkedList<Character> symbolAry, int start) {

        //짝이 맞는 ) 를 찾아야 함. (()+2)
        //i값 역시 조작해주기
        for (int i = 0; i < formula.length(); i++) {
            char c = formula.charAt(i);


            if (c == '(') { //뭉탱이로 묶어주기.
                i = findLastBracketIndex(i,formula);
                System.out.println("(의 끝 인덱스 = "+i);
                continue;
            }
            if ((c == '+') || (c == '-') || (c == '*') || (c == '/') || (c == '=')) {

                numberAry.add(makeItemFromFormula(start, i, formula));

                if (formula.charAt(i) == '=') {
                    break; //식이 끝났다.
                }

                symbolAry.add(formula.charAt(i));
                start = i + 1;
            }

        }
    }

    private String makeItemFromFormula(int start, int i, String formula) {
        //()로 묶여있는 경우
        if ( (formula.charAt(start) == '(') && (formula.charAt(i-1) == ')') ) {
            return formula.substring(start + 1, i - 1);
        }

        //()로 묶여있지 않은 경우.
        return formula.substring(start, i);

    }

    private int findLastBracketIndex(int i,String formula) {
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

        return i;
    }


    private String checkUsingCurrentValue(String formula) {
        char firstChar = formula.charAt(0);
        if ((firstChar == '+') || (firstChar == '-') || (firstChar == '*') || (firstChar == '/')) {
            formula = Integer.toString(currentValue) + formula;
        }
        // 이거 로직 마지막 수 처리하는거 만들고 나서 없애줘야함.

        // 만약 = 이 있으면 잘못된 양식이라고 반환.

        return formula;
    }


}






// 사용하지 않는 로직.
//        numberAry = formula.split("\\+|-|\\*|/"); // +,-,*,/ 를 기준으로 나눠준다.
//        for (String s : numberAry) {
//            System.out.println("나눠진 숫자 문자열 = " + s);
//        }

