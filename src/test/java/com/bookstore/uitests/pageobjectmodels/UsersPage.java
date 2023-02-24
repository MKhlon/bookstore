package com.bookstore.uitests.pageobjectmodels;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class UsersPage {
    private WebDriver driver;

    public UsersPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    @FindBy(id = "users-btn")
    private WebElement usersButton;

    @FindBy(id = "user-id")
    private WebElement userIdInput;

    @FindBy(id = "search-btn")
    private WebElement searchButton;

    @FindBy(id = "create-btn")
    private WebElement createButton;

    @FindBy(id = "edit-btn")
    private WebElement editButton;

    @FindBy(id = "delete-btn")
    private WebElement deleteButton;

    @FindBy(id = "search-results")
    private WebElement searchResultsTable;

    @FindBy(id = "user-id-pop-up")
    private WebElement userIdPopUp;

    @FindBy(id = "close-btn")
    private WebElement closePopUpButton;

    @FindBy(xpath = "/html/body/div[3]/table/tbody/tr[2]/td")
    private WebElement userWasNotFoundMessage;

    public void fillUserIdValue(String userId) {
        userIdInput.clear();
        userIdInput.sendKeys(userId);
    }

    public void clickSearchButton() {
        searchButton.click();
    }

    public void clickCreateButton() {
        createButton.click();
    }

    public void clickEditButton() {
        editButton.click();
    }

    public void clickDeleteButton() {
        deleteButton.click();
    }

    public void clickUsersButton() {
        usersButton.click();
    }

    public int getResponseRowCount() {
        return searchResultsTable.findElements(By.tagName("tr")).size() - 1;
    }

    public String getResponseCellValue(int row, int column) {
        var desiredRow = searchResultsTable.findElement(By.xpath(".//tbody/tr[" + row + "]"));
        var desiredCell = desiredRow.findElement(By.xpath(".//td[" + column + "]"));
        return desiredCell.getText();
    }

    public String getUserIdFromPopup() {
        return userIdPopUp.getAttribute("value");
    }

    public void closePopUp() {
        closePopUpButton.click();
    }

    public String getUserWasNotFoundMessage() {
        return searchResultsTable.findElement(By.xpath(".//tbody/tr[2]/td")).getText();
    }
}
