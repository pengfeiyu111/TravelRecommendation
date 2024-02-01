package cn.zwz.spot.serviceimpl;

import cn.zwz.spot.mapper.CollectionMapper;
import cn.zwz.spot.entity.Collection;
import cn.zwz.spot.service.ICollectionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 收藏 服务层接口实现
 * @author 郑为中
 */
@Slf4j
@Service
@Transactional
public class ICollectionServiceImpl extends ServiceImpl<CollectionMapper, Collection> implements ICollectionService {

    @Autowired
    private CollectionMapper collectionMapper;
}