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
import cn.zwz.spot.entity.Collection;
import cn.zwz.spot.entity.Evaluate;
import cn.zwz.spot.entity.ScenicSpot;
import cn.zwz.spot.service.ICollectionService;
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
@Api(tags = "收藏管理接口")
@RequestMapping("/zwz/collection")
@Transactional
public class CollectionController {

    @Autowired
    private ICollectionService iCollectionService;

    @Autowired
    private IScenicSpotService iScenicSpotService;

    @Autowired
    private IUserService iUserService;

    @Autowired
    private SecurityUtil securityUtil;

    @RequestMapping(value = "/addCollection", method = RequestMethod.GET)
    @ApiOperation(value = "用户收藏")
    public Result<Collection> addCollection(@RequestParam String id){
        ScenicSpot spot = iScenicSpotService.getById(id);
        if(spot == null) {
            return ResultUtil.error("景点不存在");
        }
        User currUser = securityUtil.getCurrUser();
        // 判重
        QueryWrapper<Collection> qw = new QueryWrapper<>();
        qw.eq("user_id",currUser.getId());
        qw.eq("spot_id",spot.getId());
        qw.last("limit 1");
        Collection collection = iCollectionService.getOne(qw);
        if(collection != null) {
            return ResultUtil.success("您已收藏");
        }
        // 持久化
        Collection c = new Collection();
        c.setSpotId(spot.getId());
        c.setSpotName(spot.getTitle());
        c.setImage(spot.getImage());
        c.setUserId(currUser.getId());
        c.setUserName(currUser.getNickname());
        c.setTime(DateUtil.now());
        iCollectionService.saveOrUpdate(c);
        return ResultUtil.success();
    }

    @RequestMapping(value = "/cancelCollection", method = RequestMethod.GET)
    @ApiOperation(value = "用户取消收藏")
    public Result<Collection> cancelCollection(@RequestParam String id){
        ScenicSpot spot = iScenicSpotService.getById(id);
        if(spot == null) {
            return ResultUtil.error("景点不存在");
        }
        User currUser = securityUtil.getCurrUser();
        // 判重
        QueryWrapper<Collection> qw = new QueryWrapper<>();
        qw.eq("user_id",currUser.getId());
        qw.eq("spot_id",spot.getId());
        iCollectionService.remove(qw);
        return ResultUtil.success();
    }

    @RequestMapping(value = "/getCollectionStatus", method = RequestMethod.GET)
    @ApiOperation(value = "查询收藏状态")
    public Result<Long> getCollectionStatus(@RequestParam String id){
        ScenicSpot spot = iScenicSpotService.getById(id);
        if(spot == null) {
            return ResultUtil.error("景点不存在");
        }
        User currUser = securityUtil.getCurrUser();
        // 判重
        QueryWrapper<Collection> qw = new QueryWrapper<>();
        qw.eq("user_id",currUser.getId());
        qw.eq("spot_id",spot.getId());
        return new ResultUtil<Long>().setData(iCollectionService.count(qw));
    }

    @RequestMapping(value = "/getOne", method = RequestMethod.GET)
    @ApiOperation(value = "查询单条收藏")
    public Result<Collection> get(@RequestParam String id){
        return new ResultUtil<Collection>().setData(iCollectionService.getById(id));
    }

    @RequestMapping(value = "/count", method = RequestMethod.GET)
    @ApiOperation(value = "查询全部收藏个数")
    public Result<Long> getCount(){
        return new ResultUtil<Long>().setData(iCollectionService.count());
    }

    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    @ApiOperation(value = "查询全部收藏")
    public Result<List<Collection>> getAll(){
        return new ResultUtil<List<Collection>>().setData(iCollectionService.list());
    }

    @RequestMapping(value = "/getByAllOnUser", method = RequestMethod.GET)
    @ApiOperation(value = "查询全部收藏")
    public Result<List<Collection>> getByAllOnUser(){
        User currUser = securityUtil.getCurrUser();
        QueryWrapper<Collection> qw = new QueryWrapper<>();
        qw.eq("user_id",currUser.getId());
        return new ResultUtil<List<Collection>>().setData(iCollectionService.list(qw));
    }

    @RequestMapping(value = "/getByPage", method = RequestMethod.GET)
    @ApiOperation(value = "查询收藏")
    public Result<IPage<Collection>> getByPage(@ModelAttribute Collection collection ,@ModelAttribute PageVo page){
        QueryWrapper<Collection> qw = new QueryWrapper<>();
        User currUser = securityUtil.getCurrUser();
        QueryWrapper<User> userQw = new QueryWrapper<>();
        userQw.eq("id",currUser.getId());
        userQw.inSql("id","SELECT user_id FROM a_user_role WHERE del_flag = 0 AND role_id = '496138616573952'");
        if(iUserService.count(userQw) < 1L) {
            qw.eq("user_id",currUser.getId());
        }
        if(!ZwzNullUtils.isNull(collection.getSpotName())) {
            qw.like("spot_name",collection.getSpotName());
        }
        if(!ZwzNullUtils.isNull(collection.getUserName())) {
            qw.like("user_name",collection.getUserName());
        }
        IPage<Collection> data = iCollectionService.page(PageUtil.initMpPage(page),qw);
        return new ResultUtil<IPage<Collection>>().setData(data);
    }

    @RequestMapping(value = "/insertOrUpdate", method = RequestMethod.POST)
    @ApiOperation(value = "增改收藏")
    public Result<Collection> saveOrUpdate(Collection collection){
        if(iCollectionService.saveOrUpdate(collection)){
            return new ResultUtil<Collection>().setData(collection);
        }
        return ResultUtil.error();
    }

    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    @ApiOperation(value = "新增收藏")
    public Result<Collection> insert(Collection collection){
        iCollectionService.saveOrUpdate(collection);
        return new ResultUtil<Collection>().setData(collection);
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ApiOperation(value = "编辑收藏")
    public Result<Collection> update(Collection collection){
        iCollectionService.saveOrUpdate(collection);
        return new ResultUtil<Collection>().setData(collection);
    }

    @RequestMapping(value = "/delByIds", method = RequestMethod.POST)
    @ApiOperation(value = "删除收藏")
    public Result<Object> delByIds(@RequestParam String[] ids){
        for(String id : ids){
            iCollectionService.removeById(id);
        }
        return ResultUtil.success();
    }
}
