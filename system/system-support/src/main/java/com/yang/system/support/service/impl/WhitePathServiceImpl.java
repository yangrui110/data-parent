package com.yang.system.support.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yang.system.client.entity.WhitePath;
import com.yang.system.client.resp.PageResult;
import com.yang.system.support.constant.DrStatus;
import com.yang.system.support.constant.EnableStatus;
import com.yang.system.support.dao.WhitePathDao;
import com.yang.system.support.resp.RequestPage;
import com.yang.system.support.service.WhitePathService;
import com.yang.system.support.util.IdUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 路径访问白名单 服务实现类
 * </p>
 *
 * @author yangrui
 * @since 2021-02-18
 */
@Service
public class WhitePathServiceImpl extends ServiceImpl<WhitePathDao, WhitePath> implements WhitePathService {

    @Autowired
    WhitePathDao whitePathDao;

    @Override
    public PageResult<WhitePath> pageList(RequestPage<WhitePath> requestPage) {
        QueryWrapper<WhitePath> queryWrapper = Wrappers.query(new WhitePath()).eq("dr", DrStatus.NORMAL);
        IPage<WhitePath> pageList = this.page(new Page(requestPage.getPage(), requestPage.getSize()), queryWrapper);
        PageResult<WhitePath> pageResult = new PageResult<>(pageList.getTotal(), pageList.getRecords());
        return pageResult;
    }

    @Cacheable(cacheNames = "white_path",key = "'whitePath'")
    @Override
    public List<WhitePath> listWhitePath() {
        List<WhitePath> list = this.list(Wrappers.query(new WhitePath()).eq("dr", DrStatus.NORMAL));
//        List<WhitePath> list = whitePathDao.listWhitePath();
        return list;
    }

    @CachePut(cacheNames = "white_path",key = "'whitePath'")
    @Transactional
    @Override
    public List<WhitePath> addWhitePath(WhitePath whitePath) {
        whitePath.setCreateTime(LocalDateTime.now());
        whitePath.setDr(DrStatus.NORMAL);
        whitePath.setEnable(EnableStatus.ENABLE);
        whitePath.setId(IdUtils.nextId());
        whitePath.setUpdateTime(LocalDateTime.now());
        this.save(whitePath);
        return listWhitePath();
    }
    @CachePut(cacheNames = "white_path",key = "'whitePath'")
    @Override
    public List<WhitePath> updateWhitePath(WhitePath whitePath) {
        this.updateById(whitePath);
        return listWhitePath();
    }

    @CachePut(cacheNames = "white_path",key = "'whitePath'")
    @Override
    public List<WhitePath> batchUpdateWhitePath(List<WhitePath> whitePath) {
        this.updateBatchById(whitePath);
        return null;
    }

    @CachePut(cacheNames = "white_path",key = "'whitePath'")
    @Override
    public List<WhitePath> logicalDelete(WhitePath whitePath) {
        WhitePath path = new WhitePath();
        path.setId(whitePath.getId());
        path.setDr(DrStatus.DEL);
        path.setUpdateTime(LocalDateTime.now());
        return listWhitePath();
    }
}
