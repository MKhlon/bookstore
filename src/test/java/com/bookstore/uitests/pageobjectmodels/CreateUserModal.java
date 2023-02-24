package com.bookstore.uitests.pageobjectmodels;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class CreateUserModal {
    private WebDriver driver;

    public CreateUserModal(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    @FindBy(id = "user-name-modal")
    private WebElement userName;

    @FindBy(id = "email-modal")
    private WebElement email;

    @FindBy(id = "address-modal")
    private WebElement address;

    @FindBy(id = "phone-modal")
    private WebElement phone;

    @FindBy(id = "login-modal")
    private WebElement login;

    @FindBy(id = "password-modal")
    private WebElement password;

    @FindBy(id = "save-btn")
    private WebElement saveButton;

    @FindBy(id = "user-id-pop-up")
    private WebElement userIdFromPopup;

    public void clickSave() {
        saveButton.click();
    }

    public void fillUserName(String userNameValue) {
        userName.sendKeys(userNameValue);
    }

    public void fillEmail(String emailValue) {
        email.sendKeys(emailValue);
    }

    public void fillAddress(String addressValue) {
        address.sendKeys(addressValue);
    }

    public void fillPhone(String phoneValue) {
        phone.sendKeys(phoneValue);
    }

    public void fillLogin(String loginValue) {
        login.sendKeys(loginValue);
    }

    public void fillPassword(String passwordValue) {
        password.sendKeys(passwordValue);
    }
}
