package top.sanguohf.egg.xml;

import org.dom4j.Element;
import top.sanguohf.egg.util.StringUtils;
import top.sanguohf.egg.xml.util.LoadFieldValue;
import top.sanguohf.egg.xml.variable.ParentVariable;
import top.sanguohf.egg.xml.variable.VariableAdd;

import java.util.List;

/**
 * @project platform
 * @Date 2020/8/15
 * @Auth 杨瑞
 **/
public class ForXmlExpression extends AbstractXmlExpresession {

    // 开始构建for循环的语句
    String collection,item,separator,open,close,index,text;

    public ForXmlExpression(Object data, Element element,List<ParentVariable> variable) {
        super(data, element,variable);
        this.collection = getElement().attributeValue("collection");
        this.item = getElement().attributeValue("item");
        this.separator = getElement().attributeValue("separator");
        this.open = getElement().attributeValue("open");
        this.close = getElement().attributeValue("close");
        this.index = getElement().attributeValue("index");
        this.text = getElement().getText();
    }


    @Override
    public String sqlOne(boolean isPrepare) {
        StringBuilder builder = new StringBuilder();
        // 1.获取到遍历数据集合
        Object value = LoadFieldValue.getValue(getData(), collection);
        if(value ==null) return "";

        for(Object one: (List)value){
            proccessIntenerELement(one,getElement(),builder);
        }
        if(builder.length()>0) {
            builder.insert(0,open);
            String substring = builder.substring(0, builder.lastIndexOf(separator));
            substring += close;
            return substring;
        }
        return builder.toString();
    }

    /**
     * @param data 处理数据
     * @param element 父级元素
     * */
    private void proccessIntenerELement(Object data,Element element,StringBuilder builder){
        // 处理元素
        List<Element> elements = element.elements();
        for(Element one: elements){
            if(one.getName().equalsIgnoreCase("if")){
                // 新增一个数据
                List<ParentVariable> list = VariableAdd.addValue(getVariables(), this.item, data);

                IfXmlExpresession expresession = new IfXmlExpresession(data, one,list,item);
                if(expresession.getTest()) {
                    String sqlOne = expresession.sqlOne(true);
                    builder.append(sqlOne);
                    // 处理普通的文本
                    String replace = StringUtils.replace(text);
                    builder.append(replace).append(separator);
                }
            }
        }
    }

    @Override
    public void addValue(List list) {
        // 1.获取到遍历数据集合
        Object value = LoadFieldValue.getValue(getData(), collection);
        if(value ==null) return;

        List<Element> elements = getElement().elements();
        for(Object one: (List)value){
            for(Element two: elements){
                if(two.getName().equalsIgnoreCase("if")){
                    // 新增一个数据
                    List<ParentVariable> listOne = VariableAdd.addValue(getVariables(), this.item, one);

                    IfXmlExpresession expresession = new IfXmlExpresession(one, two,listOne,item);
                    if(expresession.getTest()) {
                        expresession.addValue(list);
                    }
                }
            }
        }


    }
}
