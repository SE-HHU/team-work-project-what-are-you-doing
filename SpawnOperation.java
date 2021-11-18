package tools;

import storages.Arithmetic;
import storages.Database;
import storages.Fraction;

import java.io.*;
import java.util.LinkedList;

/**
 * 生成四则运算表达式的相关操作
 */
public class SpawnOperation {
    /**
     * 生成四则运算表达式
     */
    public static void spawnArithmetic(int questionNumber,
                                       int maxNaturalNumber, int operatorNumber,
                                       boolean haveBracket, boolean haveFraction,
                                       boolean havePlusSign, boolean haveSubtractionSign,
                                       boolean haveMultipleSign, boolean haveDivisionSign) {
        FractionOperation.setMaxNaturalNumber(maxNaturalNumber);
        FractionOperation.setIsFraction(haveFraction);
        // 循环生成每一道题目
        while (Database.arithmetics.size() < questionNumber) {
            Arithmetic arithmetic = new Arithmetic();
            StringBuilder arithmeticStr = new StringBuilder(); // 运算表达式
            int maxBracketNumber; // 括号最大生成数量
            int bracketNumber = 0; // 已经生成的括号数量

            // 在一个位置重复生成的括号数量或者在表达式最前面生成的左括号数量
            int repeatedBracketNumber = 0;

            maxBracketNumber = operatorNumber - 1; // 根据运算符数量确定括号最大数量

            if (!haveBracket) { // 根据要求确定是否有括号
                maxBracketNumber = 0;
            }

            // 左右括号间的间隔，防止出现 a + (b) + c 的 情况
            LinkedList<Integer> bracketIntervals = new LinkedList<>();

            // 循环生成操作数 + 运算符(不包含最后一个操作数)
            for (int i = 0; i < operatorNumber; i++) {

                // 能够包含在括号内的剩余的运算符数量
                int remainBracketOperator = operatorNumber - i - repeatedBracketNumber;

                // 生成左括号
                for (int j = 1; j <= maxBracketNumber - bracketNumber; j++) {
                    if (j > remainBracketOperator) { // 防止出现 a + b + ((c + d)) 的情况
                        break;
                    }
                    if (((int) (Math.random() * 2)) == 0) {
                        arithmeticStr.append("(");
                        bracketNumber++;
                        // 在表达式最前面生成了左括号或者在一个位置重复生成了左括号
                        int size = bracketIntervals.size();
                        if (i == 0 || (size >  1 && bracketIntervals.get(size - 1) == 1)) {
                            repeatedBracketNumber++;
                        }
                        bracketIntervals.add(1);
                    }
                }

                //生成操作数
                if (haveFraction) {
                    switch ((int) (Math.random() * 2)) {
                        case 0:
                            arithmeticStr.append(spawnOperand(maxNaturalNumber,
                                    true));
                            break;
                        case 1:
                            arithmeticStr.append(spawnOperand(maxNaturalNumber,
                                    false));
                            break;
                    }
                } else {
                    arithmeticStr.append(spawnOperand(maxNaturalNumber,
                            false));
                }


                int remainOperator = operatorNumber - i; // 还没有生成的剩余的运算符数量

                // 生成右括号
                for (int j = bracketIntervals.size() - 1; j >= 0; j--) {
                    Integer temp = bracketIntervals.get(j);
                    if (temp == 1) { // 确保没有出现 a + (b) + c 的 情况
                        break;
                    }

                    // 确保不会出现 (a + b + c + d) 的情况
                    if (repeatedBracketNumber > 0 &&
                            repeatedBracketNumber == remainOperator) {
                        arithmeticStr.append(")");
                        bracketIntervals.remove(temp);
                        // 判断是在表达式最前面的左括号还是重复的左括号与右括号匹配
                        if (temp == i + 1 ||
                                temp.equals(bracketIntervals.get(j - 1))) {
                            repeatedBracketNumber--;
                            break;
                        }
                        continue;
                    }

                    int x;
                    x = (int) (Math.random() * 2);
                    if (x == 0) {
                        arithmeticStr.append(")");
                        bracketIntervals.remove(temp);
                    }
                }

                arithmeticStr.append(spawnOperator(havePlusSign, haveSubtractionSign,
                        haveMultipleSign, haveDivisionSign)); // 生成运算符
                for (int j = 0; j < bracketIntervals.size(); j++) {
                    Integer integer = bracketIntervals.get(j);
                    bracketIntervals.set(j, integer + 1);
                }
            }

            //生成最后一个操作数
            arithmeticStr.append(spawnOperand(maxNaturalNumber,
                    haveFraction));

            // 生成剩余的右括号
            arithmeticStr.append(")".repeat(bracketIntervals.size()));

            bracketIntervals.clear();

            arithmeticStr.append(" ="); // 生成等号

            // 将生成好的表达式放入arithmetic对象中
            arithmetic.setExpression(arithmeticStr.toString());

            //得到答案
            arithmetic.setResult(CalculateOperation.getResult(arithmeticStr.toString()));

            // 如果结果不合法或表达式重复就跳过，否则加入四则运算表达式集合中
            if (arithmetic.getResult().getDenominator() != 0 &&
                    !Database.arithmetics.contains(arithmetic)) {
                Database.arithmetics.add(arithmetic);
            }
        }
    }

