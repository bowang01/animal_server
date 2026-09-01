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

 */
@Getter
@Setter
@Schema(name = "Rescue Object", description = "")
public class Rescue implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @Schema(description = "Name")
    private String name;

    @Schema(description = "Address")
    private String addres;

    @Schema(description = "Photo")
    private String img;

    @Schema(description = "Contact person")
    private String person;

    @Schema(description = "Contact")
    private String phone;

    @Schema(description = "Description")
    private String information;


}
