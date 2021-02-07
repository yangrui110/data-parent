package top.sanguohf.egg.xml.expr;


public enum  Phrase {

    LeftBracket('('),RightBracket(')'),Not('!'),Eq('='),Gt('>'),Lt('<'),Space(' '),Comma(','),NewLine('\n');

    private char key;

    Phrase(char key) {
        this.key = key;
    }

    public char getKey() {
        return key;
    }
}
