package com.yang.system.support.controller;


import com.yang.system.client.entity.WhitePath;
import com.yang.system.client.resp.PageResult;
import com.yang.system.support.constant.DrStatus;
import com.yang.system.support.resp.RequestPage;
import com.yang.system.support.resp.ResponseResult;
import com.yang.system.support.service.WhitePathService;
import com.yang.system.support.util.IdUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 路径访问白名单 前端控制器
 * </p>
 *
 * @author yangrui
 * @since 2021-02-18
 */
@Controller
@RequestMapping("/whitePath")
public class WhitePathController {

    @Autowired
    private WhitePathService whitePathService;

    /**
     * 用户列表
     * */
    @ResponseBody
    @PostMapping("pageList")
    public ResponseResult pageList(@RequestBody RequestPage<WhitePath> menuButton){
        PageResult<WhitePath> menuButtonPage = whitePathService.pageList(menuButton);
        return ResponseResult.success(menuButtonPage);
    }

    @ResponseBody
    @GetMapping("listWhitePath")
    public List<WhitePath> listWhitePath(){
        List<WhitePath> menuButtonPage = whitePathService.listWhitePath();
        return menuButtonPage;
    }


    @ResponseBody
    @DeleteMapping("batchDelete")
    public ResponseResult batchDelete(String ids){
        if(StringUtils.isEmpty(ids)) return ResponseResult.error();
        String[] split = ids.split(",");
        ArrayList<WhitePath> list = new ArrayList<>();
        for(String one: split){
            WhitePath menu = new WhitePath();
            menu.setId(Long.parseLong(one));
            menu.setDr(DrStatus.DEL);
            list.add(menu);
        }
        whitePathService.batchUpdateWhitePath(list);
        return ResponseResult.success();
    }

    @ResponseBody
    @DeleteMapping("delete")
    public ResponseResult delete(String id){
        if(StringUtils.isEmpty(id)) return ResponseResult.error();
        WhitePath menu = new WhitePath();
        menu.setId(Long.parseLong(id));
        menu.setDr(DrStatus.DEL);
        whitePathService.updateWhitePath(menu);
        return ResponseResult.success();
    }

    @ResponseBody
    @PostMapping("update")
    public ResponseResult update(@RequestBody WhitePath menu){
        whitePathService.updateWhitePath(menu);
        return ResponseResult.success();
    }

    @ResponseBody
    @PostMapping("add")
    public ResponseResult add(@RequestBody WhitePath menu){
        menu.setId(IdUtils.nextId());
        menu.setDr(DrStatus.NORMAL);
        whitePathService.addWhitePath(menu);
        return ResponseResult.success();
    }

}
