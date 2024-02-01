package cn.zwz.spot.controller;

import cn.zwz.basics.redis.RedisTemplateHelper;
import cn.zwz.basics.utils.PageUtil;
import cn.zwz.basics.utils.ResultUtil;
import cn.zwz.basics.baseVo.PageVo;
import cn.zwz.basics.baseVo.Result;
import cn.zwz.basics.utils.SecurityUtil;
import cn.zwz.data.entity.User;
import cn.zwz.data.utils.ZwzNullUtils;
import cn.zwz.spot.entity.Collection;
import cn.zwz.spot.entity.Evaluate;
import cn.zwz.spot.entity.ScenicSpot;
import cn.zwz.spot.service.ICollectionService;
import cn.zwz.spot.service.IEvaluateService;
import cn.zwz.spot.service.IScenicSpotService;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * @author 郑为中
 * CSDN: Designer 小郑
 */
@Slf4j
@RestController
@Api(tags = "景点管理接口")
@RequestMapping("/zwz/scenicSpot")
@Transactional
public class ScenicSpotController {

    @Autowired
    private IScenicSpotService iScenicSpotService;

    @Autowired
    private IEvaluateService iEvaluateService;

    @Autowired
    private ICollectionService iCollectionService;

    @Autowired
    private SecurityUtil securityUtil;

    @Autowired
    private RedisTemplateHelper redisTemplate;

    @RequestMapping(value = "/getRecommendListOnWeb", method = RequestMethod.GET)
    @ApiOperation(value = "查询推荐的景点")
    public Result<List<ScenicSpot>> getRecommendList(){
        String str = redisTemplate.get("SPOT_JOB_DATA");
        List<ScenicSpot> scenicSpots = JSON.parseArray(str, ScenicSpot.class);
        return new ResultUtil<List<ScenicSpot>>().setData(scenicSpots);
    }

    @RequestMapping(value = "/getRecommendList2OnWeb", method = RequestMethod.GET)
    @ApiOperation(value = "查询推荐的景点")
    public Result<List<ScenicSpot>> getRecommendList2(){
        List<ScenicSpot> spotList = iScenicSpotService.list();
        int[] arr = new int[spotList.size()];
        for(int i = 1; i < spotList.size(); i ++) {
            arr[i - 1] = i;
        }
        int[] ints = selectM(arr, 10);
        List<ScenicSpot> ans = new ArrayList<>();
        for (int i : ints) {
            ans.add(spotList.get(i));
        }
        return new ResultUtil<List<ScenicSpot>>().setData(ans);
    }

    public static int[] selectM(int[] arr,int m){
        int len=arr.length;
        if(m>arr.length) {
            throw new RuntimeException("xxxxx");
        }
        int[] res=new int[m];
        for(int i=0;i<m;i++){
            int randomIndex=len-1-new Random().nextInt(len-i);
            res[i]=arr[randomIndex];
            int tmp=arr[randomIndex];
            arr[randomIndex]=arr[i];
            arr[i]=tmp;
        }
        return res;
    }

    @RequestMapping(value = "/getOne", method = RequestMethod.GET)
    @ApiOperation(value = "查询单条景点")
    public Result<ScenicSpot> get(@RequestParam String id){
        return new ResultUtil<ScenicSpot>().setData(iScenicSpotService.getById(id));
    }

