package org.shopping.commons;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PageDetails {
    private String code;
    private String name;
    private String url;

}
