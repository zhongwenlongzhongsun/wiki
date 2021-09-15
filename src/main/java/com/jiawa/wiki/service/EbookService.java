package com.jiawa.wiki.service;

import com.jiawa.wiki.domain.Ebook;
import com.jiawa.wiki.domain.EbookExample;
import com.jiawa.wiki.mapper.EbookMapper;
import com.jiawa.wiki.req.EbookReq;
import com.jiawa.wiki.resp.EbookResp;
import com.jiawa.wiki.util.CopyUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.List;

@Service
public class EbookService {

//    @Autowired
    @Resource
    private EbookMapper ebookMapper;

    //根据名字模糊查询
    public List<EbookResp> list(EbookReq req){
        EbookExample ebookExample = new EbookExample();
        //createCriteria() 相当于where 条件
        EbookExample.Criteria criteria = ebookExample.createCriteria();
        //如果传进name字段就按照name查找,否则模糊查询
        if(!ObjectUtils.isEmpty(req.getName())) {
            criteria.andNameLike("%" + req.getName() + "%");
        }
        List<Ebook> ebookList = ebookMapper.selectByExample(ebookExample);

//        List<EbookResp> respList = new ArrayList<>();
//        for (Ebook ebook : ebookList) {
////            EbookResp ebookResp = new EbookResp();
//////            ebookResp.setId(ebook.getId());
////            //Spring自带的工具类从 ebook 拷贝到 ebookResp,不需要一个一个的 setId(ebook.getId()); ....
////            BeanUtils.copyProperties(ebook, ebookResp);

//            // 对象复制                           源    -> 目标类
//            EbookResp ebookResp = CopyUtil.copy(ebook, EbookResp.class);
//            //现在ebookResp有值了
//            respList.add(ebookResp);
//        }

        //列表复制
        List<EbookResp> list = CopyUtil.copyList(ebookList, EbookResp.class);
        return list;
    }
}
