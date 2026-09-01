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
@Schema(name = "Activity Object", description = "")
public class Activity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @Schema(description = "Title")
    private String name;

    @Schema(description = "Content")
    private String content;

    @Schema(description = "Time")
    private String time;

    @Schema(description = "Cover")
    private String img;

    @Schema(description = "Number of applicants")
    private Integer num;

    @Schema(description = "Address")
    private String address;


}
