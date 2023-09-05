package org.shopping.controllers.admins.support;

import lombok.Data;

import java.util.List;

@Data
public class RepeatQnAForm {
    private String mode = "add";

    private String question;

    private String answer;

    private boolean use;

    private List<Integer> chkNo;
}
