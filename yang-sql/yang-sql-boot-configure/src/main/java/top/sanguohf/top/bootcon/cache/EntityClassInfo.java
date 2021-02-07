package top.sanguohf.top.bootcon.cache;

import lombok.Data;

import java.util.List;

/**
 * @author 杨瑞
 **/
@Data
public class EntityClassInfo implements ClassInfo {
    /** @pdOid 29ce2116-409f-4814-9f35-05dfadab9541 */
    private String tableName;
    /** @pdOid 7f02f03d-98c4-4f10-a1d5-a6418683ff86 */
    private String tableAlias;
    /** @pdOid 1f55376e-ced4-4744-8059-b1138a8cef70 */
    private List<FieldInfo> totalFields;
    /** @pdOid 489e746d-8b18-421d-82d9-eb3f91803bcc */
    private List<FieldInfo> includeFields;
    /** @pdOid 8ea256d1-f57f-42a5-a504-8b99566e943e */
    private List<FieldInfo> excludeFields;

}
