package com.jiawa.wiki.controller;

import com.jiawa.wiki.req.DocQueryReq;
import com.jiawa.wiki.req.DocSaveReq;
import com.jiawa.wiki.resp.DocQueryResp;
import com.jiawa.wiki.resp.CommonResp;
import com.jiawa.wiki.resp.PageResp;
import com.jiawa.wiki.service.DocService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;


@RestController //返回字符串
// @Controller  // 返回页面
@RequestMapping("/doc")
public class DocController {

    @Resource
    private DocService docService;

    @GetMapping("/all")
    public CommonResp all() { // 开启校验规则
        CommonResp<List<DocQueryResp>> resp = new CommonResp<>();
        List<DocQueryResp> list = docService.all();
        resp.setContent(list);
        return resp;
    }
    @GetMapping("/list")
    public CommonResp list(@Valid DocQueryReq req) { // 开启校验规则
        CommonResp<PageResp<DocQueryResp>> resp = new CommonResp<>();
        PageResp<DocQueryResp> list = docService.list(req);
        resp.setContent(list);
        return resp;
    }

    @PostMapping("/save")                   //一般保存类，更新类都用postMapping
    public CommonResp save(@Valid @RequestBody DocSaveReq req) { //在使用axios提交post请求时，由于以json方式提交所以需要增加@RequestBody注解
        CommonResp resp = new CommonResp<>();
        docService.save(req);
        return resp;
    }


    @DeleteMapping("/delete/{idsStr}")                     //resful风格
    public CommonResp delete(@PathVariable String idsStr) {//在使用axios提交post请求时，由于以json方式提交所以需要增加@RequestBody注解
        CommonResp resp = new CommonResp<>();
        List<String> list = Arrays.asList(idsStr.split(","));//将string转成list数组
        docService.delete(list);
        return resp;
    }
}
