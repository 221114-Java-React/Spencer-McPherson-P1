package com.revature.daos;

import com.revature.models.ErsReimbursement;

import java.sql.SQLException;
import java.util.List;

public interface CrudDAO<T> {
    void save(T obj);


    void delete(T obj);
    void update(T obj) throws SQLException;
    T findById();


}