package com.example.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelWriter;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletOutputStream;
import java.net.URLEncoder;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.common.Result;
import com.example.entity.User;
import com.example.service.IArticleKpService;
import com.example.utils.TokenUtils;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.io.InputStream;
import java.util.List;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.web.multipart.MultipartFile;

import com.example.entity.ArticleKp;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  Frontend controller
 * </p>
 *
 * @author

 */
@RestController
@RequestMapping("/articleKp")
public class ArticleKpController {

    @Resource
    private IArticleKpService articleKpService;

    private final String now = DateUtil.now();

    // Create or update
    @PostMapping
    public Result save(@RequestBody ArticleKp articleKp) {
        if (articleKp.getId() == null) {
            articleKp.setTime(DateUtil.now());

        }
        articleKpService.saveOrUpdate(articleKp);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        articleKpService.removeById(id);
        return Result.success();
    }

    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        articleKpService.removeByIds(ids);
        return Result.success();
    }

    @GetMapping
    public Result findAll() {
        return Result.success(articleKpService.list());
    }

    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id) {
        ArticleKp articleKp = articleKpService.getById(id);
        UpdateWrapper<ArticleKp> wrapper = new UpdateWrapper<>();
        wrapper.eq("id", id);
        wrapper.set("read1", articleKp.getRead1() + 1);
        articleKpService.update(wrapper);
        return Result.success(articleKp);
    }

    @GetMapping("/page")
    public Result findPage(@RequestParam(defaultValue = "") String name,
                           @RequestParam Integer pageNum,
                           @RequestParam Integer pageSize) {
        QueryWrapper<ArticleKp> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        if (!"".equals(name)) {
            queryWrapper.like("name", name);
        }
        return Result.success(articleKpService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }

    /**
    * Export API
    */
    @GetMapping("/export")
    public void export(HttpServletResponse response) throws Exception {
        // Query all data from database
        List<ArticleKp> list = articleKpService.list();
        // Write to browser from memory
        ExcelWriter writer = ExcelUtil.getWriter(true);

        // Write all list objects to Excel with default style and force header output
        writer.write(list, true);

        // Set browser response format
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("ArticleKp Info", "UTF-8");
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
        List<ArticleKp> list = reader.readAll(ArticleKp.class);

        articleKpService.saveBatch(list);
        return Result.success();
    }

    private User getUser() {
        return TokenUtils.getCurrentUser();
    }

}

