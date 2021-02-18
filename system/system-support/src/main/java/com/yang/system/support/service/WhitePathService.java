package com.yang.system.support.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yang.system.client.entity.WhitePath;
import com.yang.system.client.resp.PageResult;
import com.yang.system.support.resp.RequestPage;

import java.util.List;

/**
 * <p>
 * 路径访问白名单 服务类
 * </p>
 *
 * @author yangrui
 * @since 2021-02-18
 */
public interface WhitePathService extends IService<WhitePath> {

    PageResult<WhitePath> pageList(RequestPage<WhitePath> requestPage);
    /**
     * 查询所有的白名单路径
     * */
    List<WhitePath> listWhitePath();
    /**
     * 新增白名单路径
     * */
    List<WhitePath> addWhitePath(WhitePath whitePath);
    /**
     * 修改白名单路径
     * */
    List<WhitePath> updateWhitePath(WhitePath whitePath);
    /**
     * 批量更新
     * */
    List<WhitePath> batchUpdateWhitePath(List<WhitePath> whitePath);
    /**
     * 逻辑删除白名单路径
     * */
    List<WhitePath> logicalDelete(WhitePath whitePath);
}
