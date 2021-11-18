package tools;

import storages.Fraction;

/**
 * 进行分数运算
 */
public class FractionOperation {
    private static int minNaturalNumber = 0;
    private static int maxNaturalNumber = 100;
    private static int minNumerator = 0;
    private static int maxNumerator = 100;
    private static boolean isFraction = false;

    public static void setMinNaturalNumber(int minNaturalNumber) {
        FractionOperation.minNaturalNumber = minNaturalNumber;
    }
    public static void setMaxNaturalNumber(int maxNaturalNumber) {
        FractionOperation.maxNaturalNumber = maxNaturalNumber;
    }
    public static void setIsFraction(boolean isFraction) {
        FractionOperation.isFraction = isFraction;
    }

    /**
     * 两个分数相加
     */
    public static Fraction add(Fraction fraction1, Fraction fraction2) {
        Fraction fraction = new Fraction();
        if (isIllegal(fraction1) || isIllegal(fraction2)) {
            fraction.setDenominator(0);
            return fraction;
        }
        toCommonDenominator(fraction1, fraction2);
        fraction.setDenominator(fraction1.getDenominator());
        fraction.setNumerator(fraction1.getNumerator() + fraction2.getNumerator());
        reduction(fraction);
        if (isIllegal(fraction)) {
            fraction.setDenominator(0);
        }
        return fraction;
    }

    /**
     * 两个分数相减
     */
    public static Fraction subtract(Fraction fraction1, Fraction fraction2) {
        Fraction fraction = new Fraction();
        if (isIllegal(fraction1) || isIllegal(fraction2)) {
            fraction.setDenominator(0);
            return fraction;
        }
        toCommonDenominator(fraction1, fraction2);
        fraction.setDenominator(fraction1.getDenominator());
        fraction.setNumerator(fraction1.getNumerator() - fraction2.getNumerator());
        reduction(fraction);
        if (isIllegal(fraction)) {
            fraction.setDenominator(0);
        }
        return fraction;
    }

    /**
     * 两个分数相乘
     */
    public static Fraction multiply(Fraction fraction1, Fraction fraction2) {
        Fraction fraction = new Fraction();
        if (isIllegal(fraction1) || isIllegal(fraction2)) {
            fraction.setDenominator(0);
            return fraction;
        }
        toImproperFraction(fraction1);
        toImproperFraction(fraction2);
        fraction.setNumerator(fraction1.getNumerator() * fraction2.getNumerator());
        fraction.setDenominator(fraction1.getDenominator() * fraction2.getDenominator());
        reduction(fraction);
        if (isIllegal(fraction)) {
            fraction.setDenominator(0);
        }
        return fraction;
    }

    /**
     * 两个分数相除
     */
    public static Fraction divide(Fraction fraction1, Fraction fraction2) {
        Fraction fraction = new Fraction();
        if (isIllegal(fraction1) || isIllegal(fraction2)) {
            fraction.setDenominator(0);
            return fraction;
        }
        if (fraction2.getNumerator() == 0) { // 除数为0
            fraction.setDenominator(0);
            return fraction;
        }
        toImproperFraction(fraction1);
        toImproperFraction(fraction2);
        fraction.setNumerator(fraction1.getNumerator() * fraction2.getDenominator());
        fraction.setDenominator(fraction1.getDenominator() * fraction2.getNumerator());
        reduction(fraction);
        if (isIllegal(fraction)) {
            fraction.setDenominator(0);
        }
        return fraction;
    }

    /**
     * 将分数变为假分数
     */
    private static void toImproperFraction(Fraction fraction) {
        int numerator;
        if (fraction.getNaturalNumber() < 0) { // 分数是负数
            numerator = fraction.getNaturalNumber() * fraction.getDenominator() -
                    fraction.getNumerator();
        } else {
            numerator = fraction.getNaturalNumber() * fraction.getDenominator() +
                    fraction.getNumerator();
        }
        fraction.setNumerator(numerator);
        fraction.setNaturalNumber(0);
    }

