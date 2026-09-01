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
 * @since 2022-04-02
 */
@Getter
@Setter
@Schema(name = "Sterilization Object", description = "")
public class Sterilization implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    @Schema(description = "id")
    private Integer id;

    @Schema(description = "Stray animal id")
    private Integer animalId;

    @Schema(description = "Capture status")
    private String catch1;
    private String animalName;

    @Schema(description = "Sterilization status")
    private String sterilization;

    @Schema(description = "Release status")
    private String release1;

    @Schema(description = "Vaccine status")
    private String vaccine;


}
