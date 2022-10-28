package org.exmaple.knife4j.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "my-map2")
public class FtpConfig {

    private Map<String, String> crCtDrDxMgMrRf;

    private Map<String, String> xa;

    /**
     * 病理-呼吸内镜
     */
    private Map<String, String> pathologyRespiratoryEndoscopy;
    /**
     * 超声
     */
    private Map<String, String> ultrasonic;

    /**
     * 肺功能
     */
    private Map<String, String> pulmonary;

    /**
     * 核医学
     */
    private Map<String, String> nuclearMedicine;

    /**
     * 消化内镜
     */
    private Map<String, String> digestiveEndoscopy;

    /**
     * 心导管室
     */
    private Map<String, String> cardiacCatheterizationLaboratory;
}
