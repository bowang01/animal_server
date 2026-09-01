package com.example.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 *
 * </p>
 *
 * @author
 * @since 2022-04-02
 */
@Getter
@Setter
@Schema(name = "Applcation Object", description = "")
public class Applcation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @Schema(description = "Name")
    private String name;

    @Schema(description = "Sex")
    private String sex;

    @Schema(description = "Age")
    private Integer age;

    @Schema(description = "Pet experience")
    private String experience;

    @Schema(description = "Pet")
    private String pet;

    @Schema(description = "Contact")
    private String phone;

    @Schema(description = "Marital status")
    private String married;

    @Schema(description = "Income")
    private String income;

    @Schema(description = "Profession")
    private String profession;

    @Schema(description = "Address")
    private String address;

    @Schema(description = "Adoption reason")
    private String reason;

    @Schema(description = "Status")
    private String state;

    @Schema(description = "Animal id")
    private Integer animalId;
    private Integer userId;
    @TableField(exist = false)
    private Animal animal;


}
