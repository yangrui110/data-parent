package top.sanguohf.egg.xml.expr;

import java.util.ArrayList;
import java.util.List;


public class PhraseUtil {
    private static PhraseMap map = new PhraseMap();
    /** @pdOid bae8ae01-da7d-4ccc-83d0-dcd39a783c84 */
    public static List<PhraseEntity> parseToPhrase(String str) {
        // TODO: implement
        ArrayList<PhraseEntity> list = new ArrayList<>();
        char[] chars = str.toCharArray();
        StringBuilder builder = new StringBuilder();
        char lastChar = '0';
        for(char one: chars){
            if(one==Phrase.LeftBracket.getKey()){
                builder.append(Phrase.LeftBracket.getKey());
                list.add(createPhrase(builder,getType(builder.toString())));
                builder = new StringBuilder();
            }else if(one==Phrase.RightBracket.getKey()){
                if(builder.length()>0){
                    list.add(createPhrase(builder,getType(builder.toString())));
                    builder = new StringBuilder();
                }
                builder.append(Phrase.RightBracket.getKey());
                list.add(createPhrase(builder,getType(builder.toString())));
                builder = new StringBuilder();
            }else if(one==Phrase.Not.getKey()||one==Phrase.Gt.getKey()||one==Phrase.Lt.getKey()){
                if (builder.length()>0){
                    list.add(createPhrase(builder,getType(builder.toString())));
                    builder = new StringBuilder();
                }
                builder.append(one);
            }else if(one==Phrase.Eq.getKey()){
                if(builder.length()>0&&lastChar!=Phrase.Not.getKey()&&lastChar!=Phrase.Gt.getKey()&&lastChar!=Phrase.Lt.getKey()){
                    list.add(createPhrase(builder,getType(builder.toString())));
                    builder = new StringBuilder();
                }
                builder.append(one);
                list.add(createPhrase(builder,getType(builder.toString())));
                builder = new StringBuilder();
            }else if(one == Phrase.Space.getKey()||one==Phrase.Comma.getKey()||one==Phrase.NewLine.getKey()){
                if(builder.length()>0){
                    list.add(createPhrase(builder,getType(builder.toString())));
                    builder = new StringBuilder();
                }
                builder.append(one);
                list.add(createPhrase(builder,getType(builder.toString())));
                builder = new StringBuilder();
            }else {
                if(lastChar==Phrase.Not.getKey()||lastChar==Phrase.Gt.getKey()||lastChar==Phrase.Lt.getKey()){
                    builder.append(lastChar);
                    list.add(createPhrase(builder,getType(builder.toString())));
                    builder = new StringBuilder();
                }
                builder.append(one);
            }
            lastChar = one;
        }
        if(builder.length()>0){
            list.add(createPhrase(builder,getType(builder.toString())));
        }
        return list;
    }
    private static PhraseType getType(String key){
        PhraseType type = map.get(key);
        if(type == null)
            return PhraseType.NormalChar;
        return type;
    }
    private static PhraseEntity createPhrase(StringBuilder builder,PhraseType type){
        PhraseEntity entity = new PhraseEntity();
        entity.setKey(builder.toString());
        entity.setPhraseType(type);
        return entity;
    }


}
