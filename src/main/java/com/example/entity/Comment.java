package com.example.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 *
 * </p>
 *
 * @author
 * @since 2022-03-19
 */
@Getter
@Setter
@Schema(name = "Comment Object", description = "")
public class Comment implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @Schema(description = "Reply content")
    private String content;

    @Schema(description = "Replier")
    private String user;

    @Schema(description = "Reply time")
    private String time;

    @Schema(description = "Parent id")
    private Integer pid;

    @Schema(description = "Article id")
    private Integer articleId;
    private Integer type;
    @TableField(exist = false)
    private String avatar;
    @TableField(exist = false)
    private List<Comment> children;

}
