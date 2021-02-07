package top.sanguohf.egg;

import com.alibaba.fastjson.JSONObject;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import top.sanguohf.egg.base.EntityGroupBy;
import top.sanguohf.egg.base.EntityOrderBy;
import top.sanguohf.egg.ops.EntityDeleteSql;
import top.sanguohf.egg.ops.EntitySelectSql;
import top.sanguohf.egg.ops.EntityUpdateSql;
import top.sanguohf.egg.param.EntityParamParse;
import top.sanguohf.egg.param.EntityParams;
import top.sanguohf.egg.xml.SqlXmlExpresession;

import java.io.ByteArrayInputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainTest {

    public static void main1(String[] args) throws ClassNotFoundException, NoSuchFieldException {
        EntityParams params=new EntityParams();
        JSONObject os=new JSONObject();
        os.put("left","userName");
        os.put("right","123456");
        os.put("relation","=");
        //
        JSONObject os2 = new JSONObject();
        os2.put("left","password");
        os2.put("right",888);
        os2.put("relation","=");
        List ls =new LinkedList<>();
        ls.add(os);
        ls.add(os2);
        JSONObject com1=new JSONObject();
        com1.put("condition",ls);
        com1.put("combine","or");
        /*JSONObject com=new JSONObject();
        com.put("condition",ls);
        com.put("combine","and");
        params.setCondition(com);*/

        JSONObject os3=new JSONObject();
        os3.put("left","userName");
        os3.put("right","67755");
        os3.put("relation","=");
        //
        JSONObject os4 = new JSONObject();
        os4.put("left","password");
        os4.put("right","abc");
        os4.put("relation","=");
        List ls2 =new LinkedList<>();
        ls2.add(os3);
        ls2.add(os4);
        JSONObject com2=new JSONObject();
        com2.put("condition",ls2);
        com2.put("combine","or");

        JSONObject l1 = new JSONObject();
        l1.put("left",com1);
        l1.put("right",com2);
        l1.put("relation","and");
        params.setCondition(l1);
        params.setTableClassName("top.sanguohf.egg.reflect.User");

        EntityOrderBy orderBy1=new EntityOrderBy();
        orderBy1.setColumn("userName");
        orderBy1.setDirect("desc");
        EntityOrderBy orderBy2 =new EntityOrderBy();
        List order = new LinkedList<>();
        order.add(orderBy1);

        EntityGroupBy groupBy = new EntityGroupBy();
        groupBy.setColumn("userName");
        EntityGroupBy groupBy1 = new EntityGroupBy();
        groupBy1.setColumn("password");
        LinkedList objects = new LinkedList<>();
        objects.add(groupBy);
        objects.add(groupBy1);
        params.setGroupBy(objects);
        //params.setOrderBy(order);
        JSONObject os0=new JSONObject();
        os0.put("userName","admin1");
        os0.put("password","11122");
        //params.setData(os0);
        EntitySelectSql selectSql=new EntityParamParse(params).parseToEntitySelectSql();
        System.out.println(selectSql.toSql());
        System.out.println(selectSql.sqlOne(true));
        LinkedList linkedList = new LinkedList<>();
        selectSql.addValue(linkedList);
        System.out.println(linkedList);
       // System.out.println(selectSql.);
        //System.out.println(new EntityPageSql(selectSql).toPageSql(1,10, DbType.ORACLE));
        //System.out.println(new EntityPageSql(selectSql).toCountSql(DbType.SQL));
        //ReflectEntity entity = new ReflectEntity();
        //System.out.println(entity.reflectSelectColumns(User.class));
    }

    public static void main4(String[] args) throws ClassNotFoundException, NoSuchFieldException {
        JSONObject os=new JSONObject();
        os.put("password","pmmm");
        os.put("userName","yang");
        os.put("id","99");
        EntityParams params=new EntityParams();
        params.setTableClassName("top.sanguohf.egg.reflect.User");
        params.setCondition(os);
       // new EntityParamParse(params).parseToEntityInertSql();
        //new EntityParamParse(params).parseToEntityUpdateSql();
        EntityUpdateSql sql = new EntityParamParse(params).parseToEntityUpdateSql();
        System.out.println(sql.toSql());
        //EntityDeleteSql deleteSql = new EntityParamParse(params).parseToEntityDeleteSql();
        //System.out.println(deleteSql.toSql());
        //EntitySelectSql selectSql = new EntityParamParse(params).parseToEntitySelectSql();
        //System.out.println(selectSql.toSql());
    }

    public static void main2(String[] args) throws ClassNotFoundException {
        String content = "useriiii.userName=1 and user.password = 2 and user.id=3 and user.ps>0 and user.id > [ones] and user.po < [pj]";
        String patter = "(?<=user\\.).*?(?==| |>|<)";
        Pattern compile = Pattern.compile(patter);
        Matcher matcher = compile.matcher(content);
        StringBuffer stringBuffer = new StringBuffer();
        while (matcher.find()){
            String group = matcher.group();
            matcher.appendReplacement(stringBuffer,"__");
            //System.out.println(matcher.group());
        }
        matcher.appendTail(stringBuffer);
        System.out.println(stringBuffer.toString());
        System.out.println("--------------------");
        String pattern2 = "\\[.*?\\]";
        Pattern compile2 = Pattern.compile(pattern2);
        Matcher matcher2 = compile2.matcher(content);
        while (matcher2.find()){
            System.out.println(matcher2.group());
        }
        Class<?> forName = Class.forName("top.sanguohf.egg.reflect.SubTeacher");
        Field[] fields = forName.getDeclaredFields();
        for(Field field:fields){
            System.out.println(field.getGenericType().getTypeName());
        }
        //System.out.println(forName.getName());
    }

    public static void maint(String[] args) throws ClassNotFoundException, NoSuchFieldException {
        EntityParams entityParams = new EntityParams();
        Class<?> aClass = Class.forName("top.sanguohf.egg.reflect.ThirdTeacher");
        entityParams.setTableClassName("top.sanguohf.egg.reflect.ThirdTeacher");
        EntityDeleteSql deleteSql = new EntityParamParse(entityParams).parseToEntityDeleteSql();
//        entityParams.setTableClassName("top.sanguohf.egg.reflect.TeacherView");
//        EntitySelectSql selectSql = new EntityParamParse(entityParams).parseToEntitySelectSql();
        System.out.println(deleteSql.toSql());
    }

    public static void main(String[] args) throws DocumentException {
        String xml = "<sqls >\n" +
                "    <sql id=\"getUser\">\n" +
                "        select * from user\n" +
                "        <trim prefix=\"where\" prefixOverrides=\"and\">\n" +
                "            <if test=\"id!=null\">\n" +
                "                and id = #{id} and k &lt;= t\n" +
                "            </if>\n" +
                "            <if test=\"name!=null and name !=''\">\n" +
                "                and name = #{name}\n" +
                "            </if>\n" +
                "        </trim>66666ssss\n" +
                "    </sql>\n" +
                "</sqls>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(xml.getBytes());
        SAXReader reader = new SAXReader();
        Document read = reader.read(inputStream);
        List<Node> content = read.content();
        for(Node node: content){

        }
        System.out.println(content.size());
        Element rootElement = read.getRootElement();
        List<Element> sql = rootElement.elements("sql");
        HashMap map = new HashMap<>();
        map.put("yang","55");
        ArrayList list = new ArrayList<>();
        HashMap map1 = new HashMap<>();
        map1.put("whatName","8");
        HashMap map2 = new HashMap<>();
        map2.put("whatName","777");
        list.add(map1);
        list.add(map2);
        map.put("collect",list);
        map.put("id","oooooo");
        map.put("name","王五");
        for(Element element: sql){
            System.out.println(element.getText());
            SqlXmlExpresession expresession = new SqlXmlExpresession(map, element);
            String sqlOne = expresession.sqlOne(true);
            ArrayList list1 = new ArrayList<>();
            expresession.addValue(list1);
            System.out.println(sqlOne);
            list1.forEach(k-> System.out.println(k));
        }
    }
}
