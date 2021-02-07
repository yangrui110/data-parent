package top.sanguohf.egg.xml;

import org.dom4j.Element;
import top.sanguohf.egg.util.StringUtils;
import top.sanguohf.egg.xml.expr.*;
import top.sanguohf.egg.xml.variable.ParentVariable;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author 杨瑞
 **/
public class IfXmlExpresession extends AbstractXmlExpresession {

    private String parentKey,test;

    public IfXmlExpresession(Object data, Element element,List<ParentVariable> variable) {
        super(data, element,variable);
        this.test = getElement().attributeValue("test");
    }

    public IfXmlExpresession(Object data, Element element, List<ParentVariable> variable, String parentKey) {
        super(data, element,variable);
        this.parentKey = parentKey;
        this.test = getElement().attributeValue("test");
        this.test = test.replaceAll(this.parentKey+".","");
    }

    @Override
    public String sqlOne(boolean isPrepare) {
        List<ParentVariable> variables = getVariables();
        if(test.equalsIgnoreCase("true")||getTest()) {
            return super.sqlOne(isPrepare);
        }
        return "";
    }

    public boolean getTest(){
        Sentence sentence = new Sentence(test, (Map) getData());
        AbstractSentence abstractSentence = sentence.parseToSentenceEntity();
        Object result = abstractSentence.getResult();
        if(result!=null&&(Boolean)result){
            return true;
        }
        return false;
    }

    @Override
    public void addValue(List list) {
        if(!getTest()) return;
        super.addValue(list);
    }
}
