package com.yang.system.support.controller;

import com.yang.system.support.resp.RequestPage;
import com.yang.system.support.resp.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import top.sanguohf.egg.param.EntityParams;
import top.sanguohf.top.bootcon.page.Page;
import top.sanguohf.top.bootcon.resp.CommonPageResp;
import top.sanguohf.top.bootcon.service.CommonService;

import java.util.List;

/**
 * @project data-parent
 * @Date 2021/2/7
 * @Auth yangrui
 **/
@Controller
@RequestMapping("common")
public class CommonController {

    @Autowired
    CommonService commonService;

    @ResponseBody
    @PostMapping("/findList")
    public ResponseResult findList(@RequestBody EntityParams entityParams){
        List entityList = commonService.findList(entityParams);
        return ResponseResult.success(entityList);
    }

    @ResponseBody
    @PostMapping("/findPageList")
    public ResponseResult findPageList(@RequestBody RequestPage<EntityParams> entityParams){
        CommonPageResp pageList = commonService.findPageList(entityParams.getData(), new Page(entityParams.getPage(), entityParams.getSize()));
        return ResponseResult.success(pageList);
    }

    @ResponseBody
    @PostMapping("insert")
    public ResponseResult insert(@RequestBody EntityParams entityParams){
        commonService.insert(entityParams);
        return ResponseResult.success();
    }

    @ResponseBody
    @PostMapping("batchInsert")
    public ResponseResult batchInsert(@RequestBody List<EntityParams> entityParams){
        commonService.batchInsert(entityParams);
        return ResponseResult.success();
    }

    @ResponseBody
    @PostMapping("update")
    public ResponseResult update(@RequestBody EntityParams entityParams){
        commonService.update(entityParams);
        return ResponseResult.success();
    }

    @ResponseBody
    @PostMapping("batchUpdate")
    public ResponseResult update(@RequestBody List<EntityParams> entityParams){
        commonService.batchUpdate(entityParams);
        return ResponseResult.success();
    }

    @ResponseBody
    @PostMapping("delete")
    public ResponseResult delete(@RequestBody EntityParams entityParams){
        commonService.delete(entityParams);
        return ResponseResult.success();
    }

    @ResponseBody
    @PostMapping("batchDelete")
    public ResponseResult batchDelete(@RequestBody List<EntityParams> entityParams){
        commonService.batchDelete(entityParams);
        return ResponseResult.success();
    }

}
