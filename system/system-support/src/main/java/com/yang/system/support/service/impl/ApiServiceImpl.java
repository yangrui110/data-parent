package com.yang.system.support.service.impl;

import com.yang.system.client.entity.Api;
import com.yang.system.support.dao.ApiDao;
import com.yang.system.support.service.ApiService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yangrui
 * @since 2021-02-14
 */
@Service
public class ApiServiceImpl extends ServiceImpl<ApiDao, Api> implements ApiService {

}
