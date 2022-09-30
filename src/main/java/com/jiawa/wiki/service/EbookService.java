package com.jiawa.wiki.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jiawa.wiki.domain.DocExample;
import com.jiawa.wiki.domain.Ebook;
import com.jiawa.wiki.domain.EbookExample;
import com.jiawa.wiki.mapper.DocMapper;
import com.jiawa.wiki.mapper.EbookMapper;
import com.jiawa.wiki.req.EbookQueryReq;
import com.jiawa.wiki.req.EbookSaveReq;
import com.jiawa.wiki.resp.EbookQueryResp;
import com.jiawa.wiki.resp.PageResp;
import com.jiawa.wiki.util.CopyUtil;
import com.jiawa.wiki.util.SnowFlake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.List;

@Service
public class EbookService {

    private static final Logger LOG = LoggerFactory.getLogger((EbookService.class));

//    @Autowired
    @Resource
    private EbookMapper ebookMapper;

    @Resource
    private SnowFlake snowFlake;

    /**
     *     根据名字模糊查询
     */
    public PageResp<EbookQueryResp> list(EbookQueryReq req){

        EbookExample ebookExample = new EbookExample();
        //createCriteria() 相当于where 条件
        EbookExample.Criteria criteria = ebookExample.createCriteria();
        //如果传进name字段就按照name查找,否则模糊查询
        if(!ObjectUtils.isEmpty(req.getName())) {
            criteria.andNameLike("%" + req.getName() + "%");
        }
        if(!ObjectUtils.isEmpty(req.getCategoryId2())) {  //long类型就不用加 %
            criteria.andCategory2IdEqualTo(req.getCategoryId2());
        }

        PageHelper.startPage(req.getPage(), req.getSize()); //分页 （从查找的第几页开始，每页查询的条目数）
        List<Ebook> ebookList = ebookMapper.selectByExample(ebookExample);
        //泛型对应列表实际的类
        PageInfo<Ebook> pageInfo = new PageInfo<>(ebookList);
        //日志中使用点占位符{}, 而不是用 + 凭借着字符串
        //点占位符输出 -> 总行数: 5       错误使用 + 拼接  ->   总行数: {}5
        LOG.info("总行数: {}" , pageInfo.getTotal()); // 获取总行数
        LOG.info("总页数: {}" , pageInfo.getPages()); // 获取总页数

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
        List<EbookQueryResp> list = CopyUtil.copyList(ebookList, EbookQueryResp.class);

        PageResp<EbookQueryResp> pageResp = new PageResp<>();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(list);

        return pageResp;
    }

    /**
     *  保存
     */

    public void save(EbookSaveReq req){
        Ebook ebook = CopyUtil.copy(req, Ebook.class);
        if(ObjectUtils.isEmpty(req.getId())){
            //新增
            ebook.setId(snowFlake.nextId());
            ebookMapper.insert(ebook);
        }else{
            //更新
            ebookMapper.updateByPrimaryKey(ebook);
        }
    }

    /**
     * 删除
     */
    public void delete(Long id){
        ebookMapper.deleteByPrimaryKey(id);
        DocExample docExample = new DocExample();//删除电子书时不同时删除关联文档
        DocExample.Criteria criteria = docExample.createCriteria();
        criteria.andEbookIdEqualTo(id);
        DocMapper.deleteByExample(docExample);
//        DocMapper.deleteByExample(content);

    }

}
