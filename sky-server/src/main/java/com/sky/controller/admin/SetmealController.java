package com.sky.controller.admin;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author
 * @version 1.0
 * @date 2024/7/9 4:16
 **/
@RestController
@RequestMapping("/admin/setmeal")
@Slf4j
@Api(tags = "套餐相关接口")
public class SetmealController {
    @Autowired
    private SetmealService setmealService;

    @PostMapping
    @ApiOperation("套餐添加功能")
    public Result save(@RequestBody SetmealDTO setmealDTO) {
        log.info("套餐添加功能: {}", setmealDTO);
        setmealService.save(setmealDTO);
        return Result.success();
    }

    @GetMapping("/page")
    @ApiOperation("套餐分页查询")
    public Result<PageResult> getAll(SetmealPageQueryDTO setmealPageQueryDTO) {
        PageResult pageResult = setmealService.getAll(setmealPageQueryDTO);
        return Result.success(pageResult);
    }

    @PostMapping("/status/{status}")
    @ApiOperation("套餐的起售和禁售")
    public Result startOrstop(@PathVariable Integer status, Long id) {
        setmealService.startOrstop(status, id);
        return Result.success();
    }

    @DeleteMapping
    @ApiOperation("删除套餐")
    public Result deleteByIds(@RequestParam List<Long> ids) {
        setmealService.deleteByIds(ids);
        return Result.success();
    }

    @PutMapping
    @ApiOperation("修改套餐")
    public Result updateById(@RequestBody SetmealDTO setmealDTO) {
        setmealService.updateById(setmealDTO);
        return Result.success();
    }
}
