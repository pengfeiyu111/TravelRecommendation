package cn.zwz.spot.serviceimpl;

import cn.zwz.spot.mapper.TripMapper;
import cn.zwz.spot.entity.Trip;
import cn.zwz.spot.service.ITripService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 行程 服务层接口实现
 * @author 郑为中
 */
@Slf4j
@Service
@Transactional
public class ITripServiceImpl extends ServiceImpl<TripMapper, Trip> implements ITripService {

    @Autowired
    private TripMapper tripMapper;
}