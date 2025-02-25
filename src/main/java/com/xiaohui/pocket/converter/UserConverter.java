package com.xiaohui.pocket.converter;

import com.xiaohui.pocket.model.entity.User;
import com.xiaohui.pocket.model.form.UserRegisterForm;
import org.mapstruct.Mapper;

/**
 * @author xiaohui
 * @since 2025/2/25
 */
@Mapper(componentModel = "spring")
public interface UserConverter {

    User toEntity(UserRegisterForm userRegisterForm);

}
