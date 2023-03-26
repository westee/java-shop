package com.wester.shop.generate;

import com.wester.shop.generate.Order;
import com.wester.shop.generate.OrderExample;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface OrderMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ORDER
     *
     * @mbg.generated Mon Mar 27 00:47:07 CST 2023
     */
    long countByExample(OrderExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ORDER
     *
     * @mbg.generated Mon Mar 27 00:47:07 CST 2023
     */
    int deleteByExample(OrderExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ORDER
     *
     * @mbg.generated Mon Mar 27 00:47:07 CST 2023
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ORDER
     *
     * @mbg.generated Mon Mar 27 00:47:07 CST 2023
     */
    int insert(Order row);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ORDER
     *
     * @mbg.generated Mon Mar 27 00:47:07 CST 2023
     */
    int insertSelective(Order row);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ORDER
     *
     * @mbg.generated Mon Mar 27 00:47:07 CST 2023
     */
    List<Order> selectByExample(OrderExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ORDER
     *
     * @mbg.generated Mon Mar 27 00:47:07 CST 2023
     */
    Order selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ORDER
     *
     * @mbg.generated Mon Mar 27 00:47:07 CST 2023
     */
    int updateByExampleSelective(@Param("row") Order row, @Param("example") OrderExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ORDER
     *
     * @mbg.generated Mon Mar 27 00:47:07 CST 2023
     */
    int updateByExample(@Param("row") Order row, @Param("example") OrderExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ORDER
     *
     * @mbg.generated Mon Mar 27 00:47:07 CST 2023
     */
    int updateByPrimaryKeySelective(Order row);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ORDER
     *
     * @mbg.generated Mon Mar 27 00:47:07 CST 2023
     */
    int updateByPrimaryKey(Order row);
}