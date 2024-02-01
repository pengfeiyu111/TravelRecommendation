package cn.zwz.spot.controller;

import cn.zwz.basics.utils.PageUtil;
import cn.zwz.basics.utils.ResultUtil;
import cn.zwz.basics.baseVo.PageVo;
import cn.zwz.basics.baseVo.Result;
import cn.zwz.data.utils.ZwzNullUtils;
import cn.zwz.spot.entity.ScenicSpotType;
import cn.zwz.spot.service.IScenicSpotTypeService;
import cn.hutool.core.util.StrUtil;
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
import java.util.Objects;

/**
 * @author 郑为中
 * CSDN: Designer 小郑
 */
@Slf4j
@RestController
@Api(tags = "景点类型管理接口")
@RequestMapping("/zwz/scenicSpotType")
@Transactional
public class ScenicSpotTypeController {

    @Autowired
    private IScenicSpotTypeService iScenicSpotTypeService;

    @RequestMapping(value = "/getOne", method = RequestMethod.GET)
    @ApiOperation(value = "查询单条景点类型")
    public Result<ScenicSpotType> get(@RequestParam String id){
        return new ResultUtil<ScenicSpotType>().setData(iScenicSpotTypeService.getById(id));
    }

    @RequestMapping(value = "/count", method = RequestMethod.GET)
    @ApiOperation(value = "查询全部景点类型个数")
    public Result<Long> getCount(){
        return new ResultUtil<Long>().setData(iScenicSpotTypeService.count());
    }

    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    @ApiOperation(value = "查询全部景点类型")
    public Result<List<ScenicSpotType>> getAll(){
        QueryWrapper<ScenicSpotType> qw = new QueryWrapper<>();
        qw.eq("status","正常");
        return new ResultUtil<List<ScenicSpotType>>().setData(iScenicSpotTypeService.list(qw));
    }

    @RequestMapping(value = "/getByPage", method = RequestMethod.GET)
    @ApiOperation(value = "查询景点类型")
    public Result<IPage<ScenicSpotType>> getByPage(@ModelAttribute ScenicSpotType scenicSpotType ,@ModelAttribute PageVo page){
        QueryWrapper<ScenicSpotType> qw = new QueryWrapper<>();
        if(!ZwzNullUtils.isNull(scenicSpotType.getTitle())) {
            qw.like("title",scenicSpotType.getTitle());
        }
        if(!ZwzNullUtils.isNull(scenicSpotType.getStatus())) {
            qw.eq("status",scenicSpotType.getStatus());
        }
        IPage<ScenicSpotType> data = iScenicSpotTypeService.page(PageUtil.initMpPage(page),qw);
        return new ResultUtil<IPage<ScenicSpotType>>().setData(data);
    }

    @RequestMapping(value = "/insertOrUpdate", method = RequestMethod.POST)
    @ApiOperation(value = "增改景点类型")
    public Result<ScenicSpotType> saveOrUpdate(ScenicSpotType scenicSpotType){
        if(iScenicSpotTypeService.saveOrUpdate(scenicSpotType)){
            return new ResultUtil<ScenicSpotType>().setData(scenicSpotType);
        }
        return ResultUtil.error();
    }

    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    @ApiOperation(value = "新增景点类型")
    public Result<ScenicSpotType> insert(ScenicSpotType scenicSpotType){
        if(scenicSpotType.getSortOrder() == null || Objects.equals(0,scenicSpotType.getSortOrder().compareTo(BigDecimal.ZERO))) {
            scenicSpotType.setSortOrder(BigDecimal.valueOf(iScenicSpotTypeService.count() + 1));
        }
        iScenicSpotTypeService.saveOrUpdate(scenicSpotType);
        return new ResultUtil<ScenicSpotType>().setData(scenicSpotType);
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ApiOperation(value = "编辑景点类型")
    public Result<ScenicSpotType> update(ScenicSpotType scenicSpotType){
        iScenicSpotTypeService.saveOrUpdate(scenicSpotType);
        return new ResultUtil<ScenicSpotType>().setData(scenicSpotType);
    }

    @RequestMapping(value = "/delByIds", method = RequestMethod.POST)
    @ApiOperation(value = "删除景点类型")
    public Result<Object> delByIds(@RequestParam String[] ids){
        for(String id : ids){
            iScenicSpotTypeService.removeById(id);
        }
        return ResultUtil.success();
    }
}
