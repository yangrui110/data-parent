package top.sanguohf.egg.xml.expr;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 单词表
 **/
public class PhraseMap {
    // 单词表
    private Map<String,PhraseType> map;

    public PhraseMap() {
        HashMap<String, PhraseType> one = new HashMap<>();
        one.put("(",PhraseType.SentenceStart);
        one.put(")",PhraseType.SentenceEnd);
        one.put(" ",PhraseType.PharseEnd);
        one.put("and",PhraseType.SentenceEnd);
        one.put("!=",PhraseType.PharseEnd);
        one.put("=",PhraseType.PharseEnd);
        one.put(">",PhraseType.PharseEnd);
        one.put(">=",PhraseType.PharseEnd);
        one.put("<",PhraseType.PharseEnd);
        one.put("<=",PhraseType.PharseEnd);
        this.map = Collections.unmodifiableMap(one);
    }

    public PhraseType get(String key){
        return map.get(key);
    }
}
