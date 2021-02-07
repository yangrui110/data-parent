package top.sanguohf.egg.util;

import top.sanguohf.egg.ops.EntityJoinTable;
import top.sanguohf.egg.xml.transfer.TransferEnum;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static top.sanguohf.egg.reflect.ReflectEntity.getTableField;

public class StringUtils {

    public static boolean isEmpty(String string){
        if(string==null||"".equals(string)){
            return true;
        }
        return false;
    }

    /**
     * 下划线转驼峰法(默认小驼峰)
     *
     * @param line
     *            源字符串
     * @param smallCamel
     *            大小驼峰,是否为小驼峰(驼峰，第一个字符是大写还是小写)
     * @return 转换后的字符串
     */
    public static String underline2Camel(String line, boolean ... smallCamel) {
        if (line == null || "".equals(line)) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        Pattern pattern = Pattern.compile("([A-Za-z\\d]+)(_)?");
        Matcher matcher = pattern.matcher(line);
        //匹配正则表达式
        while (matcher.find()) {
            String word = matcher.group();
            //当是true 或则是空的情况
            if((smallCamel.length ==0 || smallCamel[0] ) && matcher.start()==0){
                sb.append(Character.toLowerCase(word.charAt(0)));
            }else{
                sb.append(Character.toUpperCase(word.charAt(0)));
            }

            int index = word.lastIndexOf('_');
            if (index > 0) {
                sb.append(word.substring(1, index).toLowerCase());
            } else {
                sb.append(word.substring(1).toLowerCase());
            }
        }
        return sb.toString();
    }

    /**
     * 驼峰法转下划线
     *
     * @param line
     *            源字符串
     * @return 转换后的字符串
     */
    public static String camel2Underline(String line) {
        if (line == null || "".equals(line)) {
            return "";
        }
        line = String.valueOf(line.charAt(0)).toUpperCase()
                .concat(line.substring(1));
        StringBuffer sb = new StringBuffer();
        Pattern pattern = Pattern.compile("[A-Z]([a-z\\d_]+)?");
        Matcher matcher = pattern.matcher(line);
        while (matcher.find()) {
            String word = matcher.group();
            sb.append(word.toLowerCase());
            sb.append(matcher.end() == line.length() ? "" : "_");
        }
        return sb.toString();
    }
    public static void main(String[] args) {
        String weightCom1 = camel2Underline("weight_com1");
        System.out.println(weightCom1);
    }
    /**
     * 替换condition中的列名
     * */
    public static String patternReplace(Map<String,Class> map,String tableAlias,String condition) throws NoSuchFieldException, ClassNotFoundException {
        String patter = "(?<=%s\\.)\\w+(?==| |>|<|$)";
        String format = String.format(patter, tableAlias);
        Pattern compile = Pattern.compile(format);
        Matcher matcher = compile.matcher(condition);
        StringBuffer buffer = new StringBuffer();
        while (matcher.find()){
            String group = matcher.group();
            try {
                String tableField = getTableField(map.get(tableAlias), group);
                matcher.appendReplacement(buffer, tableField);
            }catch (Exception e){
            }
        }
        matcher.appendTail(buffer);
        return buffer.toString();
    }

    /**
     * 函数条件的列替换
     * */
    public static String patternReplaceFunction(Map<String,Class> map,String tableAlias,String condition) throws NoSuchFieldException, ClassNotFoundException {
        String patter = "(?<=\\()\\w+(?=\\))";
        //String format = String.format(patter, tableAlias);
        Pattern compile = Pattern.compile(patter);
        Matcher matcher = compile.matcher(condition);
        StringBuffer buffer = new StringBuffer();
        while (matcher.find()){
            String group = matcher.group(); //如果匹配到，那么此时就是group就代表函数的参数
            String[] params = group.split(",");
            StringBuilder builder = new StringBuilder();
            for(String param: params){
                String ps = patternReplace(map,tableAlias,param);
                builder.append(ps).append(",");
            }
            if(builder.length()>0){
                matcher.appendReplacement(buffer, builder.substring(0,builder.length()-1));
            }
            /*
            try {
                String tableField = getTableField(map.get(tableAlias), group);
                matcher.appendReplacement(buffer, tableField);
            }catch (Exception e){
            }*/
        }
        matcher.appendTail(buffer);
        return buffer.toString();
    }

    /***
     * 替换condition中的表名
     */
    public static String patternTableName(String condition) {
        throw new RuntimeException("已移除对于关联查询的依赖，本插件致力于单表的增删改查！");
        /*String pattern = "\\[.*?\\]";
        Pattern compile = Pattern.compile(pattern);
        Matcher matcher = compile.matcher(condition);
        StringBuffer buffer = new StringBuffer();
        String[] packages = SqlConfigProperties.getInstance().getPackage();
        StringBuilder stringBuilder = new StringBuilder();
        while (matcher.find()){
            String group = matcher.group();
            group = group.replaceAll("\\[","");
            group = group.replaceAll("\\]","");
            boolean exist = false;
            for(String one: packages){
                try {
                    Class<?> aClass = Class.forName(one + "." + group);
                    EntityJoinTable entityJoinTable = EntityParseUtil.parseViewEntityTable(aClass);
                    exist = true;
                    stringBuilder.append("(").append(entityJoinTable.toSql(SqlConfigProperties.getInstance().getDbType())).append(")");
                } catch (ClassNotFoundException e) {
                }
            }
            if(!exist)
                throw new RuntimeException("未找到condition"+condition+"中的类："+group);
            matcher.appendReplacement(buffer,stringBuilder.toString());
        }
        matcher.appendTail(buffer);
        return buffer.toString();*/
    }

    /**
     * @param text 替换的文本
     * */
    public static String replace(String text){
        String format = "#\\{.*?\\}";
        Pattern compile = Pattern.compile(format);
        Matcher matcher = compile.matcher(text);
        StringBuffer buffer = new StringBuffer();
        while (matcher.find()){
            String group = matcher.group();
            try {
                matcher.appendReplacement(buffer, "?");
            }catch (Exception e){
            }
        }
        matcher.appendTail(buffer);
        return buffer.toString();
    }

    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("-?[0-9]+\\.?[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }
    /**
     * 替换转义字符
     * */
    public static String replaceTrans(String text){
        text=text.replaceAll(TransferEnum.Lt.getKey(),TransferEnum.Lt.getValue());
        text=text.replaceAll(TransferEnum.Gt.getKey(),TransferEnum.Gt.getValue());
        text=text.replaceAll(TransferEnum.Amp.getKey(),TransferEnum.Amp.getValue());
        text=text.replaceAll(TransferEnum.Apos.getKey(),TransferEnum.Apos.getValue());
        text=text.replaceAll(TransferEnum.Quot.getKey(),TransferEnum.Quot.getValue());
        return text;
    }

    /**
     * 是否包含文字和数数字
     * */
    public static boolean containTextOrDigit(String str){
        String regEx = ".*[a-zA-Z]+.*";
        Pattern p = Pattern.compile(regEx);
        Matcher matcher = p.matcher(str);
        boolean matches = matcher.find();
        return matches;
    }
}
