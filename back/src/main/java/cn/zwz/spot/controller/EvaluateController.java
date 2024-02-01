package cn.zwz.spot.controller;

import cn.hutool.core.date.DateUtil;
import cn.zwz.basics.utils.PageUtil;
import cn.zwz.basics.utils.ResultUtil;
import cn.zwz.basics.baseVo.PageVo;
import cn.zwz.basics.baseVo.Result;
import cn.zwz.basics.utils.SecurityUtil;
import cn.zwz.data.entity.User;
import cn.zwz.data.service.IUserService;
import cn.zwz.data.utils.ZwzNullUtils;
import cn.zwz.spot.entity.Evaluate;
import cn.zwz.spot.entity.ScenicSpot;
import cn.zwz.spot.service.IEvaluateService;
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

import java.math.BigDecimal;
import java.util.List;

/**
 * @author 郑为中
 * CSDN: Designer 小郑
 */
@Slf4j
@RestController
@Api(tags = "评价管理接口")
@RequestMapping("/zwz/evaluate")
@Transactional
public class EvaluateController {

    @Autowired
    private IEvaluateService iEvaluateService;

    @Autowired
    private IScenicSpotService iScenicSpotService;

    @Autowired
    private IUserService iUserService;

    @Autowired
    private SecurityUtil securityUtil;

    @RequestMapping(value = "/addEvaluate", method = RequestMethod.GET)
    @ApiOperation(value = "新增评价")
    public Result<Evaluate> addEvaluate(@RequestParam String id, @RequestParam BigDecimal level, @RequestParam String message){
        ScenicSpot ss = iScenicSpotService.getById(id);
        if(ss == null) {
            return ResultUtil.error("景点不存在");
        }
        User currUser = securityUtil.getCurrUser();
        QueryWrapper<Evaluate> qw = new QueryWrapper<>();
        qw.eq("spot_id",ss.getId());
        qw.eq("user_id",currUser.getId());
        qw.last("limit 1");
        Evaluate evaluate = iEvaluateService.getOne(qw);
        if(evaluate == null) {
            evaluate = new Evaluate();
            evaluate.setSpotId(ss.getId());
            evaluate.setSpotName(ss.getTitle());
            evaluate.setUserId(currUser.getId());
            evaluate.setUserName(currUser.getNickname());
        }
        evaluate.setLevel(level);
        evaluate.setMessage(message);
        evaluate.setTime(DateUtil.now());
        iEvaluateService.saveOrUpdate(evaluate);
        return ResultUtil.success();
    }

    @RequestMapping(value = "/getOne", method = RequestMethod.GET)
    @ApiOperation(value = "查询单条评价")
    public Result<Evaluate> get(@RequestParam String id){
        return new ResultUtil<Evaluate>().setData(iEvaluateService.getById(id));
    }

    @RequestMapping(value = "/count", method = RequestMethod.GET)
    @ApiOperation(value = "查询全部评价个数")
    public Result<Long> getCount(){
        return new ResultUtil<Long>().setData(iEvaluateService.count());
    }

    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    @ApiOperation(value = "查询全部评价")
    public Result<List<Evaluate>> getAll(){
        return new ResultUtil<List<Evaluate>>().setData(iEvaluateService.list());
    }

    @RequestMapping(value = "/getByPage", method = RequestMethod.GET)
    @ApiOperation(value = "查询评价")
    public Result<IPage<Evaluate>> getByPage(@ModelAttribute Evaluate evaluate ,@ModelAttribute PageVo page){
        QueryWrapper<Evaluate> qw = new QueryWrapper<>();
        User currUser = securityUtil.getCurrUser();
        QueryWrapper<User> userQw = new QueryWrapper<>();
        userQw.eq("id",currUser.getId());
        userQw.inSql("id","SELECT user_id FROM a_user_role WHERE del_flag = 0 AND role_id = '496138616573952'");
        if(iUserService.count(userQw) < 1L) {
            qw.eq("user_id",currUser.getId());
        }
        if(!ZwzNullUtils.isNull(evaluate.getSpotName())) {
            qw.like("spot_name",evaluate.getSpotName());
        }
        if(!ZwzNullUtils.isNull(evaluate.getUserName())) {
            qw.like("user_name",evaluate.getUserName());
        }
        IPage<Evaluate> data = iEvaluateService.page(PageUtil.initMpPage(page),qw);
        return new ResultUtil<IPage<Evaluate>>().setData(data);
    }

    @RequestMapping(value = "/insertOrUpdate", method = RequestMethod.POST)
    @ApiOperation(value = "增改评价")
    public Result<Evaluate> saveOrUpdate(Evaluate evaluate){
        if(iEvaluateService.saveOrUpdate(evaluate)){
            return new ResultUtil<Evaluate>().setData(evaluate);
        }
        return ResultUtil.error();
    }

    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    @ApiOperation(value = "新增评价")
    public Result<Evaluate> insert(Evaluate evaluate){
        iEvaluateService.saveOrUpdate(evaluate);
        return new ResultUtil<Evaluate>().setData(evaluate);
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ApiOperation(value = "编辑评价")
    public Result<Evaluate> update(Evaluate evaluate){
        iEvaluateService.saveOrUpdate(evaluate);
        return new ResultUtil<Evaluate>().setData(evaluate);
    }

    @RequestMapping(value = "/delByIds", method = RequestMethod.POST)
    @ApiOperation(value = "删除评价")
    public Result<Object> delByIds(@RequestParam String[] ids){
        for(String id : ids){
            iEvaluateService.removeById(id);
        }
        return ResultUtil.success();
    }
}
