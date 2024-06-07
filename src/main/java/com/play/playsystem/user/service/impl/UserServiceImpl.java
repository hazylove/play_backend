package com.play.playsystem.user.service.impl;

import cn.hutool.core.io.FileUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.play.playsystem.basic.constant.AuthConstant;
import com.play.playsystem.basic.utils.tool.FormatCheckUtil;
import com.play.playsystem.basic.utils.tool.MyFileUtil;
import com.play.playsystem.basic.utils.tool.RedisUtil;
import com.play.playsystem.basic.utils.result.JsonResult;
import com.play.playsystem.basic.utils.result.ResultCode;
import com.play.playsystem.file.domain.entity.UploadFile;
import com.play.playsystem.file.service.IUploadFileService;
import com.play.playsystem.user.domain.vo.UserCreatedVo;
import com.play.playsystem.user.domain.vo.UserDetailsVo;
import com.play.playsystem.user.service.IUserService;
import com.play.playsystem.user.domain.dto.ChangePasswordDto;
import com.play.playsystem.user.domain.dto.UserRegistrationDto;
import com.play.playsystem.user.domain.entity.User;
import com.play.playsystem.user.mapper.UserMapper;
import com.play.playsystem.user.utils.PasswordEncoder;
import com.play.playsystem.user.utils.UserCheckUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Objects;

