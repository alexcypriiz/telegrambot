package com.cryptocoin.telegram.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

/**
 * Created by Alexey Podlubnyy on 19.02.2022
 */
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Id {
    private String name;
    private String symbol;
    private String image;
    private String current_price;
    private String market_cap;
    private String market_cap_rank;
    private String fully_diluted_valuation;
    private String total_volume;
    private String high_24h;
    private String low_24h;
    private String price_change_percentage_24h;
    private String market_cap_change_24h;
    private String market_cap_change_percentage_24h;
    private String circulating_supply;
    private String total_supply;
    private String max_supply;
    private String ath;
    private String ath_change_percentage;
    private String ath_date;
    private String atl;
    private String atl_change_percentage;
    private String atl_date;
    private String last_updated;

    @Override
    public String toString() {
        return "Id{" +
                "name='" + name + '\'' +
                ", symbol='" + symbol + '\'' +
                ", image='" + image + '\'' +
                ", current_price='" + current_price + '\'' +
                ", market_cap='" + market_cap + '\'' +
                ", market_cap_rank='" + market_cap_rank + '\'' +
                ", fully_diluted_valuation='" + fully_diluted_valuation + '\'' +
                ", total_volume='" + total_volume + '\'' +
                ", high_24h='" + high_24h + '\'' +
                ", low_24h='" + low_24h + '\'' +
                ", price_change_percentage_24h='" + price_change_percentage_24h + '\'' +
                ", market_cap_change_24h='" + market_cap_change_24h + '\'' +
                ", market_cap_change_percentage_24h='" + market_cap_change_percentage_24h + '\'' +
                ", circulating_supply='" + circulating_supply + '\'' +
                ", total_supply='" + total_supply + '\'' +
                ", max_supply='" + max_supply + '\'' +
                ", ath='" + ath + '\'' +
                ", ath_change_percentage='" + ath_change_percentage + '\'' +
                ", ath_date='" + ath_date + '\'' +
                ", atl='" + atl + '\'' +
                ", atl_change_percentage='" + atl_change_percentage + '\'' +
                ", atl_date='" + atl_date + '\'' +
                ", last_updated='" + last_updated + '\'' +
                '}';
    }
}
