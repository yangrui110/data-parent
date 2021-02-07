package top.sanguohf.egg.xml.transfer;

/**
 * @project platform
 * @Date 2020/9/2
 * @Auth 杨瑞
 **/
public enum  TransferEnum {

    Lt("&lt;","<"),Gt("&gt;",">"),Amp("&amp;","&"),Apos("&apos;","'"),Quot("&quot;","\"");

    private String key;
    private String value;


    TransferEnum(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }}
