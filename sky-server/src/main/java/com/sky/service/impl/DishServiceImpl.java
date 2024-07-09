package com.sky.service.impl;

import com.fasterxml.jackson.databind.util.BeanUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.sky.mapper.SetmealDishMapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author
 * @version 1.0
 * @date 2024/7/7 3:21
 **/
@Service
@Slf4j
public class DishServiceImpl implements DishService {
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private DishFlavorMapper dishFlavorMapper;
    @Autowired
    private SetmealDishMapper setmealDishmapper;
    @Autowired
    private SetmealMapper setmealMapper;

    @Transactional
    @Override
    public void saveWithFlavor(DishDTO dishDTO) {
        Dish dish = new Dish();
        // 属性拷贝
        BeanUtils.copyProperties(dishDTO, dish);
        // 向菜品表插入1条数据
        dishMapper.insert(dish);
        Long dishId = dish.getId();
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if (flavors != null && flavors.size() > 0) {
            flavors.forEach(dishFlavor -> {
                dishFlavor.setDishId(dishId);
            });
            // 向口味表插入n条数据
            dishFlavorMapper.insertBatch(flavors);
        }
    }

    @Override
    public PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO) {
        // 开始分页
        PageHelper.startPage(dishPageQueryDTO.getPage(), dishPageQueryDTO.getPageSize());
        Page<DishVO> dishVOS = dishMapper.pageQuery(dishPageQueryDTO);
        return new PageResult(dishVOS.getTotal(), dishVOS.getResult());
    }

    /**
     * 菜品的批量删除
     * @param ids
     */
    @Override
    @Transactional
    public void deleteBatch(List<Long> ids) {
        // 判断当前菜品是否能够删除 --- 是否存在起售中的菜品
        for (Long id : ids) {
            Dish dish = dishMapper.getById(id);
            if (dish.getStatus() == StatusConstant.ENABLE) {
               // 当前菜品处于起售中，不能删除
               throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
            }
        }
        // 判断当前菜品是否能够删除 --- 是否被套餐关联了
        List<Long> setmealIds = setmealDishmapper.getSetmealIdsByDishId(ids);
        if (setmealIds != null && setmealIds.size() > 0) {
            // 当前菜品被套餐关联了，不能删除
            throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
        }
        // 删除菜品表中的菜品数据
        // TODO 优化SQL 批量删除
        for (Long id : ids) {
            dishMapper.deleteById(id);
            // 删除菜品关联的口味数据
            dishFlavorMapper.deleteByDishId(id);
        }
    }

    @Override
    public DishVO getByIdWithFlavor(Long id) {
        // 根据ID查询菜品数据
        Dish dish = dishMapper.getById(id);
        // 根据菜品ID查询口味数据
        List<DishFlavor> dishFlavors = dishFlavorMapper.getByDishId(id);
        // 将查询到的数据封装到VO
        DishVO dishVO = new DishVO();
        BeanUtils.copyProperties(dish, dishVO);
        dishVO.setFlavors(dishFlavors);
        return dishVO;
    }

    /**
     * 根据ID修改菜品基本信息和对应的口味信息
     * @param dishDTO
     */
    public void updateWithFlavor(DishDTO dishDTO) {
        // 修改菜品表基本信息
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        dishMapper.update(dish);
        // 修改口味表 (先删除后新增)
        dishFlavorMapper.deleteByDishId(dish.getId());
        // 对口味数据执行批量插入的操作
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if (flavors != null && flavors.size() > 0) {
            flavors.forEach(dishFlavor -> {
                dishFlavor.setDishId(dishDTO.getId());
            });
            dishFlavorMapper.insertBatch(flavors);
        }
    }

    @Override
    public void startOrStop(Integer status, Long id) {
        Dish dish = dishMapper.getById(id);
        dishMapper.updateStatus(status, id);

        // 如果下架，那么套餐也要下架
        if (status == StatusConstant.DISABLE) {
            List<Long> dishIds = new ArrayList<>();
            dishIds.add(id);
            List<Long> setmealIdsByDishId = setmealDishmapper.getSetmealIdsByDishId(dishIds);
            if (setmealIdsByDishId != null && setmealIdsByDishId.size() > 0) {
                for (Long longItem : setmealIdsByDishId) {
                    Setmeal setmeal = new Setmeal().builder().id(id).status(status).build();
                    setmealMapper.update(setmeal);
                }
            }

        }

    }

    @Override
    public List<Dish> list(Long categoryId) {
        Dish dish = new Dish().builder().categoryId(categoryId).status(StatusConstant.ENABLE).build();
        return dishMapper.getDishesByCategoryId(dish);
    }


}
