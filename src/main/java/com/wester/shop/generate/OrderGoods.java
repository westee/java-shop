package com.wester.shop.generate;

import java.io.Serializable;
import java.math.BigDecimal;

public class OrderGoods implements Serializable {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ORDER_GOODS.ID
     *
     * @mbg.generated Tue Feb 21 20:28:50 CST 2023
     */
    private Long id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ORDER_GOODS.GOODS_ID
     *
     * @mbg.generated Tue Feb 21 20:28:50 CST 2023
     */
    private Long goodsId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ORDER_GOODS.NUMBER
     *
     * @mbg.generated Tue Feb 21 20:28:50 CST 2023
     */
    private BigDecimal number;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table ORDER_GOODS
     *
     * @mbg.generated Tue Feb 21 20:28:50 CST 2023
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ORDER_GOODS.ID
     *
     * @return the value of ORDER_GOODS.ID
     *
     * @mbg.generated Tue Feb 21 20:28:50 CST 2023
     */
    public Long getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ORDER_GOODS.ID
     *
     * @param id the value for ORDER_GOODS.ID
     *
     * @mbg.generated Tue Feb 21 20:28:50 CST 2023
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ORDER_GOODS.GOODS_ID
     *
     * @return the value of ORDER_GOODS.GOODS_ID
     *
     * @mbg.generated Tue Feb 21 20:28:50 CST 2023
     */
    public Long getGoodsId() {
        return goodsId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ORDER_GOODS.GOODS_ID
     *
     * @param goodsId the value for ORDER_GOODS.GOODS_ID
     *
     * @mbg.generated Tue Feb 21 20:28:50 CST 2023
     */
    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ORDER_GOODS.NUMBER
     *
     * @return the value of ORDER_GOODS.NUMBER
     *
     * @mbg.generated Tue Feb 21 20:28:50 CST 2023
     */
    public BigDecimal getNumber() {
        return number;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ORDER_GOODS.NUMBER
     *
     * @param number the value for ORDER_GOODS.NUMBER
     *
     * @mbg.generated Tue Feb 21 20:28:50 CST 2023
     */
    public void setNumber(BigDecimal number) {
        this.number = number;
    }
}