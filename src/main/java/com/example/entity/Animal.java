package com.example.entity;

import com.baomidou.mybatisplus.annotation.IdType;
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
@Schema(name = "Animal Object", description = "")
public class Animal implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @Schema(description = "Animal name")
    private String nickname;

    @Schema(description = "Animal sex")
    private String sex;

    @Schema(description = "Type")
    private String type;

    @Schema(description = "Age")
    private String age;

    @Schema(description = "Animal photo")
    private String img;

    @Schema(description = "Activity range")
    private String address;

    @Schema(description = "Health status")
    private String status;

    @Schema(description = "Spayed/neutered")
    private String sterilization;

    @Schema(description = "Vaccination")
    private String vaccine;

    @Schema(description = "Adoption status")
    private String adopt;

    @Schema(description = "Other description")
    private String information;
    private String isAdopt;
    private Integer praise;


}
