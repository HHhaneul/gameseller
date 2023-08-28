package org.shopping.controllers.admins;

import lombok.Data;

import java.util.List;

@Data
public class CategoryForm {

    private String mode = "add";

    private String cateCd;

    private String cateNm;

    private boolean use;

    private List<Integer> chkNo;
}
