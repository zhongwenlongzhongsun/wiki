package com.jiawa.wiki.controller;

import com.jiawa.wiki.req.EbookQueryReq;
import com.jiawa.wiki.req.EbookSaveReq;
import com.jiawa.wiki.resp.CommonResp;
import com.jiawa.wiki.resp.EbookQueryResp;
import com.jiawa.wiki.resp.PageResp;
import com.jiawa.wiki.service.EbookService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;


@RestController //返回字符串
// @Controller  // 返回页面
@RequestMapping("/ebook")
public class EbookController {

    @Resource
    private EbookService ebookService;

    @GetMapping("/list")
    public CommonResp list(@Valid  EbookQueryReq req){ // 开启校验规则
        CommonResp<PageResp<EbookQueryResp>> resp = new CommonResp<>();
        PageResp<EbookQueryResp> list = ebookService.list(req);
        resp.setContent(list);
        return resp;
    }

    @PostMapping("/save")                   //一般保存类，更新类都用postMapping
    public CommonResp save(@RequestBody EbookSaveReq req){ //在使用axios提交post请求时，由于以json方式提交所以需要增加@RequestBody注解
        CommonResp resp = new CommonResp<>();
        ebookService.save(req);
        return resp;
    }

    @DeleteMapping("/delete/{id}")                   //resful风格
    public CommonResp delete(@PathVariable Long id){ //在使用axios提交post请求时，由于以json方式提交所以需要增加@RequestBody注解
        CommonResp resp = new CommonResp<>();
        ebookService.delete(id);
        return resp;
    }
}
