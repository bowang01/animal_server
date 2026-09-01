package com.example.service;

import com.example.entity.Notice;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  Service
 * </p>
 *
 * @author

 */
public interface INoticeService extends IService<Notice> {

    List<Notice> limit(int i);
}
