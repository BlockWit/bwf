package com.blockwit.bwf.form;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NewPostForm {

    @NotNull
    private String body;

    @NotNull
    @Size(min = 3, max = 256)
    private String title;

}
