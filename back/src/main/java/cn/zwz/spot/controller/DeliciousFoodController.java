package cn.zwz.spot.controller;

import cn.zwz.basics.utils.PageUtil;
import cn.zwz.basics.utils.ResultUtil;
import cn.zwz.basics.baseVo.PageVo;
import cn.zwz.basics.baseVo.Result;
import cn.zwz.basics.utils.SecurityUtil;
import cn.zwz.data.entity.User;
import cn.zwz.data.utils.ZwzNullUtils;
import cn.zwz.spot.entity.Collection;
import cn.zwz.spot.entity.DeliciousFood;
import cn.zwz.spot.entity.ScenicSpot;
import cn.zwz.spot.entity.Trip;
import cn.zwz.spot.service.IDeliciousFoodService;
import cn.hutool.core.util.StrUtil;
import cn.zwz.spot.service.IScenicSpotService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author 郑为中
 * CSDN: Designer 小郑
 */
@Slf4j
@RestController
@Api(tags = "美食管理接口")
@RequestMapping("/zwz/deliciousFood")
@Transactional
public class DeliciousFoodController {

    @Autowired
    private IDeliciousFoodService iDeliciousFoodService;

    @Autowired
    private IScenicSpotService iScenicSpotService;

    @Autowired
    private SecurityUtil securityUtil;

    @RequestMapping(value = "/getAllOnWeb", method = RequestMethod.GET)
    @ApiOperation(value = "查询全部景点美食")
    public Result<List<DeliciousFood>> getAllOnWeb(@RequestParam String title) {
        QueryWrapper<DeliciousFood> qw = new QueryWrapper<>();
        if(!ZwzNullUtils.isNull(title)) {
            qw.like("spot_name",title);
        }
        return new ResultUtil<List<DeliciousFood>>().setData(iDeliciousFoodService.list(qw));
    }

    @RequestMapping(value = "/getOne", method = RequestMethod.GET)
    @ApiOperation(value = "查询单条美食")
    public Result<DeliciousFood> get(@RequestParam String id){
        return new ResultUtil<DeliciousFood>().setData(iDeliciousFoodService.getById(id));
    }

    @RequestMapping(value = "/count", method = RequestMethod.GET)
    @ApiOperation(value = "查询全部美食个数")
    public Result<Long> getCount(){
        return new ResultUtil<Long>().setData(iDeliciousFoodService.count());
    }

    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    @ApiOperation(value = "查询全部美食")
    public Result<List<DeliciousFood>> getAll(){
        return new ResultUtil<List<DeliciousFood>>().setData(iDeliciousFoodService.list());
    }

    @RequestMapping(value = "/getByAllOnUser", method = RequestMethod.GET)
    @ApiOperation(value = "查询全部收藏")
    public Result<List<DeliciousFood>> getByAllOnUser(){
        User currUser = securityUtil.getCurrUser();
        QueryWrapper<DeliciousFood> qw = new QueryWrapper<>();
        qw.eq("user_id",currUser.getId());
        return new ResultUtil<List<DeliciousFood>>().setData(iDeliciousFoodService.list(qw));
    }

    @RequestMapping(value = "/getByPage", method = RequestMethod.GET)
    @ApiOperation(value = "查询美食")
    public Result<IPage<DeliciousFood>> getByPage(@ModelAttribute DeliciousFood deliciousFood ,@ModelAttribute PageVo page){
        QueryWrapper<DeliciousFood> qw = new QueryWrapper<>();
        if(!ZwzNullUtils.isNull(deliciousFood.getTitle())) {
            qw.like("title",deliciousFood.getTitle());
        }
        if(!ZwzNullUtils.isNull(deliciousFood.getContent())) {
            qw.like("content",deliciousFood.getContent());
        }
        if(!ZwzNullUtils.isNull(deliciousFood.getSpotId())) {
            qw.eq("spot_id",deliciousFood.getSpotId());
        }
        IPage<DeliciousFood> data = iDeliciousFoodService.page(PageUtil.initMpPage(page),qw);
        return new ResultUtil<IPage<DeliciousFood>>().setData(data);
    }

    @RequestMapping(value = "/insertOrUpdate", method = RequestMethod.POST)
    @ApiOperation(value = "增改美食")
    public Result<DeliciousFood> saveOrUpdate(DeliciousFood deliciousFood){
        if(iDeliciousFoodService.saveOrUpdate(deliciousFood)){
            return new ResultUtil<DeliciousFood>().setData(deliciousFood);
        }
        return ResultUtil.error();
    }

    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    @ApiOperation(value = "新增美食")
    public Result<DeliciousFood> insert(DeliciousFood deliciousFood){
        ScenicSpot ss = iScenicSpotService.getById(deliciousFood.getSpotId());
        if(ss == null) {
            return ResultUtil.error("景点不存在");
        }
        deliciousFood.setSpotName(ss.getTitle());
        iDeliciousFoodService.saveOrUpdate(deliciousFood);
        return new ResultUtil<DeliciousFood>().setData(deliciousFood);
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ApiOperation(value = "编辑美食")
    public Result<DeliciousFood> update(DeliciousFood deliciousFood){
        ScenicSpot ss = iScenicSpotService.getById(deliciousFood.getSpotId());
        if(ss == null) {
            return ResultUtil.error("景点不存在");
        }
        deliciousFood.setSpotName(ss.getTitle());
        iDeliciousFoodService.saveOrUpdate(deliciousFood);
        return new ResultUtil<DeliciousFood>().setData(deliciousFood);
    }

    @RequestMapping(value = "/delByIds", method = RequestMethod.POST)
    @ApiOperation(value = "删除美食")
    public Result<Object> delByIds(@RequestParam String[] ids){
        for(String id : ids){
            iDeliciousFoodService.removeById(id);
        }
        return ResultUtil.success();
    }
}
