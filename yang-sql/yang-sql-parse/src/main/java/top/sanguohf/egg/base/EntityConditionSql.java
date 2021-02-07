package top.sanguohf.egg.base;

import lombok.Data;

@Data
public class EntityConditionSql implements EntityCondition {

    private String sql;

    @Override
    public String sqlOne(boolean isPrepare) {
        return sql;
    }
}
