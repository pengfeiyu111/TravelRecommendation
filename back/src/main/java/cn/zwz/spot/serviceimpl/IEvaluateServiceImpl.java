package cn.zwz.spot.serviceimpl;

import cn.zwz.spot.mapper.EvaluateMapper;
import cn.zwz.spot.entity.Evaluate;
import cn.zwz.spot.service.IEvaluateService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 评价 服务层接口实现
 * @author 郑为中
 */
@Slf4j
@Service
@Transactional
public class IEvaluateServiceImpl extends ServiceImpl<EvaluateMapper, Evaluate> implements IEvaluateService {

    @Autowired
    private EvaluateMapper evaluateMapper;
}