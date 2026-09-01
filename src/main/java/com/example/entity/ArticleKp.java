package com.example.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
@TableName("article_kp")
@Schema(name = "ArticleKp Object", description = "")
public class ArticleKp implements Serializable {

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

    @Schema(description = "Read count")
    private Integer read1;


}
