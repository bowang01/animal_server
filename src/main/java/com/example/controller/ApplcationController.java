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
import com.example.entity.Animal;
import com.example.entity.Applcation;
import com.example.entity.User;
import com.example.service.IAnimalService;
import com.example.service.IApplcationService;
import com.example.utils.TokenUtils;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.io.InputStream;
import java.util.ArrayList;
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
@RequestMapping("/applcation")
public class ApplcationController {

    @Resource
    private IApplcationService applcationService;

    @Resource
    private IAnimalService animalService;

    private final String now = DateUtil.now();

    // Create or update
    @PostMapping
    public Result save(@RequestBody Applcation applcation) {
        applcationService.saveOrUpdate(applcation);
        return Result.success();
    }

    @PostMapping("/state/{id}/{state}")
    public Result state(@PathVariable Integer id, @PathVariable String state) {
        Applcation applcation = applcationService.getById(id);
        applcation.setState(state);

        QueryWrapper<Applcation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("animal_id", applcation.getAnimalId());
        List<Applcation> list = applcationService.list(queryWrapper);
        for (Applcation app : list) {
            app.setState("Rejected");
            applcationService.updateById(app);
        }

        applcationService.updateById(applcation);

        Animal animal = animalService.getById(applcation.getAnimalId());
        animal.setIsAdopt("Yes");
        animal.setAdopt("Not available");
        animalService.updateById(animal);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        applcationService.removeById(id);
        return Result.success();
    }

    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        applcationService.removeByIds(ids);
        return Result.success();
    }

    @GetMapping
    public Result findAll() {
        return Result.success(applcationService.list());
    }

    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id) {
        return Result.success(applcationService.getById(id));
    }

    @GetMapping("/my")
    public Result my() {
        List<Animal> animals = animalService.list();
        QueryWrapper<Applcation> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        User currentUser = TokenUtils.getCurrentUser();
        if (currentUser == null) {
            return Result.success(new ArrayList<>());
        }
        queryWrapper.eq("user_id", currentUser.getId());
        List<Applcation> applcations = applcationService.list(queryWrapper);
        for (Applcation record : applcations) {
            animals.stream().filter(animal -> animal.getId().equals(record.getAnimalId())).findFirst().ifPresent(record::setAnimal);
        }
        return Result.success(applcations);
    }

    @GetMapping("/page")
    public Result findPage(@RequestParam(defaultValue = "") String name,
                           @RequestParam Integer pageNum,
                           @RequestParam Integer pageSize) {
        List<Animal> animals = animalService.list();
        QueryWrapper<Applcation> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        if (!"".equals(name)) {
            queryWrapper.like("name", name);
        }
        Page<Applcation> page = applcationService.page(new Page<>(pageNum, pageSize), queryWrapper);
        for (Applcation record : page.getRecords()) {
            animals.stream().filter(animal -> animal.getId().equals(record.getAnimalId())).findFirst().ifPresent(record::setAnimal);
        }
        return Result.success(page);
    }

    /**
    * Export API
    */
    @GetMapping("/export")
    public void export(HttpServletResponse response) throws Exception {
        // Query all data from database
        List<Applcation> list = applcationService.list();
        // Write to browser from memory
        ExcelWriter writer = ExcelUtil.getWriter(true);

        // Write all list objects to Excel with default style and force header output
        writer.write(list, true);

        // Set browser response format
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("Applcation Info", "UTF-8");
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
        List<Applcation> list = reader.readAll(Applcation.class);

        applcationService.saveBatch(list);
        return Result.success();
    }

    private User getUser() {
        return TokenUtils.getCurrentUser();
    }

}

