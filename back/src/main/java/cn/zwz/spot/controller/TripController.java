package cn.zwz.spot.controller;

import cn.zwz.basics.utils.PageUtil;
import cn.zwz.basics.utils.ResultUtil;
import cn.zwz.basics.baseVo.PageVo;
import cn.zwz.basics.baseVo.Result;
import cn.zwz.data.utils.ZwzNullUtils;
import cn.zwz.spot.entity.ScenicSpot;
import cn.zwz.spot.entity.Trip;
import cn.zwz.spot.service.IScenicSpotService;
import cn.zwz.spot.service.ITripService;
import cn.hutool.core.util.StrUtil;
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
@Api(tags = "行程管理接口")
@RequestMapping("/zwz/trip")
@Transactional
public class TripController {

    @Autowired
    private ITripService iTripService;

    @Autowired
    private IScenicSpotService iScenicSpotService;

    @RequestMapping(value = "/getAllOnWeb", method = RequestMethod.GET)
    @ApiOperation(value = "查询全部景点")
    public Result<List<Trip>> getAllOnWeb(@RequestParam String title) {
        QueryWrapper<Trip> qw = new QueryWrapper<>();
        if(!ZwzNullUtils.isNull(title)) {
            qw.like("spot_name",title);
        }
        return new ResultUtil<List<Trip>>().setData(iTripService.list(qw));
    }

    @RequestMapping(value = "/getOne", method = RequestMethod.GET)
    @ApiOperation(value = "查询单条行程")
    public Result<Trip> get(@RequestParam String id){
        return new ResultUtil<Trip>().setData(iTripService.getById(id));
    }

    @RequestMapping(value = "/count", method = RequestMethod.GET)
    @ApiOperation(value = "查询全部行程个数")
    public Result<Long> getCount(){
        return new ResultUtil<Long>().setData(iTripService.count());
    }

    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    @ApiOperation(value = "查询全部行程")
    public Result<List<Trip>> getAll(){
        return new ResultUtil<List<Trip>>().setData(iTripService.list());
    }

    @RequestMapping(value = "/getByPage", method = RequestMethod.GET)
    @ApiOperation(value = "查询行程")
    public Result<IPage<Trip>> getByPage(@ModelAttribute Trip trip ,@ModelAttribute PageVo page){
        QueryWrapper<Trip> qw = new QueryWrapper<>();
        if(!ZwzNullUtils.isNull(trip.getTitle())) {
            qw.like("title",trip.getTitle());
        }
        if(!ZwzNullUtils.isNull(trip.getWay())) {
            qw.like("way",trip.getWay());
        }
        if(!ZwzNullUtils.isNull(trip.getSpotId())) {
            qw.eq("spot_id",trip.getSpotId());
        }
        IPage<Trip> data = iTripService.page(PageUtil.initMpPage(page),qw);
        return new ResultUtil<IPage<Trip>>().setData(data);
    }

    @RequestMapping(value = "/insertOrUpdate", method = RequestMethod.POST)
    @ApiOperation(value = "增改行程")
    public Result<Trip> saveOrUpdate(Trip trip){
        if(iTripService.saveOrUpdate(trip)){
            return new ResultUtil<Trip>().setData(trip);
        }
        return ResultUtil.error();
    }

    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    @ApiOperation(value = "新增行程")
    public Result<Trip> insert(Trip trip){
        ScenicSpot ss = iScenicSpotService.getById(trip.getSpotId());
        if(ss == null) {
            return ResultUtil.error("景点不存在");
        }
        trip.setSpotName(ss.getTitle());
        iTripService.saveOrUpdate(trip);
        return new ResultUtil<Trip>().setData(trip);
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ApiOperation(value = "编辑行程")
    public Result<Trip> update(Trip trip){
        ScenicSpot ss = iScenicSpotService.getById(trip.getSpotId());
        if(ss == null) {
            return ResultUtil.error("景点不存在");
        }
        trip.setSpotName(ss.getTitle());
        iTripService.saveOrUpdate(trip);
        return new ResultUtil<Trip>().setData(trip);
    }

    @RequestMapping(value = "/delByIds", method = RequestMethod.POST)
    @ApiOperation(value = "删除行程")
    public Result<Object> delByIds(@RequestParam String[] ids){
        for(String id : ids){
            iTripService.removeById(id);
        }
        return ResultUtil.success();
    }
}
