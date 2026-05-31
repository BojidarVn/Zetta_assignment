package com.bojidar.zetta.linkchecker;

import com.bojidar.zetta.base.BaseTest;

import com.bojidar.zetta.config.Config;
import com.bojidar.zetta.models.LinkResult;
import com.bojidar.zetta.utils.CsvReportWriter;
import com.bojidar.zetta.utils.LinkChecker;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class LinkCheckerTest extends BaseTest {

    private final By mainContentLinks = By.cssSelector("#content a[href]");

    @Test(description = "Check links from The Internet main content area")
    public void shouldCheckAllMainContentLinksAndWriteCsvReport() {
        driver().get(Config.internetPageUrl());

        List<String> urls = driver().findElements(mainContentLinks)
                .stream()
                .map(link -> link.getAttribute("href"))
                .filter(url -> url != null && !url.isBlank())
                .distinct()
                .toList();

        Assert.assertFalse(urls.isEmpty(),
                "The Internet page should contain links in the main content area.");

        LinkChecker linkChecker = new LinkChecker();

        List<LinkResult> results = urls.stream()
                .map(linkChecker::check)
                .toList();

        Path csvPath = new CsvReportWriter().writeResults(results);

        Assert.assertTrue(Files.exists(csvPath),
                "CSV results file should be created.");

        Assert.assertEquals(results.size(), urls.size(),
                "There should be one result row per collected link.");
    }
}