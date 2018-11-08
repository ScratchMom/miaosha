package com.imooc.miaosha.validator;

import com.imooc.miaosha.config.UserArgumentResolver;
import com.imooc.miaosha.util.ValidatorUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author yifan
 * @date 2018/7/20 下午2:00
 */
public class IsMobileValidator implements ConstraintValidator<IsMobile,String> {    // 校验器
    private static Logger logger = LoggerFactory.getLogger(IsMobileValidator.class);

    private boolean required = false;

    @Override
    public void initialize(IsMobile constraintAnnotation) { // 初始化，拿到注解
        required = constraintAnnotation.required();
        logger.info("required === " + required);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (required) {
            logger.info("value == " + value);
            return ValidatorUtil.isMobiel(value);
        } else {
            if (StringUtils.isEmpty(value)) {
                return true;
            } else {
                return ValidatorUtil.isMobiel(value);
            }
        }
    }
}
