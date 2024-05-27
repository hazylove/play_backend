package com.play.playsystem.user.service.impl;

import com.play.playsystem.user.service.IUserService;
import com.play.playsystem.user.domain.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

@Component
public class UserDetailsServiceImpl implements UserDetailsService {
    @Resource
    private IUserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userService.getUserByUsernameOrEmail(username);

        if (user == null) {
            throw new UsernameNotFoundException(username);
        } else {
            Collection<GrantedAuthority> authorities = getUserAuthority(user.getId());
            return new org.springframework.security.core.userdetails.User(
                    user.getUsername(),
                    user.getPassword(),
                    true, //用户是否启用
                    true, //用户账号是否未过期
                    true, //用户名凭证是否未过期
                    true, //用户是否未被锁定
                    authorities //权限列表
            );
        }
    }

    /**
     * 获取用户权限信息（角色、菜单权限）
     * @param userId 用户id
     * @return 用户权限信息
     */
    public List<GrantedAuthority> getUserAuthority(Long userId) {
        // 实际怎么写以数据表结构为准，这里只是写个例子
        // 角色(比如ROLE_admin)，菜单操作权限(比如sys:user:list)
//        String authority = userMapper.getUserAuthorityInfo(userId);     // 比如ROLE_admin,ROLE_normal,sys:user:list,...

        return AuthorityUtils.commaSeparatedStringToAuthorityList("");
    }
}
