package com.example.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
@Schema(name = "Lost Object", description = "")
public class Lost implements Serializable {

    private static final long serialVersionUID = 1L;
@TableId(type = IdType.AUTO)
    @Schema(description = "id")
    private Integer id;

    @Schema(description = "Lost pet name")
    private String nickname;

    @Schema(description = "Type")
    private String type;

    @Schema(description = "Sex")
    private String sex;

    @Schema(description = "Contact person")
    private String person;

    @Schema(description = "Contact")
    private String phone;

    @Schema(description = "Lost / brought back")
    private String status1;

    @Schema(description = "Recovery status")
    private String status2;


}
