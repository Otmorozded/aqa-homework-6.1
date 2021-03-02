package ru.netology.test;

import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.data.DataHelper;
import ru.netology.page.DashboardPage;
import ru.netology.page.LoginPage;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TransferTest {

    DashboardPage dashboardPage;
    String numberFirstCard = "5559 0000 0000 0001";
    String numberSecondCard = "5559 0000 0000 0002";

    @BeforeEach
    void setUpAll() {
        val loginPage = open("http://localhost:9999", LoginPage.class);
        val authInfo = DataHelper.getAuthInfo();
        val verificationPage = loginPage.validLogin(authInfo);
        val verificationCode = DataHelper.getVerificationCode(authInfo);
        dashboardPage = verificationPage.validVerify(verificationCode);
        dashboardPage.refreshSUT();
    }

    @Test
    void shouldTransferFromFirstToSecondCard() {
        val dataCardFirst = DataHelper.getFirstCard();
        val transferPage = dashboardPage.selectCard(numberSecondCard);
        transferPage.moneyTransferFromCardToCard(dataCardFirst, 5000);
        $(withText("Ваши карты")).shouldBe(visible);
        int balanceAfterTransferFirstCard = dashboardPage.getBalanceCard(numberFirstCard);
        int balanceAfterTransferSecondCard = dashboardPage.getBalanceCard(numberSecondCard);
        assertEquals(5000, balanceAfterTransferFirstCard);
        assertEquals(15000, balanceAfterTransferSecondCard);

    }

    @Test
    void shouldTransferFromSecondCardToFirst() {
        val dataCardSecond = DataHelper.getSecondCard();
        val transferPage = dashboardPage.selectCard(numberFirstCard);
        transferPage.moneyTransferFromCardToCard(dataCardSecond, 5000);
        $(withText("Ваши карты")).shouldBe(visible);
        int balanceAfterTransferFirstCard = dashboardPage.getBalanceCard(numberFirstCard);
        int balanceAfterTransferSecondCard = dashboardPage.getBalanceCard(numberSecondCard);
        assertEquals(15000, balanceAfterTransferFirstCard);
        assertEquals(5000, balanceAfterTransferSecondCard);


    }

    @Test
    void shouldTransferMaximumSumOnFirstCard() {
        val dataCardSecond = DataHelper.getSecondCard();
        val transferPage = dashboardPage.selectCard(numberFirstCard);
        transferPage.moneyTransferFromCardToCard(dataCardSecond, 10000);
        $(withText("Ваши карты")).shouldBe(visible);
        int balanceAfterTransferFirstCard = dashboardPage.getBalanceCard(numberFirstCard);
        int balanceAfterTransferSecondCard = dashboardPage.getBalanceCard(numberSecondCard);
        assertEquals(20000, balanceAfterTransferFirstCard);
        assertEquals(0, balanceAfterTransferSecondCard);

    }

    @Test
    void shouldTransferMaximumSumOnSecondCard() {
        val dataCardFirst = DataHelper.getFirstCard();
        val transferPage = dashboardPage.selectCard(numberSecondCard);
        transferPage.moneyTransferFromCardToCard(dataCardFirst, 10000);
        $(withText("Ваши карты")).shouldBe(visible);
        int balanceAfterTransferFirstCard = dashboardPage.getBalanceCard(numberFirstCard);
        int balanceAfterTransferSecondCard = dashboardPage.getBalanceCard(numberSecondCard);
        assertEquals(0, balanceAfterTransferFirstCard);
        assertEquals(20000, balanceAfterTransferSecondCard);

    }


    @Test
    void shouldTransferZeroSumOnFirstCard() {
        val dataCardSecond = DataHelper.getSecondCard();
        val transferPage = dashboardPage.selectCard(numberFirstCard);
        transferPage.moneyTransferFromCardToCard(dataCardSecond, 0);
        $(withText("Ваши карты")).shouldBe(visible);
        int balanceAfterTransferFirstCard = dashboardPage.getBalanceCard(numberFirstCard);
        int balanceAfterTransferSecondCard = dashboardPage.getBalanceCard(numberSecondCard);
        assertEquals(10000, balanceAfterTransferFirstCard);
        assertEquals(10000, balanceAfterTransferSecondCard);
    }

    @Test
    void shouldTransferZeroSumOnSecondCard() {
        val dataCardFirst = DataHelper.getFirstCard();
        val transferPage = dashboardPage.selectCard(numberSecondCard);
        transferPage.moneyTransferFromCardToCard(dataCardFirst, 0);
        $(withText("Ваши карты")).shouldBe(visible);
        int balanceAfterTransferFirstCard = dashboardPage.getBalanceCard(numberFirstCard);
        int balanceAfterTransferSecondCard = dashboardPage.getBalanceCard(numberSecondCard);
        assertEquals(10000, balanceAfterTransferFirstCard);
        assertEquals(10000, balanceAfterTransferSecondCard);
    }

    //Тесты упадут, так как сервис пропускает суммы переводов более чем есть на счете

    @Test
    void shouldTransferMoreThanMaximumSumOnFirstCard() {
        val dataCardSecond = DataHelper.getSecondCard();
        val transferPage = dashboardPage.selectCard(numberFirstCard);
        transferPage.moneyTransferFromCardToCard(dataCardSecond, 11000);
        $(withText("Недостаточно средств для перевода!")).shouldBe(visible);

    }

    @Test
    void shouldTransferMoreThanMaximumSumOnSecondCard() {
        val dataCardFirst = DataHelper.getFirstCard();
        val transferPage = dashboardPage.selectCard(numberSecondCard);
        transferPage.moneyTransferFromCardToCard(dataCardFirst, 11000);
        $(withText("Недостаточно средств для перевода!")).shouldBe(visible);
    }


}
