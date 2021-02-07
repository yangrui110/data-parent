package top.sanguohf.top.bootcon.cache;

import lombok.Data;

import java.util.List;

/**
 * @author 杨瑞
 **/
@Data
public class ViewClassInfo {

    private EntityClassInfo mainTable;

    private List<ReferEntityClassInfo> referTables;

}
