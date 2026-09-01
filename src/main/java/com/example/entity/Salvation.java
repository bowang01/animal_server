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
@Schema(name = "Salvation Object", description = "")
public class Salvation implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @Schema(description = "Situation description")
    private String information;

    @Schema(description = "Scene photo")
    private String img;

    @Schema(description = "Location")
    private String address;

    @Schema(description = "Found time")
    private String time;

    @Schema(description = "Contact person")
    private String person;

    @Schema(description = "Contact")
    private String phone;

    @Schema(description = "Resolution status")
    private String state;


}
