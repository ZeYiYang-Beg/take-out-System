package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetmealService;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author
 * @version 1.0
 * @date 2024/7/9 4:20
 **/
@Service
@Slf4j
public class SetmealServiceImpl implements SetmealService {
    @Autowired
    private SetmealMapper setmealMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;

    @Override
    @Transactional
    public void save(SetmealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        // 向套餐表中插入数据
        setmealMapper.insert(setmeal);
        Long id = setmeal.getId();
        List<SetmealDish> dishList = setmealDTO.getSetmealDishes();
        for (SetmealDish setmealDish : dishList) {
            setmealDish.setSetmealId(id);
        }
        setmealMapper.insertBatch(dishList);
    }

    @Override
    public PageResult getAll(SetmealPageQueryDTO setmealPageQueryDTO) {
        PageHelper.startPage(setmealPageQueryDTO.getPage(), setmealPageQueryDTO.getPageSize());
        Page<SetmealVO> page = setmealMapper.getAll(setmealPageQueryDTO);
        return new PageResult(page.getTotal(), page.getResult());
    }

    // TODO 修改
    @Override
    public void startOrstop(Integer status, Long id) {
        // 查到套餐
        Setmeal setmeal = setmealMapper.getById(id);
        // 更改套餐的status
        setmeal.setStatus(status);
        // 更改数据库
        setmealMapper.update(setmeal);
    }

    @Override
    public void deleteByIds(List<Long> ids) {
        // 获取所有套餐的集合
        List<Setmeal> setmealList = setmealMapper.getByIds(ids);
        List<Long> idsn = new ArrayList<>();
        // 查看集合有没有东西
        if (setmealList != null && setmealList.size() > 0) {
            setmealList.forEach(setmeal -> {
                if (setmeal.getStatus() == 0) {
                    idsn.add(setmeal.getId());
                }
            });
        }
        if (idsn.isEmpty()) {
            throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
        }
        if (idsn != null && idsn.size() > 0) {
            // 删除集合
            setmealMapper.delete(idsn);
            setmealDishMapper.delete(idsn);
        }
    }

    // TODO 修改
    @Transactional
    @Override
    public void updateById(SetmealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        setmealMapper.update(setmeal);
        Long id = setmealDTO.getId();
        setmealDishMapper.deleteBySetMealId(id);
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        setmealDishes.forEach(setmealEach -> {
            setmealEach.setSetmealId(id);
        });
        setmealDishMapper.insertBatch(setmealDishes);
    }

    /**
     * 条件查询
     * @param setmeal
     * @return
     */
    public List<Setmeal> list(Setmeal setmeal) {
        List<Setmeal> list = setmealMapper.list(setmeal);
        return list;
    }

    /**
     * 根据id查询菜品选项
     * @param id
     * @return
     */
    public List<DishItemVO> getDishItemById(Long id) {
        return setmealMapper.getDishItemBySetmealId(id);
    }
}
