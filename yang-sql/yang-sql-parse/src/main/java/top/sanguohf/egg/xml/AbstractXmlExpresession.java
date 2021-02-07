package top.sanguohf.egg.xml;

import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.Text;
import top.sanguohf.egg.SqlParse;
import top.sanguohf.egg.util.StringUtils;
import top.sanguohf.egg.xml.expr.PhraseEntity;
import top.sanguohf.egg.xml.expr.PhraseUtil;
import top.sanguohf.egg.xml.variable.ParentVariable;
import top.sanguohf.egg.xml.variable.VariableAdd;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @project platform
 * @Date 2020/8/15
 * @Auth 杨瑞
 **/
public abstract class AbstractXmlExpresession implements SqlParse {
    // 查询条件数据
    private Object data;
    // xml元素
    private Element element;

    private String text;
    // 上级的变量
    private List<ParentVariable> variables = new ArrayList<>();

    public AbstractXmlExpresession(Object data, Element element,List<ParentVariable> variable) {
        this.data = data;
        this.element = element;
        // 拷贝属性
        this.variables = variable;
        this.text = getElement().getText();
    }

    /**
     * 根据上级的key和attr获取到对应的属性值
     * @param key 上一级的key
     * */
    public Object getParam(String key){
        for(int i=getVariables().size()-1;i>=0;i--){
            ParentVariable variable = getVariables().get(i);
            if(!StringUtils.isEmpty(key)&&key.equalsIgnoreCase(variable.getKey())){
                // 输出相等
                return variable.getData();
            }else if(key == null) {
                return getVariables().get(0).getData();
            }
        }
        Object data=null;
        if(getVariables().size()==0){
            data=getData();
        }else {
            data = getVariables().get(0).getData();
        }
        if(data instanceof Map) return ((Map) data).get(key);
        return data;
    }

    /**
     * 获取到$的值
     * */
    public Object get$param(String key){
        String s = "(?<=\\$\\{).*?(?=\\})";
        Pattern compile = Pattern.compile(s);
        Matcher matcher = compile.matcher(key);
        while (matcher.find()){
            String group = matcher.group();
            String[] split = group.split("\\.");
            if(split.length==1) {
                Object param = getParam(null);
                return param;
            }else {
                Object one = getParam(split[0]);
                int i = 0;
                while (one==null&&i<split.length-1){
                    i++;
                    one=getParam(split[i]);
                }
                if(one==null){
                    return null;
                }else {
                    // 从i开始，循环获取数据
                    Object o = ((Map) one).get(split[i + 1]);
                    i++;
                    while(o!=null&&i<split.length-1){
                        o = ((Map) o).get(split[i + 1]);
                        i++;
                    }
                    return o;
                }
            }
        }
        return null;
    }

    @Override
    public void addValue(List list) {
        String s = "(?<=#\\{).*?(?=\\})";
        Pattern compile = Pattern.compile(s);
        // 获取到节点
        List<Node> content = getElement().content();
        for(Node node: content){
            if(node instanceof Text){
                addTextValue(node.getText(),list,compile);
            }else if(node instanceof Element){
                addElementValue((Element) node,list);
            }
        }

    }
    private void addElementValue(Element element,List list){
        List<ParentVariable> vars = VariableAdd.addValue(getVariables(), null, getData());
        String name = element.getName();
        AbstractXmlExpresession expression = null;
        if(name.equalsIgnoreCase("foreach")){
            expression = new ForXmlExpression(getData(), element,vars);
        }else if(name.equalsIgnoreCase("where")){
            expression = new WhereXmlExpression(getData(), element,vars);
        }else if(name.equalsIgnoreCase("if")){
            expression = new IfXmlExpresession(getData(), element,vars);
        }else if(name.equalsIgnoreCase("trim")){
            expression = new TrimXmlExpression(getData(), element,vars);
        }
        expression.addValue(list);
    }
    private void addTextValue(String str,List list,Pattern compile){
        List<PhraseEntity> entities = PhraseUtil.parseToPhrase(str);
        for(PhraseEntity entity: entities){
            Matcher matcher1 = compile.matcher(entity.getKey());

            if(matcher1.find()){
                String group = matcher1.group();
                if(group.length()==entity.getKey().length()-3)
                    addParamData(group,list,null,false);
                else addParamData(group,list,entity.getKey(),true);
            }
        }
    }

