package cn.zwz.spot.job;

import cn.zwz.basics.redis.RedisTemplateHelper;
import cn.zwz.data.utils.ZwzNullUtils;
import cn.zwz.spot.entity.Collection;
import cn.zwz.spot.entity.Evaluate;
import cn.zwz.spot.entity.ScenicSpot;
import cn.zwz.spot.service.ICollectionService;
import cn.zwz.spot.service.IEvaluateService;
import cn.zwz.spot.service.IScenicSpotService;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class ScenicJob {

    @Autowired
    private IScenicSpotService iScenicSpotService;

    @Autowired
    private IEvaluateService iEvaluateService;

    @Autowired
    private ICollectionService iCollectionService;

    @Autowired
    private RedisTemplateHelper redisTemplate;

    @Scheduled(cron = "0 0/1 * * * ?")
    @ApiOperation(value = "景点数据更新")
    public void job(){
        List<ScenicSpot> spotList = iScenicSpotService.list();
        for (ScenicSpot vo : spotList) {
            Long evaluateSum = 0L;
            QueryWrapper<Evaluate> evalQw = new QueryWrapper<>();
            evalQw.eq("spot_id",vo.getId());
            List<Evaluate> evaluateList = iEvaluateService.list(evalQw);
            for (Evaluate evaluate : evaluateList) {
                evaluateSum += evaluate.getLevel().longValue();
            }
            // 收藏 10分
            QueryWrapper<Collection> coQw = new QueryWrapper<>();
            coQw.eq("spot_id",vo.getId());
            evaluateSum += iCollectionService.count(coQw);
            // 浏览 1分
            String viewStr = redisTemplate.get("SPOT_VIEW:" + vo.getId());
            if(!ZwzNullUtils.isNull(viewStr)) {
                try {
                    long viewNumber = Long.parseLong(viewStr);
                    evaluateSum += viewNumber;
                } catch (Exception e) {}
            }
            vo.setValue(evaluateSum);
        }
        Collections.sort(spotList, new Comparator<ScenicSpot>() {
            @Override
            public int compare(ScenicSpot o1, ScenicSpot o2) {
                return (int)(o2.getValue() - o1.getValue());
            }
        });
        if(spotList.size() > 10) {
            spotList = spotList.subList(0,10);
        }
        for (ScenicSpot vo1 : spotList) {
            // 评分
            BigDecimal evaluateSum = BigDecimal.ZERO;
            QueryWrapper<Evaluate> evalQw = new QueryWrapper<>();
            evalQw.eq("spot_id",vo1.getId());
            List<Evaluate> evaluateList = iEvaluateService.list(evalQw);
            for (Evaluate evaluate : evaluateList) {
                evaluateSum = evaluateSum.add(evaluate.getLevel());
            }
            if(evaluateList.size() > 0) {
                vo1.setStar(evaluateSum.divide(BigDecimal.valueOf(evaluateList.size()),2, RoundingMode.DOWN));
            } else {
                vo1.setStar(BigDecimal.valueOf(-1));
            }
            // 收藏
            QueryWrapper<Collection> coQw = new QueryWrapper<>();
            coQw.eq("spot_id",vo1.getId());
            vo1.setCollection(iCollectionService.count(coQw));
        }
        redisTemplate.set("SPOT_JOB_DATA", JSON.toJSONString(spotList));
        System.out.println("缓存完毕！");
    }
}
