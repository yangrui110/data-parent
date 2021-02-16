package com.yang.system.support.controller;


import com.yang.system.client.entity.MenuApi;
import com.yang.system.client.resp.PageResult;
import com.yang.system.client.vo.MenuApiVo;
import com.yang.system.support.resp.RequestPage;
import com.yang.system.support.resp.ResponseResult;
import com.yang.system.support.service.MenuApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * <p>
 * 菜单对应的接口 前端控制器
 * </p>
 *
 * @author yangrui
 * @since 2021-02-14
 */
@Controller
@RequestMapping("/menuApi")
public class MenuApiController {

    @Autowired
    private MenuApiService menuApiService;

    /**
     * 获取到菜单对应的Api数据
     * */
    @ResponseBody
    @PostMapping("getMenuApis")
    public ResponseResult getMenuApis(@RequestBody RequestPage<MenuApiVo> menuApiVo){
        PageResult<MenuApiVo> menuApis = menuApiService.getMenuApis(menuApiVo);
        return ResponseResult.success(menuApis);
    }

    @ResponseBody
    @PostMapping("add")
    public ResponseResult add(@RequestBody MenuApiVo menuApiVo){
        menuApiService.add(menuApiVo);
        return ResponseResult.success();
    }

    @ResponseBody
    @PostMapping("update")
    public ResponseResult update(@RequestBody MenuApiVo menuApiVo){
        menuApiService.update(menuApiVo);
        return ResponseResult.success();
    }

    @ResponseBody
    @PostMapping("delete")
    public ResponseResult delete(@RequestBody MenuApi menuApi){
        menuApiService.delete(menuApi);
        return ResponseResult.success();
    }

    @ResponseBody
    @PostMapping("batchDelete")
    public ResponseResult batchDelete(@RequestBody List<MenuApi> menuApis){
        menuApiService.batchDelete(menuApis);
        return ResponseResult.success();
    }
}
