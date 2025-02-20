package com.xiaohui.pocket.model.dto;

import lombok.*;

/**
 * 邮件信息
 *
 * @author xiaohui
 * @since 2025/2/19
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MailDto {

    /**
     * 收件人
     */
    private String to;

    /**
     * 邮件主题
     */
    private String subject;

    /**
     * 邮件内容
     */
    private String content;

    /**
     * 邮件内容是否是HTML
     */
    private boolean isHtml;

}
