package storages;

/**
 * 存储分数
 */
public class Fraction {
    private int naturalNumber; // 自然数
    private int numerator;     // 分子
    private int denominator;   // 分母, 如果为0, 则结果错误, 不再进行计算

    public Fraction() {
        naturalNumber = 0;
        numerator = 0;
        denominator = 1;
    }
    public Fraction(int naturalNumber, int numerator, int denominator) {
        this.naturalNumber = naturalNumber;
        this.numerator = numerator;
        this.denominator = denominator;
    }

    public int getNaturalNumber() {
        return naturalNumber;
    }
    public void setNaturalNumber(int naturalNumber) {
        this.naturalNumber = naturalNumber;
    }
    public int getNumerator() {
        return numerator;
    }
    public void setNumerator(int numerator) {
        this.numerator = numerator;
    }
    public int getDenominator() {
        return denominator;
    }
    public void setDenominator(int denominator) {
        this.denominator = denominator;
    }

    @Override
    public String toString() {
        String string;
        if (numerator == 0) { // 自然数
            string = naturalNumber + "";
        } else if (denominator == 1) { // 真分数
            string = numerator + "";
        } else { // 真分数
            string = numerator + "/" + denominator;
        }
        return string;
    }
}
