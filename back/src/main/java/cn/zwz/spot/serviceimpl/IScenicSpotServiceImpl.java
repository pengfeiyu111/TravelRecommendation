package cn.zwz.spot.serviceimpl;

import cn.zwz.spot.mapper.ScenicSpotMapper;
import cn.zwz.spot.entity.ScenicSpot;
import cn.zwz.spot.service.IScenicSpotService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 景点 服务层接口实现
 * @author 郑为中
 */
@Slf4j
@Service
@Transactional
public class IScenicSpotServiceImpl extends ServiceImpl<ScenicSpotMapper, ScenicSpot> implements IScenicSpotService {

    @Autowired
    private ScenicSpotMapper scenicSpotMapper;
}