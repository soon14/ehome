package cn.ehome.sso.service.impl;

import cn.ehome.common.util.EhomeResult;
import cn.ehome.mapper.TbUserMapper;
import cn.ehome.pojo.TbUser;
import cn.ehome.pojo.TbUserExample;
import cn.ehome.sso.service.RegisterService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.List;

/**
 * @author:Jun
 * @time:2019/4/7
 */
@Service
public class RegisterServiceImpl implements RegisterService {

    @Autowired
    private TbUserMapper userMapper;

    @Override
    public EhomeResult checkData(String param, int type) {
        //根据不同的type生成不同的查询条件
        TbUserExample example = new TbUserExample();
        TbUserExample.Criteria criteria = example.createCriteria();
        //1：用户名 2：手机号 3：邮箱
        if (type == 1) {
            criteria.andUsernameEqualTo(param);
        } else if (type == 2) {
            criteria.andPhoneEqualTo(param);
        } else if (type == 3) {
            criteria.andEmailEqualTo(param);
        } else {
            return EhomeResult.build(400, "数据类型错误");
        }
        //执行查询
        List<TbUser> list = userMapper.selectByExample(example);
        //判断结果中是否包含数据
        if (list != null && list.size()>0) {
            //如果有数据返回false
            return EhomeResult.ok(false);
        }
        //如果没有数据返回true
        return EhomeResult.ok(true);
    }

    @Override
    public EhomeResult register(TbUser user) {
        //数据有效性校验
        if (StringUtils.isBlank(user.getUsername()) || StringUtils.isBlank(user.getPassword())
                || StringUtils.isBlank(user.getPhone())) {
            return EhomeResult.build(400, "用户数据不完整，注册失败");
        }
        //1：用户名 2：手机号 3：邮箱
        EhomeResult result = checkData(user.getUsername(), 1);
        if (!(boolean) result.getData()) {
            return EhomeResult.build(400, "此用户名已经被占用");
        }
        user.setCreated(new Date());
        user.setUpdated(new Date());
        //对密码进行md5加密
        String md5Pass = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
        user.setPassword(md5Pass);
        //把用户数据插入到数据库中
        userMapper.insert(user);
        //返回添加成功
        return EhomeResult.ok();
    }
}
