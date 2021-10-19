package com.jiawa.wiki.controller;

import com.jiawa.wiki.req.CategoryQueryReq;
import com.jiawa.wiki.req.CategorySaveReq;
import com.jiawa.wiki.resp.CategoryQueryResp;
import com.jiawa.wiki.resp.CommonResp;
import com.jiawa.wiki.resp.PageResp;
import com.jiawa.wiki.service.CategoryService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;


@RestController //返回字符串
// @Controller  // 返回页面
@RequestMapping("/category")
public class CategoryController {

    @Resource
    private CategoryService categoryService;

    @GetMapping("/list")
    public CommonResp list(@Valid CategoryQueryReq req) { // 开启校验规则
        CommonResp<PageResp<CategoryQueryResp>> resp = new CommonResp<>();
        PageResp<CategoryQueryResp> list = categoryService.list(req);
        resp.setContent(list);
        return resp;
    }

    @PostMapping("/save")                   //一般保存类，更新类都用postMapping
    public CommonResp save(@Valid @RequestBody CategorySaveReq req) { //在使用axios提交post请求时，由于以json方式提交所以需要增加@RequestBody注解
        CommonResp resp = new CommonResp<>();
        categoryService.save(req);
        return resp;
    }

    @DeleteMapping("/delete/{id}")                   //resful风格
    public CommonResp delete(@PathVariable Long id) { //在使用axios提交post请求时，由于以json方式提交所以需要增加@RequestBody注解
        CommonResp resp = new CommonResp<>();
        categoryService.delete(id);
        return resp;
    }
}
