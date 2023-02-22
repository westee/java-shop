CREATE TABLE `ORDER`
(
    ID                  BIGINT PRIMARY KEY AUTO_INCREMENT,
    USER_ID             BIGINT,
    TOTAL_PRICE         DECIMAL,
    ADDRESS             VARCHAR(1024),
    EXPRESS_COMPANY     VARCHAR(16),
    EXPRESS_ID          VARCHAR(128),
    STATUS              VARCHAR(16),        -- PENDING 待付款 PAID 已付款 DELIVERED 物流中 RECEIVED 已收货
    CREATED_AT          TIMESTAMP NOT NULL DEFAULT NOW(),
    UPDATED_AT          TIMESTAMP NOT NULL DEFAULT NOW()
);
CREATE TABLE `ORDER_GOODS`
(
    ID       BIGINT PRIMARY KEY AUTO_INCREMENT,
    GOODS_ID BIGINT,
    NUMBER   DECIMAL
)