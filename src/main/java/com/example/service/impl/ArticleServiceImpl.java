package com.example.service.impl;

import com.example.entity.Article;
import com.example.mapper.ArticleMapper;
import com.example.service.IArticleService;
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
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements IArticleService {

}
