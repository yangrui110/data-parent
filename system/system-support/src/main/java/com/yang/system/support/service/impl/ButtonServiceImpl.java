package com.yang.system.support.service.impl;

import com.yang.system.client.entity.Button;
import com.yang.system.support.dao.ButtonDao;
import com.yang.system.support.service.ButtonService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 按钮信息表 服务实现类
 * </p>
 *
 * @author yangrui
 * @since 2021-02-09
 */
@Service
public class ButtonServiceImpl extends ServiceImpl<ButtonDao, Button> implements ButtonService {

}
