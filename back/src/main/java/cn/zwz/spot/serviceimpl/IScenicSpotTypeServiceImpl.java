package cn.zwz.spot.serviceimpl;

import cn.zwz.spot.mapper.ScenicSpotTypeMapper;
import cn.zwz.spot.entity.ScenicSpotType;
import cn.zwz.spot.service.IScenicSpotTypeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 景点类型 服务层接口实现
 * @author 郑为中
 */
@Slf4j
@Service
@Transactional
public class IScenicSpotTypeServiceImpl extends ServiceImpl<ScenicSpotTypeMapper, ScenicSpotType> implements IScenicSpotTypeService {

    @Autowired
    private ScenicSpotTypeMapper scenicSpotTypeMapper;
}