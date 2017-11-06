package uk.nhs.digital.ps.test.acceptance.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

public class ConsumableAttachmentElement {

    private final WebElement rootElement;
    private final WebDriver webDriver;

    public ConsumableAttachmentElement(WebDriver webDriver, WebElement rootElement) {
        this.webDriver = webDriver;
        this.rootElement = rootElement;
    }

    public String getFullName() {
        return getHyperlink().getText();
    }

    public String getSizeText() {
        return rootElement.findElement(By.className("fileSize")).getText();
    }

    public void clickHyperlink() {
        new Actions(webDriver).moveToElement(getHyperlink()).click().perform();
    }

    private WebElement getHyperlink() {
        return rootElement.findElement(By.tagName("a"));
    }
}