@Slf4j
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserCheckUtil userCheckUtil;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private IUploadFileService fileService;

    @Value("${file.avatar.max-size}")
    private long avatarMaxSize;

    // 默认头像
    private final String DEFAULT_AVATAR = AuthConstant.AVATAR_PATH + "default.png";


    @Override
    @Transactional(rollbackFor = Exception.class)
    public JsonResult register(UserRegistrationDto userRegistrationDto) {
        JsonResult jsonResult = new JsonResult();
        // 校验用户名格式
        if (UserCheckUtil.isInvalidUsername(userRegistrationDto.getUsername()) && FormatCheckUtil.validateEmail(userRegistrationDto.getUsername())) {
            return jsonResult.setSuccess(false).setCode(ResultCode.USERNAME_FORMAT_ERROR).setMassage("用户名格式不正确");
        }
        // 校验手机号格式
        if (userRegistrationDto.getPhone() !=  null && !userRegistrationDto.getPhone().isEmpty() && !FormatCheckUtil.validatePhone(userRegistrationDto.getPhone())) {
            return jsonResult.setSuccess(false).setCode(ResultCode.PHONE_FORMAT_ERROR).setMassage("手机号格式不正确");
        }
        // 检查两次输入密码
        if (!Objects.equals(userRegistrationDto.getPassword1(), userRegistrationDto.getPassword2())){
            return jsonResult.setSuccess(false).setCode(ResultCode.TWICE_PASSWORD_INCONSISTENT).setMassage("两次输入密码不一致");
        }
        // 检查用户名是否存在
        if (registerUsernameExist(userRegistrationDto.getUsername())){
            return jsonResult.setSuccess(false).setCode(ResultCode.USERNAME_EXISTING).setMassage("用户名已存在");
        }
        // 检查邮箱是否已注册
        if (registerEmailExist(userRegistrationDto.getEmail())) {
            return new JsonResult().setCode(ResultCode.EMAIL_EXISTING).setSuccess(false).setMassage("该邮箱已注册");
        }
        // 校验邮箱验证码
        if (userCheckUtil.isInvalidEmailCode(userRegistrationDto.getEmail(), userRegistrationDto.getEmailCode())) {
            return jsonResult.setSuccess(false).setCode(ResultCode.EMAIL_CODE_ERROR).setMassage("邮箱验证码错误");
        }
        // 创建用户
        User user = new User(
                null, // 自增id
                userRegistrationDto.getUsername(), //用户名
                passwordEncoder.encode(userRegistrationDto.getPassword1()), // 加密后的密码
                userRegistrationDto.getNickname(), // 昵称
                DEFAULT_AVATAR, // 默认头像
                userRegistrationDto.getEmail(), // 邮箱
                userRegistrationDto.getPhone(), // 电话
                LocalDateTime.now(), // 注册时间
                null,
                null,
                null,
                null
        );
        userMapper.insert(user);

        return jsonResult.setMassage("注册成功");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public JsonResult changePassword(ChangePasswordDto changePasswordDto) {
        JsonResult jsonResult = new JsonResult();
        String email = (String) getOneFieldValueByUserId(changePasswordDto.getId(), User::getEmail);
        // 检查两次输入密码
        if (!Objects.equals(changePasswordDto.getNewPassword1(), changePasswordDto.getNewPassword2())){
            return jsonResult.setSuccess(false).setCode(ResultCode.TWICE_PASSWORD_INCONSISTENT).setMassage("两次输入密码不一致");
        }
        // 校验邮箱验证码
        if (userCheckUtil.isInvalidEmailCode(email, changePasswordDto.getEmailCode())) {
            return jsonResult.setSuccess(false).setCode(ResultCode.EMAIL_CODE_ERROR).setMassage("邮箱验证码错误");
        }
        // 修改密码
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda()
                .set(User::getPassword, passwordEncoder.encode(changePasswordDto.getNewPassword1()))
                .eq(User::getId, changePasswordDto.getId());
        userMapper.update(null, updateWrapper);

        // 删除Redis中存储的token
        String username = (String) getOneFieldValueByUserId(changePasswordDto.getId(), User::getUsername);
        redisUtil.del(AuthConstant.TOKEN_REDIS_PREFIX + username, email);

        return jsonResult.setMassage("修改成功");
    }

    @Override
    public User getUserByUsername(String username) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(User::getUsername, username);
        return userMapper.selectOne(queryWrapper);
    }

    @Override
    public User getUserByUsernameOrEmail(String Account) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(User::getUsername, Account).or().eq(User::getEmail, Account);
        return userMapper.selectOne(queryWrapper);
    }

    @Override
    public boolean registerEmailExist(String email) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(User::getEmail, email);
        return userMapper.selectOne(queryWrapper) != null;
    }

    @Override
    public boolean registerUsernameExist(String username) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(User::getUsername, username);
        return userMapper.selectOne(queryWrapper) != null;
    }

    @Override
    public Object getOneFieldValueByUserId(Long userId, SFunction<User, String> fieldExtractor) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().select(fieldExtractor).eq(User::getId, userId);
        return userMapper.selectObjs(queryWrapper).stream().findFirst().orElse(null);
    }

    @Override
    public User getUserByEmail(String email) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(User::getEmail, email);
        return userMapper.selectOne(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public JsonResult changeAvatar(Long userId, MultipartFile avatarImage) throws IOException {
        JsonResult jsonResult = new JsonResult();

        // 检查文件大小
        if (avatarImage.getSize() > avatarMaxSize) {
            return jsonResult.setSuccess(false).setCode(ResultCode.AVATAR_SIZE_TOO_LARGE).setMassage("头像大小超出限制：" + avatarMaxSize / 1024 + "KB");
        }
        //根据文件扩展名得到文件类型
        String fileType = MyFileUtil.getFileType(FileUtil.extName(avatarImage.getOriginalFilename()));
        // 判断文件类型
        if (!fileType.equals("image")) {
            return jsonResult.setSuccess(false).setCode(ResultCode.AVATAR_TYPE_ERROR).setMassage("文件类型错误");
        }
        // 删除原头像数据及文件
        String oldAvatarUrl = (String) getOneFieldValueByUserId(userId, User::getAvatar);
        if (!oldAvatarUrl.equals(DEFAULT_AVATAR)){
            UploadFile oldAvatar = fileService.getUploadFileByUrl(oldAvatarUrl);
            File file = new File(oldAvatar.getPath());
            if (file.delete()) {
                fileService.deleteById(oldAvatar.getId());
            } else {
                log.error("头像文件删除失败：{}", oldAvatarUrl);
            }
        }
        // 保存新头像图片
        UploadFile uploadFile = fileService.uploadFile(userId, avatarImage, AuthConstant.AVATAR_PATH);
        // 修改头像
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda()
                .set(User::getAvatar, uploadFile.getUrl())
                .eq(User::getId, userId);
        userMapper.update(null, updateWrapper);

        return jsonResult;
    }

    @Override
    public UserDetailsVo getUserDetails(Long userId) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .select(
                        User::getId,
                        User::getNickname,
                        User::getAvatar,
                        User::getUsername,
                        User::getEmail,
                        User::getPhone,
                        User::getGender,
                        User::getAge,
                        User::getBirth,
                        User::getProfile
                )
                .eq(User::getId, userId);
        User user = userMapper.selectOne(queryWrapper);
        return new UserDetailsVo(user);
    }

    @Override
    public UserCreatedVo getUserCreatedVo(Long userId) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .select(
                        User::getId,
                        User::getNickname,
                        User::getAvatar
                )
                .eq(User::getId, userId);
        User user = userMapper.selectOne(queryWrapper);
        user.setAvatar(MyFileUtil.reSetFileUrl(user.getAvatar()));
        return new UserCreatedVo(user.getId(), user.getNickname(), user.getAvatar());
    }
}
