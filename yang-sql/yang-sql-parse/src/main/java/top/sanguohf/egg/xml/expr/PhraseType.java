package top.sanguohf.egg.xml.expr;

/**
 * 类型
 * */
public enum PhraseType {

    SentenceStart(0,"句子开始符号"),SentenceEnd(1,"句子结束符号"),
    PharseEnd(2,"单词结束符号"),NormalChar(3,"普通单词");

    private int type;
    private String desc;

    PhraseType(int type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public int getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }
}
