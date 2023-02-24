package com.bookstore.uitests.pageobjectmodels;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class EditUserModal {

    private WebDriver driver;

    public EditUserModal(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    @FindBy(id = "save-modal-btn")
    private WebElement saveButton;

    @FindBy(id = "edit-user-name-modal")
    private WebElement userName;

    @FindBy(id = "edit-email-modal")
    private WebElement email;

    @FindBy(id = "edit-address-modal")
    private WebElement address;

    @FindBy(id = "edit-phone-modal")
    private WebElement phone;

    @FindBy(id = "edit-login-modal")
    private WebElement login;

    @FindBy(id = "edit-password-modal")
    private WebElement password;

    public void clickSave() {
        saveButton.click();
    }

    public void fillUserName(String userNameValue) {
        userName.clear();
        userName.sendKeys(userNameValue); }

    public void fillEmail(String emailValue) {
        email.clear();
        email.sendKeys(emailValue);
    }

    public void fillAddress(String addressValue) {
        address.clear();
        address.sendKeys(addressValue);
    }

    public void fillPhone(String phoneValue) {
        phone.clear();
        phone.sendKeys(phoneValue);
    }

    public void fillLogin(String loginValue) {
        login.clear();
        login.sendKeys(loginValue);
    }

    public void fillPassword(String passwordValue) {
        password.clear();
        password.sendKeys(passwordValue);
    }
}
