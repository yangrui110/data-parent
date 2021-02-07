package top.sanguohf.egg.xml;

import org.dom4j.Element;
import top.sanguohf.egg.util.StringUtils;
import top.sanguohf.egg.xml.variable.ParentVariable;

import java.util.List;

/**
 * @project platform
 * @Date 2020/8/15
 * @Auth 杨瑞
 **/
public class WhereXmlExpression extends AbstractXmlExpresession {
//    private String text = "";
    public WhereXmlExpression(Object data, Element element,List<ParentVariable> variable) {
        super(data, element,variable);
    }

    @Override
    public String sqlOne(boolean isPrepare) {
        String sqlOne = super.sqlOne(isPrepare);
        StringBuilder builder = new StringBuilder(sqlOne);
        return builder.insert(0,"where").append(" ").toString();
    }

    @Override
    public void addValue(List list) {
        super.addValue(list);
    }
}
