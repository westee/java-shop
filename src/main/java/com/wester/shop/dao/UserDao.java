package com.wester.shop.dao;

import com.wester.shop.generate.User;
import com.wester.shop.generate.UserExample;
import com.wester.shop.generate.UserMapper;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.stereotype.Service;

@Service
public class UserDao {
    private SqlSessionFactory sqlSessionFactory;

    public UserDao(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    public void insertUser(User user){
        try (SqlSession sql = sqlSessionFactory.openSession(true);) {
            UserMapper mapper = sql.getMapper(UserMapper.class);
            mapper.insert(user);
        }
    }

    public User getUserByTel(String tel) {
        try (SqlSession sql = sqlSessionFactory.openSession(true);) {
            UserMapper mapper = sql.getMapper(UserMapper.class);
            UserExample userExample = new UserExample();
            userExample.createCriteria().andTelEqualTo(tel);
            return mapper.selectByExample(userExample).get(0);
        }
    }
}
