package com.example.service.impl;

import com.example.service.IAnimalService;
import com.example.entity.Animal;
import com.example.mapper.AnimalMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  Service implementation
 * </p>
 *
 * @author
 * @since 2022-04-02
 */
@Service
public class AnimalServiceImpl extends ServiceImpl<AnimalMapper, Animal> implements IAnimalService {

}