    @RequestMapping(value = "/getOneOnWeb", method = RequestMethod.GET)
    @ApiOperation(value = "查询单条景点")
    public Result<ScenicSpot> getOneOnWeb(@RequestParam String id){
        ScenicSpot vo = iScenicSpotService.getById(id);
        if(vo == null) {
            return ResultUtil.error("景点不存在");
        }
        // 评分
        BigDecimal evaluateSum = BigDecimal.ZERO;
        QueryWrapper<Evaluate> evalQw = new QueryWrapper<>();
        evalQw.eq("spot_id",vo.getId());
        List<Evaluate> evaluateList = iEvaluateService.list(evalQw);
        for (Evaluate evaluate : evaluateList) {
            evaluateSum = evaluateSum.add(evaluate.getLevel());
        }
        if(evaluateList.size() > 0) {
            vo.setStar(evaluateSum.divide(BigDecimal.valueOf(evaluateList.size()),2, RoundingMode.DOWN));
        } else {
            vo.setStar(BigDecimal.valueOf(-1));
        }
        // 收藏
        QueryWrapper<Collection> coQw = new QueryWrapper<>();
        coQw.eq("spot_id",vo.getId());
        vo.setCollection(iCollectionService.count(coQw));
        String viewStr = redisTemplate.get("SPOT_VIEW:" + vo.getId());
        if(!ZwzNullUtils.isNull(viewStr)) {
            try {
                long viewNumber = Long.parseLong(viewStr);
                redisTemplate.set("SPOT_VIEW:" + vo.getId(),(viewNumber + 1) + "");
            } catch (Exception e) {}
        } else {
            redisTemplate.set("SPOT_VIEW:" + vo.getId(), "1");
        }
        return new ResultUtil<ScenicSpot>().setData(vo);
    }

    @RequestMapping(value = "/count", method = RequestMethod.GET)
    @ApiOperation(value = "查询全部景点个数")
    public Result<Long> getCount(){
        return new ResultUtil<Long>().setData(iScenicSpotService.count());
    }

    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    @ApiOperation(value = "查询全部景点")
    public Result<List<ScenicSpot>> getAll(){
        return new ResultUtil<List<ScenicSpot>>().setData(iScenicSpotService.list());
    }

    @RequestMapping(value = "/getTopCollection", method = RequestMethod.GET)
    @ApiOperation(value = "查询收藏前10的景点")
    public Result<List<ScenicSpot>> getTopCollection(){
        List<ScenicSpot> spotList = iScenicSpotService.list();
        for (ScenicSpot vo : spotList) {
            QueryWrapper<Collection> coQw = new QueryWrapper<>();
            coQw.eq("spot_id",vo.getId());
            vo.setCollection(iCollectionService.count(coQw));
        }
        Collections.sort(spotList, new Comparator<ScenicSpot>() {
            @Override
            public int compare(ScenicSpot o1, ScenicSpot o2) {
                return o2.getCollection().compareTo(o1.getCollection());
            }
        });
        if(spotList.size() > 10) {
            spotList = spotList.subList(0,10);
        }
        return new ResultUtil<List<ScenicSpot>>().setData(spotList);
    }

    @RequestMapping(value = "/getTopEvaluate", method = RequestMethod.GET)
    @ApiOperation(value = "查询评价前10的景点")
    public Result<List<ScenicSpot>> getTopEvaluate(){
        List<ScenicSpot> spotList = iScenicSpotService.list();
        for (ScenicSpot vo : spotList) {
            BigDecimal evaluateSum = BigDecimal.ZERO;
            QueryWrapper<Evaluate> evalQw = new QueryWrapper<>();
            evalQw.eq("spot_id",vo.getId());
            List<Evaluate> evaluateList = iEvaluateService.list(evalQw);
            for (Evaluate evaluate : evaluateList) {
                evaluateSum = evaluateSum.add(evaluate.getLevel());
            }
            if(evaluateList.size() > 0) {
                vo.setStar(evaluateSum.divide(BigDecimal.valueOf(evaluateList.size()),2, RoundingMode.DOWN));
            } else {
                vo.setStar(BigDecimal.ZERO);
            }
        }
        Collections.sort(spotList, new Comparator<ScenicSpot>() {
            @Override
            public int compare(ScenicSpot o1, ScenicSpot o2) {
                return o2.getStar().compareTo(o1.getStar());
            }
        });
        if(spotList.size() > 10) {
            spotList = spotList.subList(0,10);
        }
        return new ResultUtil<List<ScenicSpot>>().setData(spotList);
    }