    private Object addParamData(String str,List list,String totalStr,boolean isReplace){
        String[] split = str.split("\\.");
        if(split.length==1) {
            Object param = getParam(null);
            if(param!=null){
                Object o = ((Map) param).get(split[0]);
                addOne(str,list,totalStr,isReplace,o);
            }else list.add(null);
        }else {
            Object one = getParam(split[0]);
            int i = 0;
            while (one==null&&i<split.length-1){
                i++;
                one=getParam(split[i]);
            }
            if(one==null){
                list.add(null);
            }else {
                // 从i开始，循环获取数据
                Object o = ((Map) one).get(split[i + 1]);
                i++;
                while(o!=null&&i<split.length-1){
                    o = ((Map) o).get(split[i + 1]);
                    i++;
                }
                if(o!=null) {
//                    list.add(o);
                    addOne(str,list,totalStr,isReplace,o);
                }
            }
        }
        return null;
    }
    private void addOne(String str,List list,String totalStr,boolean isReplace,Object o){
        if(isReplace) {
            String replace = totalStr.replace("#{" + str + "}", ""+ o);
            /*StringBuilder builder = new StringBuilder();
            builder.append("'").append(replace).append("'");
            list.add(builder.toString());*/
            list.add(replace);
        } else {
            list.add(o);
            /*if(o instanceof Number) {
                list.add(o);
            }else {
                StringBuilder builder = new StringBuilder("'");
                builder.append(o).append("'");
                list.add(builder.toString());
            }*/
        }
    }

    @Override
    public String sqlOne(boolean isPrepare) {
        StringBuilder builder = new StringBuilder();
        List<ParentVariable> list = getVariables();
        if(!getElement().getName().equalsIgnoreCase("trim"))
            list = VariableAdd.addValue(getVariables(), null, getData());
        List<Node> contents = getElement().content();
        for(Node node: contents){
            if(node instanceof Text){
                String text = node.getText();
                String str = parseStr(text);
                builder.append(str);
            }else if(node instanceof Element){
                parseElement((Element) node,builder,isPrepare,list);
            }
        }

        return builder.toString();
    }
    private void parseElement(Element element,StringBuilder builder,boolean isPrepare,List list){
        String name = element.getName();
        AbstractXmlExpresession expression = null;
        if(name.equalsIgnoreCase("foreach")){
            expression = new ForXmlExpression(getData(), element,list);

        }else if(name.equalsIgnoreCase("where")){
            expression = new WhereXmlExpression(getData(), element,list);
        }else if(name.equalsIgnoreCase("if")){
            expression = new IfXmlExpresession(getData(), element,list);
        }else if(name.equalsIgnoreCase("trim")){
            expression = new TrimXmlExpression(getData(), element,list);
        }
        if(expression !=null){
            String sqlOne = expression.sqlOne(isPrepare);
            if(StringUtils.containTextOrDigit(sqlOne)) {
                builder.append(" ").append(sqlOne).append(" ");
            }
        }
    }

    private String parseStr(String str){
        String text = StringUtils.replaceTrans(str);
        // 1、分离出词组
        List<PhraseEntity> entities = PhraseUtil.parseToPhrase(text);
        // 2.构造
        StringBuilder builder = new StringBuilder();
        // 3.替换#号表达式
        String s = "(?<=#\\{).*?(?=\\})";
        Pattern compile = Pattern.compile(s);
        for(PhraseEntity entity: entities){
            if(compile.matcher(entity.getKey()).find()) {
                builder.append("?");
            }else builder.append(entity.getKey());
        }
        String toString = builder.toString();
        // 4.替换$表达式
        String s1 = "(?<=\\$\\{).*?(?=\\})";
        Pattern compile1 = Pattern.compile(s1);
        for(PhraseEntity entity: entities){
            if(compile1.matcher(entity.getKey()).find()) {
                Object param = get$param(entity.getKey());
                toString=toString.replaceAll("\\$","").replaceAll("\\{","").replaceAll("\\}","");
                String key = entity.getKey().replaceAll("\\$","").replaceAll("'","").replaceAll("\\{","").replaceAll("\\}","");
                toString = toString.replaceAll(key, (String) param);
            }
        }
        return toString;
    }

    public Object getData() {
        return data;
    }

    public Element getElement() {
        return element;
    }

    public List<ParentVariable> getVariables() {
        return variables;
    }

    public String getText() {
        return text;
    }

    public void setVariables(List<ParentVariable> variables) {
        this.variables = variables;
    }
}