    public static void print() {
        //创建文件输出流
        String arithmeticFileName = "./Exercises.txt";
        String resultFileName = "./Results.txt";
        String answerFileName = "./Answers.txt";
        File arithmeticFile = new File(arithmeticFileName);
        File resultFile = new File(resultFileName);
        File answerFile = new File(answerFileName);
        BufferedWriter arithmeticBufferedWriter;
        BufferedWriter resultBufferedWriter;
        BufferedWriter answerBufferedWriter;
        try {
            if (!arithmeticFile.exists()) {
                arithmeticFile.createNewFile();
            }
            if (!resultFile.exists()) {
                resultFile.createNewFile();
            }
            if (!answerFile.exists()) {
                answerFile.createNewFile();
            }
            arithmeticBufferedWriter = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(arithmeticFile)));
            resultBufferedWriter = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(resultFile)));
            answerBufferedWriter = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(answerFile)));
        } catch (IOException e) {
            System.out.println("请检查文件！");
            return;
        }

        for (int i = 0; i < Database.arithmetics.size(); i++) {
            Arithmetic arithmetic = Database.arithmetics.get(i);
            String question = i + 1 + ". " + arithmetic.getExpression();
            String result = i + 1 + ". " + arithmetic.getResult();
            String answer = i + 1 + ". ";
            try {
                arithmeticBufferedWriter.write(question);
                resultBufferedWriter.write(result);
                answerBufferedWriter.write(answer);
                if (i == Database.arithmetics.size() - 1) {
                    break;
                }
                arithmeticBufferedWriter.newLine();
                resultBufferedWriter.newLine();
                answerBufferedWriter.newLine();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        // 关闭流
        try {
            arithmeticBufferedWriter.flush();
            arithmeticBufferedWriter.close();
            resultBufferedWriter.flush();
            resultBufferedWriter.close();
            answerBufferedWriter.flush();
            answerBufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 生成操作数
     */
    private static String spawnOperand(int maxNaturalNumber,
                                       boolean isFraction) {
        Fraction fraction = new Fraction();
        if (isFraction) {
            fraction.setDenominator((int) (Math.random() * 8 + 2));
            fraction.setNumerator((int) (Math.random() * (
                    fraction.getDenominator() - 1) + 1));
        } else {
            // 确保可以随机生成正数和负数
            while (fraction.getNaturalNumber() == 0) {
                fraction.setNaturalNumber((int) (Math.random() * maxNaturalNumber + 1));
            }
        }
        return fraction.toString();
    }

    /**
     * 生成运算符
     */
    private static String spawnOperator(boolean havePlusSign, boolean haveSubtractionSign,
                                        boolean haveMultipleSign, boolean haveDivisionSign) {
        String operator = null;
        while (operator == null) {
            switch ((int) (Math.random() * 4 + 1)) {
                case 1:
                    if (havePlusSign) {
                        operator = " + ";
                    }
                    break;
                case 2:
                    if (haveSubtractionSign) {
                        operator = " - ";
                    }
                    break;
                case 3:
                    if (haveMultipleSign) {
                        operator = " × ";
                    }
                    break;
                case 4:
                    if (haveDivisionSign) {
                        operator = " ÷ ";
                    }
                    break;
            }
        }
        return operator;
    }
}