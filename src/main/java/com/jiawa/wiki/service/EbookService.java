package com.jiawa.wiki.service;

import com.jiawa.wiki.domain.Ebook;
import com.jiawa.wiki.domain.EbookExample;
import com.jiawa.wiki.mapper.EbookMapper;
import com.jiawa.wiki.req.EbookReq;
import com.jiawa.wiki.resp.EbookResp;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
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
        criteria.andNameLike("%" + req.getName() + "%");
        List<Ebook> ebookList = ebookMapper.selectByExample(ebookExample);

        List<EbookResp> respList = new ArrayList<>();
        for (Ebook ebook : ebookList) {
            EbookResp ebookResp = new EbookResp();
//            ebookResp.setId(ebook.getId());
            //Spring自带的工具类从 ebook 拷贝到 ebookResp,不需要一个一个的 setId(ebook.getId()); ....
            BeanUtils.copyProperties(ebook, ebookResp);
            //现在ebookResp有值了
            respList.add(ebookResp);
        }
        return respList;
    }
}
