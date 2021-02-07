package top.sanguohf.egg.xml;

import org.dom4j.Element;
import top.sanguohf.egg.constant.DbType;
import top.sanguohf.egg.util.StringUtils;
import top.sanguohf.egg.xml.variable.ParentVariable;
import top.sanguohf.egg.xml.variable.VariableAdd;

import java.util.ArrayList;
import java.util.List;

/**
 * @project platform
 * @Date 2020/8/15
 * @Auth 杨瑞
 **/
public class SqlXmlExpresession extends AbstractXmlExpresession {

    public SqlXmlExpresession(Object data, Element element) {
        this(data,element,new ArrayList<>());
    }

    public SqlXmlExpresession(Object data, Element element, List<ParentVariable> variable) {
        super(data, element,variable);
    }

    @Override
    public String toSql(DbType dbType) {
        return null;
    }

    @Override
    public void addValue(List list) {
        ArrayList list1 = new ArrayList<>();
        list1.addAll(getVariables());
        ParentVariable parentVariable = new ParentVariable();
        parentVariable.setData(getData());
        list1.add(parentVariable);
        setVariables(list1);
        super.addValue(list);
        /*List<Element> elements = getElement().elements();
        List<ParentVariable> listOne = VariableAdd.addValue(getVariables(), null, getData());
        for(Element element: elements){
            String name = element.getName();
            if(name.equalsIgnoreCase("foreach")){
                ForXmlExpression expression = new ForXmlExpression(getData(), element,listOne);
                expression.addValue(list);
            }
            if(name.equalsIgnoreCase("where")){
                WhereXmlExpression expression = new WhereXmlExpression(getData(), element,listOne);
                expression.addValue(list);
            }
            if(name.equalsIgnoreCase("if")){
                IfXmlExpresession expression = new IfXmlExpresession(getData(), element,listOne);
                expression.addValue(list);
            }
            if(name.equalsIgnoreCase("trim")){
                TrimXmlExpression expression = new TrimXmlExpression(getData(), element,listOne);
                expression.addValue(list);
            }
        }*/
    }
}
