package top.sanguohf.egg.xml.expr;


import lombok.Data;

@Data
public class PhraseEntity {
    private String key;
    /** @pdOid a982904b-9f58-4b09-97dd-67104d06a60e */
    private PhraseType phraseType;

    @Override
    public String toString() {
        return "PhraseEntity{" +
                "key='" + key + '\'' +
                ", phraseType=" + phraseType +
                '}';
    }
}
