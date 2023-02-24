package com.bookstore.uitests.tests;

import com.bookstore.uitests.pageobjectmodels.CreateUserModal;
import com.bookstore.uitests.pageobjectmodels.EditUserModal;
import com.bookstore.uitests.pageobjectmodels.UsersPage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.platform.suite.api.Suite;

import java.util.ArrayList;
import java.util.List;

import static com.bookstore.utils.Messages.ADDRESS_IS_NOT_AS_EXPECTED;
import static com.bookstore.utils.Messages.EMAIL_IS_NOT_AS_EXPECTED;
import static com.bookstore.utils.Messages.LOGIN_IS_NOT_AS_EXPECTED;
import static com.bookstore.utils.Messages.NUMBER_OF_ROWS_IS_NOT_AS_EXPECTED;
import static com.bookstore.utils.Messages.PHONE_IS_NOT_AS_EXPECTED;
import static com.bookstore.utils.Messages.ROLE_ID_IS_NOT_AS_EXPECTED;
import static com.bookstore.utils.Messages.ROLE_NAME_IS_NOT_AS_EXPECTED;
import static com.bookstore.utils.Messages.USER_ID_IS_NOT_AS_EXPECTED;
import static com.bookstore.utils.Messages.USER_NAME_IS_NOT_AS_EXPECTED;
import static com.bookstore.utils.Messages.USER_WAS_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

@Suite
public class UsersTest extends BaseTest {

    private final UsersPage usersPage = new UsersPage(super.driver);
    private final CreateUserModal createUserModal = new CreateUserModal(super.driver);
    private final EditUserModal editUserModal = new EditUserModal(super.driver);

    private ArrayList<String> userData = new ArrayList<>(List.of("testName", "userId", "test address", "test phone",
            "test@gmail.com", "test login", "CUSTOMER", "3"));

    @Test
    public void searchByUserIdTest() {
        // given
        final var userId = "2";
        final var resultSet = List.of("Test User", "2", "Sweden, Malmo", "+48111222333", "test_user@test.com",
                "testLogin2", "MANAGER", "2");
        // when
        usersPage.clickUsersButton();
        usersPage.fillUserIdValue(userId);
        usersPage.clickSearchButton();

        // then
        assumeTrue(usersPage.getResponseRowCount() == 1, NUMBER_OF_ROWS_IS_NOT_AS_EXPECTED);
        verifyResultSet(resultSet, 2);
    }

    @Test
    public void createUserTest() {
        // given
        usersPage.clickUsersButton();

        // when
        usersPage.clickCreateButton();
        fillSearchInputs(userData);
        createUserModal.clickSave();

        // then
        var userId = usersPage.getUserIdFromPopup();
        userData.set(1, userId);
        usersPage.closePopUp();
        usersPage.fillUserIdValue(userId);
        usersPage.clickSearchButton();
        verifyResultSet(userData, 2);
    }

    @Test
    public void editUserTest() {
        // given
        usersPage.clickUsersButton();
        // create new user
        usersPage.clickCreateButton();
        fillSearchInputs(userData);
        createUserModal.clickSave();
        var userId = usersPage.getUserIdFromPopup();
        usersPage.closePopUp();
        // find it
        usersPage.fillUserIdValue(userId);
        usersPage.clickSearchButton();

        // when
        usersPage.clickEditButton();
        var updatedUserData = new ArrayList<>(List.of("updatedName", userId, "updatedAddress", "updatedPhone",
                "test999@gmail.com", "updatedLogin", "CUSTOMER", "3"));
        updateUserFields(updatedUserData);
        editUserModal.clickSave();

        // then
        usersPage.fillUserIdValue(userId);
        usersPage.clickSearchButton();
        verifyResultSet(updatedUserData, 2);
    }

    @Test
    public void deleteUserTest() {
        // given
        usersPage.clickUsersButton();
        // create new user
        usersPage.clickCreateButton();
        fillSearchInputs(userData);
        createUserModal.clickSave();
        var userId = usersPage.getUserIdFromPopup();
        usersPage.closePopUp();
        // find it
        usersPage.fillUserIdValue(userId);
        usersPage.clickSearchButton();

        // then
        usersPage.clickDeleteButton();
        usersPage.fillUserIdValue(userId);
        usersPage.clickSearchButton();
        Assertions.assertEquals(USER_WAS_NOT_FOUND, usersPage.getUserWasNotFoundMessage());
    }

    private void verifyResultSet(List<String> resultSet, int rowNumber) {
        assertAll(
                () -> assertEquals(resultSet.get(0), usersPage.getResponseCellValue(rowNumber, 1),
                        USER_NAME_IS_NOT_AS_EXPECTED),
                () -> assertEquals(resultSet.get(1), usersPage.getResponseCellValue(rowNumber, 2),
                        USER_ID_IS_NOT_AS_EXPECTED),
                () -> assertEquals(resultSet.get(2), usersPage.getResponseCellValue(rowNumber, 3),
                        ADDRESS_IS_NOT_AS_EXPECTED),
                () -> assertEquals(resultSet.get(3), usersPage.getResponseCellValue(rowNumber, 4),
                        PHONE_IS_NOT_AS_EXPECTED),
                () -> assertEquals(resultSet.get(4), usersPage.getResponseCellValue(rowNumber, 5),
                        EMAIL_IS_NOT_AS_EXPECTED),
                () -> assertEquals(resultSet.get(5), usersPage.getResponseCellValue(rowNumber, 6),
                        LOGIN_IS_NOT_AS_EXPECTED),
                () -> assertEquals(resultSet.get(6), usersPage.getResponseCellValue(rowNumber, 7),
                        ROLE_NAME_IS_NOT_AS_EXPECTED),
                () -> assertEquals(resultSet.get(7), usersPage.getResponseCellValue(rowNumber, 8),
                        ROLE_ID_IS_NOT_AS_EXPECTED)
        );
    }

    public void fillSearchInputs(ArrayList<String> userData) {
        createUserModal.fillUserName(userData.get(0));
        createUserModal.fillAddress(userData.get(2));
        createUserModal.fillPhone(userData.get(3));
        createUserModal.fillEmail(userData.get(4));
        createUserModal.fillLogin(userData.get(5));
        createUserModal.fillPassword("test password");
    }

    public void updateUserFields(ArrayList<String> userData) {
        editUserModal.fillUserName(userData.get(0));
        editUserModal.fillAddress(userData.get(2));
        editUserModal.fillPhone(userData.get(3));
        editUserModal.fillEmail(userData.get(4));
        editUserModal.fillLogin(userData.get(5));
        editUserModal.fillPassword("test password");
    }
}
