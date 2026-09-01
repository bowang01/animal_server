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
@Schema(name = "Feed Object", description = "")
public class Feed implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @Schema(description = "Environment photo 1")
    private String img;

    @Schema(description = "Environment photo 2")
    private String img2;

    @Schema(description = "Environment photo 3")
    private String img3;

    @Schema(description = "Address")
    private String address;

    @Schema(description = "Description")
    private String information;


}
