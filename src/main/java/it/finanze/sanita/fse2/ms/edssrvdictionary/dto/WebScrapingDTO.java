package it.finanze.sanita.fse2.ms.edssrvdictionary.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WebScrapingDTO {
    
    private String id;
    private String system;
    private String url;
    private boolean deleted;
    private boolean forceDraft;
    
}
