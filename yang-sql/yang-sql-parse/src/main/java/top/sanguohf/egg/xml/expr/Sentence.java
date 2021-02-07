package top.sanguohf.egg.xml.expr;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Sentence {
    /** @pdOid 37f368a4-43b1-4611-9205-539a2259ea29 */
    private String str;
    /** @pdOid 75a8ca7d-1b8c-48fb-8bc9-5d9ea2ad4b94 */
    private List<PhraseEntity> phrases;

    private Map osData;

    public Sentence(String str) {
        this.str = str;
        this.phrases = PhraseUtil.parseToPhrase(str);
    }
    public Sentence(String str,Map osData){
        this(str);
        this.osData = osData;
    }

    /** @pdOid 6d8c5f61-46f2-4e49-848e-0c33c458e8fb */
    public AbstractSentence parseToSentenceEntity() {
        AbstractSentence parse = parse(0, phrases.size());
        return parse;
    }

    private AbstractSentence parse(int start,int end){
        // 4、再次循环

        for(int i=start;i<end;i++){
            PhraseEntity entity = phrases.get(i);
            if(entity.getKey().equals(WordGroup.LeftBracket.getWord())){
                // 开启一个小循环，获取到最右侧的右括号
                int result = 0;
                for(int j=i+1;j<end;j++){
                    if(phrases.get(j).getKey().equals(WordGroup.RightBracket.getWord())){
                        result = j;
                    }
                }
                if(result==0) throw new RuntimeException("括号不匹配");
                if(result == end-1){
                    // 直接返回
                    return parse(i+1,end-1);
                }
                i=result+1;
            }else if(entity.getKey().equalsIgnoreCase(WordGroup.AND.getWord())||entity.getKey().equalsIgnoreCase(WordGroup.OR.getWord())){
                // 同时，把左侧的数据加入到
                SentenceEntity sentenceEntity = new SentenceEntity();
                AbstractSentence parseLeft = parse(start,i-1);
                AbstractSentence parseRight = parse(i+1,end);
                sentenceEntity.setLeftData(parseLeft);
                sentenceEntity.setRightData(parseRight);
                sentenceEntity.setRelation(entity.getKey());
                return sentenceEntity;
            }else if(i==end-1){
                SentenceEntity sentenceEntity = new SentenceEntity();
                ArrayList list = new ArrayList<>();
                for(int j=start;j<end;j++){
                    PhraseEntity phraseEntity = phrases.get(j);
                    if(!phraseEntity.getKey().equals(WordGroup.Space.getWord())){
                        // 只允许构造三元表达式
                        list.add(phraseEntity.getKey());
                    }
                }
                if(list.size()!=3){
                    throw new RuntimeException("语句格式错误");
                }
                sentenceEntity.setLeftData(new ConcreteSentenceEntity(list.get(0),osData));
                sentenceEntity.setRightData(new ConcreteSentenceEntity(list.get(2),osData));
                sentenceEntity.setRelation(list.get(1)==null?"=": String.valueOf(list.get(1)));
                return sentenceEntity;
            }
        }
        return null;
    }

    public static void main(String[] args) {
        BigDecimal decimal = new BigDecimal("10");
        BigDecimal decimal1 = new BigDecimal("20");
        BigDecimal add = decimal.add(decimal1);
        System.out.println(add.toPlainString());
        HashMap map = new HashMap<>();
        ArrayList list = new ArrayList<>();
        list.add("999");
        map.put("yang",list);
        String sql = "0= 0";
        Sentence sentence = new Sentence(sql,map);
        AbstractSentence entity = sentence.parseToSentenceEntity();
        System.out.println(entity.getResult());
    }
}