    @RequestMapping(value = "/getTopCollectionOnWeb", method = RequestMethod.GET)
    @ApiOperation(value = "查询收藏前10的景点")
    public Result<List<ScenicSpot>> getTopCollectionOnWeb(){
        List<ScenicSpot> spotList = iScenicSpotService.list();
        for (ScenicSpot vo : spotList) {
            QueryWrapper<Collection> coQw = new QueryWrapper<>();
            coQw.eq("spot_id",vo.getId());
            vo.setCollection(iCollectionService.count(coQw));
        }
        Collections.sort(spotList, new Comparator<ScenicSpot>() {
            @Override
            public int compare(ScenicSpot o1, ScenicSpot o2) {
                return o2.getCollection().compareTo(o1.getCollection());
            }
        });
        if(spotList.size() > 10) {
            spotList = spotList.subList(0,10);
        }
        return new ResultUtil<List<ScenicSpot>>().setData(spotList);
    }

    @RequestMapping(value = "/getTopEvaluateOnWeb", method = RequestMethod.GET)
    @ApiOperation(value = "查询评价前10的景点")
    public Result<List<ScenicSpot>> getTopEvaluateOnWeb(){
        List<ScenicSpot> spotList = iScenicSpotService.list();
        for (ScenicSpot vo : spotList) {
            BigDecimal evaluateSum = BigDecimal.ZERO;
            QueryWrapper<Evaluate> evalQw = new QueryWrapper<>();
            evalQw.eq("spot_id",vo.getId());
            List<Evaluate> evaluateList = iEvaluateService.list(evalQw);
            for (Evaluate evaluate : evaluateList) {
                evaluateSum = evaluateSum.add(evaluate.getLevel());
            }
            if(evaluateList.size() > 0) {
                vo.setStar(evaluateSum.divide(BigDecimal.valueOf(evaluateList.size()),2, RoundingMode.DOWN));
            } else {
                vo.setStar(BigDecimal.valueOf(-1));
            }
        }
        Collections.sort(spotList, new Comparator<ScenicSpot>() {
            @Override
            public int compare(ScenicSpot o1, ScenicSpot o2) {
                return o2.getStar().compareTo(o1.getStar());
            }
        });
        if(spotList.size() > 10) {
            spotList = spotList.subList(0,10);
        }
        return new ResultUtil<List<ScenicSpot>>().setData(spotList);
    }

    @RequestMapping(value = "/getByPage", method = RequestMethod.GET)
    @ApiOperation(value = "查询景点")
    public Result<IPage<ScenicSpot>> getByPage(@ModelAttribute ScenicSpot scenicSpot ,@ModelAttribute PageVo page){
        User currUser = securityUtil.getCurrUser();
        QueryWrapper<ScenicSpot> qw = new QueryWrapper<>();
        if(!ZwzNullUtils.isNull(scenicSpot.getTitle())) {
            qw.like("title",scenicSpot.getTitle());
        }
        if(!ZwzNullUtils.isNull(scenicSpot.getType())) {
            qw.eq("type",scenicSpot.getType());
        }
        if(!ZwzNullUtils.isNull(scenicSpot.getAddress())) {
            qw.like("address",scenicSpot.getAddress());
        }
        IPage<ScenicSpot> data = iScenicSpotService.page(PageUtil.initMpPage(page),qw);
        for (ScenicSpot vo : data.getRecords()) {
            // 评分
            BigDecimal evaluateSum = BigDecimal.ZERO;
            QueryWrapper<Evaluate> evalQw = new QueryWrapper<>();
            evalQw.eq("spot_id",vo.getId());
            List<Evaluate> evaluateList = iEvaluateService.list(evalQw);
            for (Evaluate evaluate : evaluateList) {
                evaluateSum = evaluateSum.add(evaluate.getLevel());
            }
            if(evaluateList.size() > 0) {
                vo.setStar(evaluateSum.divide(BigDecimal.valueOf(evaluateList.size()),2, RoundingMode.DOWN));
            } else {
                vo.setStar(BigDecimal.valueOf(-1));
            }
            // 收藏
            QueryWrapper<Collection> coQw = new QueryWrapper<>();
            coQw.eq("spot_id",vo.getId());
            vo.setCollection(iCollectionService.count(coQw));
            // 我的收藏
            QueryWrapper<Collection> coQw2 = new QueryWrapper<>();
            coQw2.eq("spot_id",vo.getId());
            coQw2.eq("user_id",currUser.getId());
            coQw2.last("limit 1");
            vo.setMyCollection(iCollectionService.getOne(coQw2) != null);
            // 我的评分
            QueryWrapper<Evaluate> evalQw2 = new QueryWrapper<>();
            evalQw2.eq("spot_id",vo.getId());
            evalQw2.eq("user_id",currUser.getId());
            evalQw2.last("limit 1");
            Evaluate myEvaluate = iEvaluateService.getOne(evalQw2);
            if(myEvaluate == null) {
                vo.setMyEvaluate(BigDecimal.ZERO);
                vo.setMyEvaluateMessage("");
            } else {
                vo.setMyEvaluate(myEvaluate.getLevel());
                vo.setMyEvaluateMessage(myEvaluate.getMessage());
            }
        }
        return new ResultUtil<IPage<ScenicSpot>>().setData(data);
    }

