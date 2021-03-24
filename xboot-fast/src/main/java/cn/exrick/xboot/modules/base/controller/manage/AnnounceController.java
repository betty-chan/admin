package cn.exrick.xboot.modules.base.controller.manage;

import cn.exrick.xboot.common.utils.ResultUtil;
import cn.exrick.xboot.common.vo.Result;
import cn.exrick.xboot.modules.base.service.AnnounceService;
import cn.exrick.xboot.modules.base.entity.Announce;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author chenxin
 */
@Api(tags = "公告接口")
@RestController
@RequestMapping("/xboot/announce")
public class AnnounceController {

    @Autowired
    private AnnounceService announceService;

    @RequestMapping(value = "/get", method = RequestMethod.GET)
    @ApiOperation(value = "获取公告")
    public Result<List<Announce>> get(){
        List<Announce> list;
        list = announceService.get();
        return new ResultUtil<List<Announce>>().setData(list);
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ApiOperation(value = "更新")
    public Result<String> save(Announce announce){
        announceService.update(announce);
        return ResultUtil.success("编辑成功");
    }
}