    /**
     * 两个分数通分
     */
    private static void toCommonDenominator(Fraction fraction1, Fraction fraction2) {
        // fraction1 和 fraction2 分母的最小公倍数
        int lcm = getLcm(fraction1.getDenominator(), fraction2.getDenominator());
        int numerator1; // 分数1通分后的分子
        if (fraction1.getNaturalNumber() < 0) {
            numerator1 = -1 * lcm / fraction1.getDenominator() * fraction1.getNumerator() +
                    lcm * fraction1.getNaturalNumber();
        } else {
            numerator1 = lcm / fraction1.getDenominator() * fraction1.getNumerator() +
                    lcm * fraction1.getNaturalNumber();
        }
        fraction1.setNumerator(numerator1);
        int numerator2; // 分数2通分后的分子
        if (fraction2.getNaturalNumber() < 0) {
            numerator2 = -1 * lcm / fraction2.getDenominator() * fraction2.getNumerator() +
                    lcm * fraction2.getNaturalNumber();
        } else {
            numerator2 = lcm / fraction2.getDenominator() * fraction2.getNumerator() +
                    lcm * fraction2.getNaturalNumber();
        }
        fraction2.setNumerator(numerator2);
        fraction1.setDenominator(lcm);
        fraction2.setDenominator(lcm);
        fraction1.setNaturalNumber(0);
        fraction2.setNaturalNumber(0);
    }

    /**
     * 分数约分
     */
    public static void reduction(Fraction fraction) {
        if (fraction.getNumerator() == 0) { // 分子为0
            fraction.setDenominator(1);
            return;
        }
        int gcd; // 分子分母的最大公约数
        if (fraction.getNumerator() < 0 && fraction.getDenominator() < 0) { // 分子分母小于0
            gcd = getGcd(-fraction.getNumerator(), -fraction.getDenominator());
        } else if (fraction.getNumerator() < 0) { // 分子小于0
            gcd = getGcd(-fraction.getNumerator(), fraction.getDenominator());
        } else if (fraction.getDenominator() < 0){ // 分母小于0
            gcd = getGcd(fraction.getNumerator(), -fraction.getDenominator());
        } else {
            gcd = getGcd(fraction.getNumerator(), fraction.getDenominator());
        }

        // 分子分母同时除以最大公约数
        fraction.setNumerator(fraction.getNumerator() / gcd);
        fraction.setDenominator(fraction.getDenominator() / gcd);

        if (fraction.getDenominator() < 0) { // 分母如果小于0, 则约分后要变为正数
            fraction.setDenominator(fraction.getDenominator() * -1);
            fraction.setNumerator(fraction.getNumerator() * -1);
        }

        if (fraction.getDenominator() == 1) {
            fraction.setNaturalNumber(fraction.getNumerator());
            fraction.setNumerator(0);

        }
    }

    /**
     * 求两个数最大公约数
     */
    private static int getGcd(int num1, int num2) {
        int gcd;

        // 更相减损法求最大公约数
        int temp;
        while (true) {
            if (num1 == num2) {
                gcd = num1;
                break;
            }
            if (num1 < num2) {
                temp = num2;
                num2 = num1;
                num1 = temp;
            }
            num1 -= num2;
        }

        return gcd;
    }

    /**
     * 求两个数最小公倍数
     */
    private static int getLcm(int num1, int num2) {
        // 最大公约数辅助法求最小公倍数
        return num1 * num2 / getGcd(num1, num2);
    }

    /**
     * 判断结果是否合法
     * 如果不合法, 则将分母变为0, 并返回false
     * 如果合法, 则进行化简约分, 并返回true
     */
    private static boolean isIllegal(Fraction fraction) {
        if (fraction.getDenominator() == 0) { // 分母为0
            return true;
        } else if (isFraction) {
            return  (minNaturalNumber < 0 ?
                    fraction.getNaturalNumber() <= minNaturalNumber :
                    fraction.getNaturalNumber() < minNaturalNumber) ||
                    fraction.getNaturalNumber() > maxNaturalNumber ||
                    fraction.getNumerator() < minNumerator ||
                    fraction.getNumerator() > maxNumerator ||
                    fraction.getDenominator() > 100;
        } else {
            return (minNaturalNumber < 0 ?
                    fraction.getNaturalNumber() <= minNaturalNumber :
                    fraction.getNaturalNumber() < minNaturalNumber) ||
                    fraction.getNaturalNumber() > maxNaturalNumber ||
                    fraction.getNumerator() != 0;
        }
    }
}