    @RequestMapping(value = "/insertOrUpdate", method = RequestMethod.POST)
    @ApiOperation(value = "增改景点")
    public Result<ScenicSpot> saveOrUpdate(ScenicSpot scenicSpot){
        if(iScenicSpotService.saveOrUpdate(scenicSpot)){
            return new ResultUtil<ScenicSpot>().setData(scenicSpot);
        }
        return ResultUtil.error();
    }

    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    @ApiOperation(value = "新增景点")
    public Result<ScenicSpot> insert(ScenicSpot scenicSpot){
        iScenicSpotService.saveOrUpdate(scenicSpot);
        return new ResultUtil<ScenicSpot>().setData(scenicSpot);
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ApiOperation(value = "编辑景点")
    public Result<ScenicSpot> update(ScenicSpot scenicSpot){
        iScenicSpotService.saveOrUpdate(scenicSpot);
        return new ResultUtil<ScenicSpot>().setData(scenicSpot);
    }

    @RequestMapping(value = "/delByIds", method = RequestMethod.POST)
    @ApiOperation(value = "删除景点")
    public Result<Object> delByIds(@RequestParam String[] ids){
        for(String id : ids){
            iScenicSpotService.removeById(id);
        }
        return ResultUtil.success();
    }

    @RequestMapping(value = "/getAllOnWeb", method = RequestMethod.GET)
    @ApiOperation(value = "查询全部景点")
    public Result<List<ScenicSpot>> getAllOnWeb(@RequestParam String type,@RequestParam String address,@RequestParam BigDecimal min,@RequestParam BigDecimal max){
        QueryWrapper<ScenicSpot> qw = new QueryWrapper<>();
        if(!ZwzNullUtils.isNull(type)) {
            qw.like("type",type);
        }
        if(!ZwzNullUtils.isNull(address)) {
            qw.like("address",address);
        }
        qw.ge("price",min);
        qw.le("price",max);
        List<ScenicSpot> spotList = iScenicSpotService.list(qw);
        for (ScenicSpot vo : spotList) {
            // 评分
            BigDecimal evaluateSum = BigDecimal.ZERO;
            QueryWrapper<Evaluate> evalQw = new QueryWrapper<>();
            evalQw.eq("spot_id",vo.getId());
            List<Evaluate> evaluateList = iEvaluateService.list(evalQw);
            for (Evaluate evaluate : evaluateList) {
                evaluateSum = evaluateSum.add(evaluate.getLevel());
            }
            if(evaluateList.size() > 0) {
                vo.setStar(evaluateSum.divide(BigDecimal.valueOf(evaluateList.size()),2, RoundingMode.DOWN));
            } else {
                vo.setStar(BigDecimal.valueOf(-1));
            }
            // 收藏
            QueryWrapper<Collection> coQw = new QueryWrapper<>();
            coQw.eq("spot_id",vo.getId());
            vo.setCollection(iCollectionService.count(coQw));
        }
        return new ResultUtil<List<ScenicSpot>>().setData(spotList);
    }
}
