package cn.zwz.spot.serviceimpl;

import cn.zwz.spot.mapper.DeliciousFoodMapper;
import cn.zwz.spot.entity.DeliciousFood;
import cn.zwz.spot.service.IDeliciousFoodService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 美食 服务层接口实现
 * @author 郑为中
 */
@Slf4j
@Service
@Transactional
public class IDeliciousFoodServiceImpl extends ServiceImpl<DeliciousFoodMapper, DeliciousFood> implements IDeliciousFoodService {

    @Autowired
    private DeliciousFoodMapper deliciousFoodMapper;
}