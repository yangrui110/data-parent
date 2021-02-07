package top.sanguohf.egg.xml.expr;


import lombok.Data;

import java.math.BigDecimal;

@Data
public class SentenceEntity extends AbstractSentence {
    /** @pdOid 392e7c49-eb46-4f72-b02b-2c87a2091ad5 */
    private AbstractSentence leftData;
    /** @pdOid 84019818-13f4-455c-9ada-7169267841b7 */
    private String relation;
    /** @pdOid b3eedf92-3765-46d7-90e7-fda90c5b97e4 */
    private AbstractSentence rightData;


    @Override
    public Object getResult() {
        // TODO: implement
        String s1 = String.valueOf(leftData.getResult());
        String s2 = String.valueOf(rightData.getResult());
        if(relation.equalsIgnoreCase(WordGroup.AND.getWord())) {
            if("true".equalsIgnoreCase(s1)&&"true".equalsIgnoreCase(s2)){
                return true;
            }else return false;
        }else if(relation.equalsIgnoreCase(WordGroup.OR.getWord())){
            if("true".equalsIgnoreCase(s1)||"true".equalsIgnoreCase(s2)){
                return true;
            }else return false;
        }
        else if(relation.equals(WordGroup.Eq.getWord())) return s1.equalsIgnoreCase(s2);
        else if(relation.equals(WordGroup.Gt.getWord())
                ||relation.equals(WordGroup.Gqt.getWord())
                ||relation.equals(WordGroup.Lt.getWord())
                ||relation.equals(WordGroup.Lqt.getWord())
                ||relation.equals(WordGroup.Add.getWord())
                ||relation.equals(WordGroup.Sub.getWord())
                ||relation.equals(WordGroup.Mul.getWord())
                ||relation.equals(WordGroup.Devide.getWord())
        ) {
            BigDecimal decimal = new BigDecimal(s1);
            BigDecimal bigDecimal = new BigDecimal(s2);
            int to = decimal.compareTo(bigDecimal);
            // 大于
            if(relation.equals(WordGroup.Gt.getWord())) {
                if(to==1){
                    return true;
                }else  return false;
            }
            // 大于等于
            else if(relation.equals(WordGroup.Gqt.getWord())) {
                if(to==1||to==0){
                    return true;
                }else  return false;
            }
            // 小于
            else if(relation.equals(WordGroup.Lt.getWord())) {
                if(to==-1){
                    return true;
                }else  return false;
            }
            // 小于等于
            else if(relation.equals(WordGroup.Lqt.getWord())) {
                if(to==-1||to==0){
                    return true;
                }else  return false;
            }
            // 加法
            else if(relation.equals(WordGroup.Add.getWord())) {
                BigDecimal add = decimal.add(bigDecimal);
                return returnData(add,leftData.getResult());
            }else if(relation.equals(WordGroup.Devide.getWord())) {
                BigDecimal add = decimal.divide(bigDecimal);
                return returnData(add,leftData.getResult());
            }else if(relation.equals(WordGroup.Sub.getWord())) {
                BigDecimal add = decimal.subtract(bigDecimal);
                return returnData(add,leftData.getResult());
            }else if(relation.equals(WordGroup.Mul.getWord())) {
                BigDecimal add = decimal.multiply(bigDecimal);
                return returnData(add,leftData.getResult());
            }
        }else if(relation.equals(WordGroup.NOTEQ.getWord())) return !s1.equalsIgnoreCase(s2);
        return null;
    }

    private Object returnData(BigDecimal decimal,Object cs){
        if(cs instanceof Integer) {
            return decimal.intValue();
        }else if(cs instanceof Long) return decimal.longValue();
        else if(cs instanceof Double) return decimal.doubleValue();
        else if(cs instanceof Short) return decimal.shortValue();
        else return decimal.toPlainString();
    }
}
