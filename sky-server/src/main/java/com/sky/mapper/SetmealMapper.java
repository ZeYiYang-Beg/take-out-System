package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SetmealMapper {
    @Select("select count(id) from setmeal where category_id = #{categoryId}")
    Integer countByCategoryId(Long id);

    @AutoFill(OperationType.UPDATE)
    void update(Setmeal setmeal);

    @AutoFill(OperationType.INSERT)
    void insert(Setmeal setmeal);

    /**
     * 向套餐中批量添加菜品
     * @param dishList
     */
    @AutoFill(OperationType.INSERT)
    void insertBatch(List<SetmealDish> dishList);

    /**
     * 分页查询套餐
     * @param setmealPageQueryDTO
     * @return
     */
    Page<SetmealVO> getAll(SetmealPageQueryDTO setmealPageQueryDTO);

    /**
     * 根据套餐id获取套餐对象
     * @param id
     * @return
     */
    @Select("select * from setmeal where id = #{id}")
    Setmeal getById(Long id);

    /**
     * 根据多个id获取套餐对象集合
     * @param ids
     * @return
     */
    List<Setmeal> getByIds(List<Long> ids);

    /**
     * 删除多个套餐
     * @param idsn
     */
    void delete(List<Long> idsn);

    /**
     * 动态条件查询套餐
     * @param setmeal
     * @return
     */
    List<Setmeal> list(Setmeal setmeal);

    /**
     * 根据套餐id查询菜品选项
     * @param setmealId
     * @return
     */
    @Select("select sd.name, sd.copies, d.image, d.description " +
            "from setmeal_dish sd left join dish d on sd.dish_id = d.id " +
            "where sd.setmeal_id = #{setmealId}")
    List<DishItemVO> getDishItemBySetmealId(Long setmealId);
}
