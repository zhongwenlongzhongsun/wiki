package com.jiawa.wiki.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jiawa.wiki.domain.User;
import com.jiawa.wiki.domain.UserExample;
import com.jiawa.wiki.exception.BusinessException;
import com.jiawa.wiki.exception.BusinessExceptionCode;
import com.jiawa.wiki.mapper.UserMapper;
import com.jiawa.wiki.req.UserQueryReq;
import com.jiawa.wiki.req.UserSaveReq;
import com.jiawa.wiki.resp.PageResp;
import com.jiawa.wiki.resp.UserQueryResp;
import com.jiawa.wiki.util.CopyUtil;
import com.jiawa.wiki.util.SnowFlake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserService {

    private static final Logger LOG = LoggerFactory.getLogger((UserService.class));

//    @Autowired
    @Resource
    private UserMapper userMapper;

    @Resource
    private SnowFlake snowFlake;

    /**
     *     根据名字模糊查询
     */
    public PageResp<UserQueryResp> list(UserQueryReq req){

        UserExample userExample = new UserExample();
        //createCriteria() 相当于where 条件
        UserExample.Criteria criteria = userExample.createCriteria();
        //如果传进name字段就按照name查找,否则模糊查询
        if(!ObjectUtils.isEmpty(req.getLoginName())) {
            criteria.andLoginNameEqualTo(req.getLoginName());
        }

        PageHelper.startPage(req.getPage(), req.getSize()); //分页 （从查找的第几页开始，每页查询的条目数）
        List<User> userList = userMapper.selectByExample(userExample);
        //泛型对应列表实际的类
        PageInfo<User> pageInfo = new PageInfo<>(userList);
        //日志中使用点占位符{}, 而不是用 + 凭借着字符串
        //点占位符输出 -> 总行数: 5       错误使用 + 拼接  ->   总行数: {}5
        LOG.info("总行数: {}" , pageInfo.getTotal()); // 获取总行数
        LOG.info("总页数: {}" , pageInfo.getPages()); // 获取总页数

//        List<UserResp> respList = new ArrayList<>();
//        for (User user : userList) {
////            UserResp userResp = new UserResp();
//////            userResp.setId(user.getId());
////            //Spring自带的工具类从 user 拷贝到 userResp,不需要一个一个的 setId(user.getId()); ....
////            BeanUtils.copyProperties(user, userResp);

//            // 对象复制                           源    -> 目标类
//            UserResp userResp = CopyUtil.copy(user, UserResp.class);
//            //现在userResp有值了
//            respList.add(userResp);
//        }

        //列表复制
        List<UserQueryResp> list = CopyUtil.copyList(userList, UserQueryResp.class);

        PageResp<UserQueryResp> pageResp = new PageResp<>();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(list);

        return pageResp;
    }

    /**
     *  保存
     */

    public void save(UserSaveReq req){
        User user = CopyUtil.copy(req, User.class);
        if(ObjectUtils.isEmpty(req.getId())){
            User userDB = selectByLoginName(req.getLoginName());
            if(ObjectUtils.isEmpty(userDB)) {
                //新增
                user.setId(snowFlake.nextId());
                userMapper.insert(user);
            }else{
                //用户名已存在
                throw new BusinessException(BusinessExceptionCode.USER_LOGIN_NAME_EXIST);
            }
        }else{
            //更新 (修改时不更新用户名)
            user.setLoginName(null);
            userMapper.updateByPrimaryKeySelective(user);//里面属性有之才去更新，没值不更新
        }
    }

    /**
     * 删除
     */
    public void delete(Long id){
        userMapper.deleteByPrimaryKey(id);
    }

    public User selectByLoginName(String LoginName){
        UserExample userExample = new UserExample();
        //createCriteria() 相当于where 条件
        UserExample.Criteria criteria = userExample.createCriteria();
        //如果传进name字段就按照name查找,否则模糊查询
        criteria.andLoginNameEqualTo(LoginName);

        List<User> userList = userMapper.selectByExample(userExample); //mybatis只能用list接收
        if(CollectionUtils.isEmpty(userList)){
            return null;
        }else{
            return userList.get(0);
        }
    }

}
