package com.example.portfoliobackend.Models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

// FIXME generate failure  field _$Id70
@NoArgsConstructor
@Data
public class WorldClockBean {

    @JsonProperty("currentDateTime")
    private String currentDateTime;
    @JsonProperty("utcOffset")
    private String utcOffset;
    @JsonProperty("isDayLightSavingsTime")
    private Boolean isDayLightSavingsTime;
    @JsonProperty("dayOfTheWeek")
    private String dayOfTheWeek;
    @JsonProperty("timeZoneName")
    private String timeZoneName;
    @JsonProperty("currentFileTime")
    private Long currentFileTime;
    @JsonProperty("ordinalDate")
    private String ordinalDate;
    @JsonProperty("serviceResponse")
    private Object serviceResponse;
}
