package com.digicore.omnexa.notification.lib.contract.email.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author mosh
 * @role software engineer
 * @createdOn 31 Thu Jul, 2025
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class EmailRequest {
  private Set<String> recipients;

  private String sender;

  private Set<String> ccs;

  private Set<String> bccs;

  private String subject;

  private String copy;

  private boolean useTemplate = false;

  private boolean isHtml = false;

  private String templateName;

  private String templatePath;

  private Map<String, Object> placeHolders;

  private List<MultipartFile> attachments = new ArrayList<>();

  public List<MultipartFile> getAttachments() {
    return attachments == null ? new ArrayList<>() : attachments;
  }
}
