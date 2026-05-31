package com.bojidar.zetta.models;

public record LinkResult(
        String url,
        int httpStatus,
        String pageTitle,
        String result) {

}