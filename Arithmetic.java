package storages;

/**
 * 四则运算表达式数据类型
 */
public class Arithmetic {
    private String expression = ""; // 四则运算表达式
    private Fraction result;   // 四则运算表达式的结果

    public String getExpression() {
        return expression;
    }
    public void setExpression(String expression) {
        this.expression = expression;
    }
    public Fraction getResult() {
        return result;
    }
    public void setResult(Fraction result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return expression;
    }
}