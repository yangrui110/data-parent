package top.sanguohf.egg.xml.expr;

public enum  WordGroup {
    AND("and"),OR("or"),NOTEQ("!="),LeftBracket("("),RightBracket(")"),Space(" "),
    Gqt(">="),Lqt("<="),Gt(">"),Lt("<"),Eq("="),Add("+"),Sub("-"),Mul("*"),Devide("/");

    private String word;

    WordGroup(String word) {
        this.word = word;
    }

    public String getWord() {
        return word;
    }
}
