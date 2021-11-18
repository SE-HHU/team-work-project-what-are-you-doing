package tools;

import storages.Fraction;
import storages.Record;

import java.io.*;
import java.util.Date;
import java.util.Stack;

/**
 * 生成四则运算表达式的相关操作
 */
public class CalculateOperation {
    /**
     * 计算四则运算表达式的值
     */
    public static Fraction getResult(String arithmetic) {
        Fraction result; // 表达式的结果
        String postfix = infixToPostfix(arithmetic); // 利用栈，中缀表达式转到后缀表达式
        result = calculatePostfix(postfix); // 计算后缀表达式的值并赋值给result
        return result;
    }

    /**
     * 检查结果并统计
     */
    public static void check() {
        Record.wrongArithmetics.clear();

        // 创建文件输出流
        String recordFileName = "./Record.txt";
        File recordFile = new File(recordFileName);
        FileWriter fileWriter;
        BufferedWriter recordBufferedWriter = null;
        try {
            if (!recordFile.exists()) {
                recordFile.createNewFile();
            }
            fileWriter = new FileWriter(recordFileName, true);
            recordBufferedWriter = new BufferedWriter(fileWriter);
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        //创建文件输入流
        String arithmeticFileName = "./Exercises.txt";
        String resultFileName = "./Results.txt";
        String answerFileName = "./Answers.txt";
        File arithmeticFile = new File(arithmeticFileName);
        File resultFile = new File(resultFileName);
        File answerFile = new File(answerFileName);
        BufferedReader arithmeticBufferedReader;
        BufferedReader resultBufferedReader;
        BufferedReader answerBufferedReader;
        try {
            arithmeticFile.createNewFile();
            resultFile.createNewFile();
            answerFile.createNewFile();
            arithmeticBufferedReader = new BufferedReader(new InputStreamReader(
                    new FileInputStream(arithmeticFile)));
            resultBufferedReader = new BufferedReader(new InputStreamReader(
                    new FileInputStream(resultFile)));
            answerBufferedReader = new BufferedReader(new InputStreamReader(
                    new FileInputStream(answerFile)));
        } catch (IOException e) {
            System.out.println("请检查文件！");
            return;
        }

        // 核对答案
        checkAnswer(arithmeticBufferedReader, resultBufferedReader,
                answerBufferedReader);

        // 将错题记录写入文件中
        int length = Record.wrongArithmetics.size();
        if (length == 0) {
            return;
        }
        try {
            if (recordBufferedWriter != null) {
                recordBufferedWriter.write("时间： " + new Date());
            }
            if (recordBufferedWriter != null) {
                recordBufferedWriter.newLine();
            }

            for (int i = 0; i < length; i++) {
                if (recordBufferedWriter != null) {
                    recordBufferedWriter.write(Record.wrongArithmetics.get(i));
                }
                if (recordBufferedWriter != null) {
                    recordBufferedWriter.newLine();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        // 关闭流
        try {
            arithmeticBufferedReader.close();
            resultBufferedReader.close();
            if (recordBufferedWriter != null) {
                recordBufferedWriter.flush();
            }
            if (recordBufferedWriter != null) {
                recordBufferedWriter.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 核对答案
     */
    private static void checkAnswer(BufferedReader arithmeticBufferedReader,
                                    BufferedReader resultBufferedReader,
                                    BufferedReader answerBufferedReader) {
        String str1;
        String arithmetic;
        String str2;
        String result;
        String str3;
        String answer;
        String[] str;
        int number = 1;
        try {
            while ((str1 = arithmeticBufferedReader.readLine()) != null &&
                    (str2 = resultBufferedReader.readLine()) != null &&
                    (str3 = answerBufferedReader.readLine()) != null) {
                str1 = removeSpaces(str1);
                str = str1.split(". ");
                arithmetic = str[1];
                number = Integer.parseInt(str[0]);
                str2 = removeSpaces(str2);
                result = str2.split(". ")[1];
                str3 = removeSpaces(str3);
                answer = str3.split(". ")[1];

                // 与给定的答案文件进行比较
                if (!result.equals(answer)) {
                    arithmetic = removeSpaces(arithmetic);
                    String record = arithmetic + " " + result;
                    Record.wrongArithmetics.add(record);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (Record.accuracyRate.size() >= 5) {
                Record.accuracyRate.remove(0);
            }
            double d = (double) (number - Record.wrongArithmetics.size()) / number;
            Record.accuracyRate.add(d);
        }
    }

    /**
     * 去除字符串中的空格
     */
    public static String removeSpaces(String string) {
        StringBuilder str = new StringBuilder();
        char temp;
        for (int i = 0; i < string.length(); i++) {
            temp = string.charAt(i);
            if (temp != ' ') {
                if (temp == '.') {
                    str.append(temp).append(" ");
                } else {
                    str.append(temp);
                }
            }
        }
        return str.toString();
    }

    /**
     * 计算后缀表达式的值
     */
    private static Fraction calculatePostfix(String postfixExpression) {
        Fraction result = new Fraction(); // 存储结果
        StringBuilder tempInt; // 遇见数字临时存储
        Stack<Fraction> fractionStack = new Stack<>();
        char temp; // 存储后缀表达式的每个字符
        int isFraction = 0;
        // 1 表示正在对分数的分子部分进行操作
        // 2 表示正在对分数的分母部分进行操作
        Fraction temp1; // 进行运算的临时数字
        Fraction temp2; // 进行运算的临时数字
        for (int i = 0; i < postfixExpression.length(); i++) {
            temp = postfixExpression.charAt(i);
            switch (temp) {
                case '+':
                    temp2 = fractionStack.pop();
                    temp1 = fractionStack.pop();
                    fractionStack.push(FractionOperation.add(temp1, temp2));
                    break;
                case '-':
                    temp2 = fractionStack.pop();
                    temp1 = fractionStack.pop();
                    fractionStack.push(FractionOperation.subtract(temp1, temp2));
                    break;
                case '×':
                    temp2 = fractionStack.pop();
                    temp1 = fractionStack.pop();
                    fractionStack.push(FractionOperation.multiply(temp1, temp2));
                    break;
                case '÷':
                    temp2 = fractionStack.pop();
                    temp1 = fractionStack.pop();
                    fractionStack.push(FractionOperation.divide(temp1, temp2));
                    break;
                default:
                    if (temp < '0' || temp > '9') {
                        break;
                    }
                    tempInt = new StringBuilder();
                    while (temp >= '0' && temp <= '9') {
                        tempInt.append(temp);
                        i++;
                        temp = postfixExpression.charAt(i);
                    }
                    if (temp == '/'){
                        isFraction = 1;
                    }
                    if (isFraction == 0) {
                        result.setNaturalNumber(Integer.parseInt(tempInt.toString()));
                    } else if (isFraction == 1) {
                        isFraction = 2;
                        result.setNumerator(Integer.parseInt(tempInt.toString()));
                    } else {
                        isFraction = 0;
                        result.setDenominator(Integer.parseInt(tempInt.toString()));
                        fractionStack.push(result);
                        result = new Fraction();
                        break;
                    }
                    if (temp == ' '){
                        fractionStack.push(result);
                        result = new Fraction();
                    }
                    break;
            }
        }
        result = fractionStack.pop();
        return result;
    }

    /**
     * 中缀表达式转为后缀表达式
     */
    private static String infixToPostfix(String infixExpression) {
        Stack<String> stack = new Stack<>();
        StringBuilder postfixExpression = new StringBuilder(); //存储后缀表达式
        char temp; // 存储中缀表达式的每个字符
        boolean isFraction = false; //是否遇见分数
        boolean haveNaturalNumber = false;
        for (int i = 0; i < infixExpression.length(); i++) {
            temp = infixExpression.charAt(i);
            switch (temp) {
                case '+':
                case '-':
                    // 当栈不为空以及栈顶元素不是左括号时,栈内元素直接出栈,
                    // 因为栈内此时只有可能是+-×÷四种运算符,
                    // 否则入栈
                    while (!stack.isEmpty() && !stack.peek().equals("(")) {
                        postfixExpression.append(stack.pop()).append(" ");
                    }
                    //入栈
                    stack.push(temp + "");
                    break;
                case '÷':
                case '×':
                    //遇到运算符 × 或 ÷
                    while (!stack.isEmpty() && (stack.peek().equals("×") ||
                            stack.peek().equals("÷"))) {
                        postfixExpression.append(stack.pop()).append(" ");
                    }
                    stack.push(temp + "");
                    break;
                case '(':
                    //遇到左括号
                    if (!isFraction) { //没有遇到分数
                        stack.push(temp + "");
                    }
                    break;
                case ')':
                    String out = stack.pop();
                    while (!stack.isEmpty() && !("(".equals(out))) {
                        postfixExpression.append(out).append(" ");
                        out = stack.pop();
                    }
                    break;
                default:
                    if (temp < '0' || temp > '9') {
                        break;
                    }

                    // 遇到操作数
                    while (temp >= '0' && temp <= '9') {
                        postfixExpression.append(temp);
                        i++;
                        if (i == infixExpression.length()) {
                            break;
                        }
                        temp = infixExpression.charAt(i);
                    }
                    if (temp == '(') {
                        haveNaturalNumber = true;
                        isFraction = true;
                        postfixExpression.append(temp);
                        break;
                    } else if (temp == '/') {
                        isFraction = true;
                        postfixExpression.append(temp);
                        break;
                    } else if (isFraction) {
                        isFraction = false;
                        if (haveNaturalNumber) {
                            haveNaturalNumber = false;
                            postfixExpression.append(temp).append(" ");
                            break;
                        }
                    }
                    postfixExpression.append(" ");
                    i--;
                    break;
            }
        }
        while (!stack.isEmpty()) {
            postfixExpression.append(stack.pop()).append(" ");
        }
        postfixExpression.append("=");
        return postfixExpression.toString();
    }
}
