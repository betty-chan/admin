package cn.exrick.xboot.modules.base.controller.common;

import cn.exrick.xboot.common.redis.RedisTemplateHelper;
import cn.exrick.xboot.common.utils.CreateVerifyCode;
import cn.exrick.xboot.common.utils.ResultUtil;
import cn.exrick.xboot.common.utils.emailUtil;
import cn.exrick.xboot.common.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 
 */
@Api(tags = "验证码接口")
@RequestMapping("/xboot/common/captcha")
@RestController
@Transactional
public class CaptchaController {

    @Autowired
    private RedisTemplateHelper redisTemplate;

    @RequestMapping(value = "/init", method = RequestMethod.GET)
    @ApiOperation(value = "初始化验证码")
    public Result<Object> initCaptcha() {

        String captchaId = UUID.randomUUID().toString().replace("-", "");
        String code = new CreateVerifyCode().randomStr(4);
        // 缓存验证码
        redisTemplate.set(captchaId, code, 2L, TimeUnit.MINUTES);
        return ResultUtil.data(captchaId);
    }

    @RequestMapping(value = "/draw/{captchaId}", method = RequestMethod.GET)
    @ApiOperation(value = "根据验证码ID获取图片")
    public void drawCaptcha(@PathVariable("captchaId") String captchaId, HttpServletResponse response) throws IOException {

        // 得到验证码 生成指定验证码
        String code = redisTemplate.get(captchaId);
        CreateVerifyCode vCode = new CreateVerifyCode(116, 36, 4, 10, code);
        response.setContentType("image/png");
        vCode.write(response.getOutputStream());
    }

    @RequestMapping(value = "/send", method = RequestMethod.GET)
    @ApiOperation(value = "邮件发送验证码")
    public Result<Object> sendCaptcha (@RequestParam String username) throws Exception {
        String code = new CreateVerifyCode().randomStr(4);
        // 缓存验证码
        redisTemplate.set(username, code, 2L, TimeUnit.MINUTES);
        emailUtil.sendEmail(username,"找回密码",code);
        return ResultUtil.data("true");
    }
}
