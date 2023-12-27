package com.example.repositories;

import com.example.models.UserEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, Long> {

    Optional<UserEntity> findByUsername(String username);  //metodo por default de jpa

//    @Query("select u from UserEntity u where u.username = ?1")   metodo personalizado en el cual con un query le decimos que va a consultar donde ?1 coresponde al primer parametro pasado
//    Optional<UserEntity> buscarPorNombre(String username);
}
