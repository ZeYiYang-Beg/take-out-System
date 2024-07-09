package com.sky.service;

import com.github.pagehelper.Page;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.result.PageResult;
import org.springframework.stereotype.Service;

import java.util.List;

public interface SetmealService {
    /**
     * 套餐添加接口
     * @param setmealDTO
     */
    void save(SetmealDTO setmealDTO);

    /**
     * 套餐分页查询
     * @param setmealPageQueryDTO
     * @return
     */
    PageResult getAll(SetmealPageQueryDTO setmealPageQueryDTO);

    /**
     * 套餐的启用和禁用
     * @param status
     */
    void startOrstop(Integer status, Long id);

    /**
     * 删除套餐
     * @param ids
     */
    void deleteByIds(List<Long> ids);

    /**
     * 修改套餐
     * @param setmealDTO
     */
    void updateById(SetmealDTO setmealDTO);
}
