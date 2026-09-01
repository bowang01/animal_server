package com.example.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelWriter;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletOutputStream;
import java.net.URLEncoder;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.common.Result;
import com.example.config.interceptor.AuthAccess;
import com.example.entity.Animal;
import com.example.entity.User;
import com.example.service.IAnimalService;
import com.example.utils.TokenUtils;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.io.InputStream;
import java.util.List;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  Frontend controller
 * </p>
 *
 * @author
 * @since 2022-04-02
 */
@RestController
@RequestMapping("/animal")
public class AnimalController {

    @Resource
    private IAnimalService animalService;

    private final String now = DateUtil.now();

    // Create or update
    @PostMapping
    public Result save(@RequestBody Animal animal) {
        animalService.saveOrUpdate(animal);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        animalService.removeById(id);
        return Result.success();
    }

    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        animalService.removeByIds(ids);
        return Result.success();
    }

    @GetMapping
    public Result findAll() {
        return Result.success(animalService.list());
    }

    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id) {
        return Result.success(animalService.getById(id));
    }

    @AuthAccess
    @GetMapping("/page/user")
    public Result findPage(@RequestParam(defaultValue = "") String name,
                           @RequestParam Integer pageNum,
                           @RequestParam Integer pageSize) {
        QueryWrapper<Animal> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        if (!"".equals(name)) {
            queryWrapper.like("nickname", name);
        }
        queryWrapper.eq("adopt", "Available");
        return Result.success(animalService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }

    @AuthAccess
    @GetMapping("/page")
    public Result findPage(@RequestParam(defaultValue = "") String name,
                           @RequestParam(defaultValue = "") String adopt,
                           @RequestParam Integer pageNum,
                           @RequestParam Integer pageSize) {
        QueryWrapper<Animal> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        if (!"".equals(name)) {
            queryWrapper.like("nickname", name);
        }
        if (!"".equals(adopt)) {
            queryWrapper.eq("adopt", adopt);
        }
        return Result.success(animalService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }

    /**
    * Export API
    */
    @GetMapping("/export")
    public void export(HttpServletResponse response) throws Exception {
        // Query all data from database
        List<Animal> list = animalService.list();
        // Write to browser from memory
        ExcelWriter writer = ExcelUtil.getWriter(true);

        // Write all list objects to Excel with default style and force header output
        writer.write(list, true);

        // Set browser response format
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("Animal Info", "UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");

        ServletOutputStream out = response.getOutputStream();
        writer.flush(out, true);
        out.close();
        writer.close();

        }

    /**
     * Excel import
     * @param file
     * @throws Exception
     */
    @PostMapping("/import")
    public Result imp(MultipartFile file) throws Exception {
        InputStream inputStream = file.getInputStream();
        ExcelReader reader = ExcelUtil.getReader(inputStream);
        // Read Excel rows as JavaBeans; headers must be English and match JavaBean properties
        List<Animal> list = reader.readAll(Animal.class);

        animalService.saveBatch(list);
        return Result.success();
    }

    private User getUser() {
        return TokenUtils.getCurrentUser();
    }

}

