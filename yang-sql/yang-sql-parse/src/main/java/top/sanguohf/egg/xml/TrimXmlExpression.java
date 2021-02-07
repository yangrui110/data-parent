package top.sanguohf.egg.xml;

import org.dom4j.Element;
import top.sanguohf.egg.util.StringUtils;
import top.sanguohf.egg.xml.variable.ParentVariable;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @project platform
 * @Date 2020/8/30
 * @Auth 杨瑞
 **/
public class TrimXmlExpression extends AbstractXmlExpresession {

    // sql前缀
    private String prefix;
    // SQL后缀
    private String suffix;
    // sql前去除字段
    private String prefixOverrides;
    // sql后去除字段
    private String suffixOverrides;

    public TrimXmlExpression(Object data, Element element,List<ParentVariable> variable) {
        super(data, element,variable);
        this.prefix = getElement().attributeValue("prefix");
        this.suffix = getElement().attributeValue("suffix");
        this.prefixOverrides = getElement().attributeValue("prefixOverrides");
        this.suffixOverrides = getElement().attributeValue("suffixOverrides");
    }

    @Override
    public String sqlOne(boolean isPrepare) {
        // 先考虑是prepare语句
        String sqlOne = super.sqlOne(isPrepare);
        StringBuilder builder = new StringBuilder(sqlOne);

        // 3、去掉第一个
        if(!StringUtils.isEmpty(prefixOverrides)){
            int indexOf = builder.indexOf(prefixOverrides);
            if(indexOf!=-1)
            builder.replace(indexOf,indexOf+prefixOverrides.length(),"");
        }
        if(!StringUtils.isEmpty(suffixOverrides)){
            int indexOf = builder.lastIndexOf(suffixOverrides);
            if(indexOf!=-1)
            builder.replace(indexOf,indexOf+suffixOverrides.length(),"");
        }
        if(!StringUtils.isEmpty(prefix)){
            if(StringUtils.containTextOrDigit(builder.toString()))
                builder.insert(0,prefix).append(" ");
        }
        if(!StringUtils.isEmpty(suffix)){
            if(StringUtils.containTextOrDigit(builder.toString()))
                builder.append(" ").append(suffix);
        }
        return builder.toString();
    }

    /*@Override
    public void addValue(List list) {
        List<Element> elements = getElement().elements();
        for(Element element: elements){
            String name = element.getName();
            if(name.equalsIgnoreCase("foreach")){
                ForXmlExpression expression = new ForXmlExpression(getData(), element,getVariables());
                expression.addValue(list);
            }
            if(name.equalsIgnoreCase("where")){
                WhereXmlExpression expression = new WhereXmlExpression(getData(), element,getVariables());
                expression.addValue(list);
            }
            if(name.equalsIgnoreCase("if")){
                IfXmlExpresession expression = new IfXmlExpresession(getData(), element,getVariables());
                expression.addValue(list);
            }
        }
    }*/
}
