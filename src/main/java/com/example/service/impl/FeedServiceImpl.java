package com.example.service.impl;

import com.example.service.IFeedService;
import com.example.entity.Feed;
import com.example.mapper.FeedMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  Service implementation
 * </p>
 *
 * @author

 */
@Service
public class FeedServiceImpl extends ServiceImpl<FeedMapper, Feed> implements IFeedService {

}
