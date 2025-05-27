package ge.configs;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Machine {
    @JsonProperty("browser")
    private String browser;
    @JsonProperty("browserVersion")
    private String browserVersion;
    @JsonProperty("os")
    private String os;
    @JsonProperty("osVersion")
    private String osVersion;
    @JsonProperty("build")
    private String build;
    @JsonProperty("name")
    private String name;
}
